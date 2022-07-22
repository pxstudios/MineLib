package net.pxstudios.minelib.common.cooldown;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class PlayerCooldownApi {

    public enum CooldownLeftReason {
        PLAYER_QUIT, TIME_EXPIRED
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class Cooldown {

        public static Cooldown byMilliseconds(String name, long millisecondsDelay) {
            return new Cooldown(name, System.currentTimeMillis(), millisecondsDelay);
        }

        public static Cooldown bySeconds(String name, long secondsDelay) {
            return byMilliseconds(name, TimeUnit.SECONDS.toMillis(secondsDelay));
        }

        public static Cooldown byTicks(String name, long bukkitTicksDelay) {
            return byMilliseconds(name, bukkitTicksDelay * 50L);
        }

        String name;

        long initMillisecondsTime, millisecondsDelay;

        @NonFinal
        @Setter(AccessLevel.PRIVATE)
        Player usableTarget;

        @NonFinal
        @Setter(AccessLevel.PRIVATE)
        CompletableFuture<CooldownLeftReason> onLeft;

        @NonFinal
        WrappedBukkitTask leftActionTask;

        void startLeftActionTask(PlayerCooldownApi cooldownApi) {
            long delayAsTicks = (millisecondsDelay / 50L);
            leftActionTask = MineLibrary.getLibrary().getBeater().runLater(delayAsTicks, () -> left(CooldownLeftReason.TIME_EXPIRED, cooldownApi));
        }

        void left(CooldownLeftReason leftReason, PlayerCooldownApi cooldownApi) {
            if (leftActionTask != null) {
                leftActionTask.cancel();
            }

            if (usableTarget != null) {
                cooldownApi.cleanUp(usableTarget);

                if (onLeft != null) {
                    onLeft.complete(leftReason);
                }
            }
        }

        boolean isExpired() {
            return System.currentTimeMillis() - initMillisecondsTime >= millisecondsDelay;
        }
    }

    private final Multimap<Player, Cooldown> cooldownsMap = HashMultimap.create();

    public PlayerCooldownApi() {
        MineLibrary.getLibrary().getEventsSubscriber()
                .subscribe(PlayerQuitEvent.class, EventPriority.HIGHEST)
                .complete(event -> {

                    for (Cooldown cooldown : cooldownsMap.removeAll(event.getPlayer())) {
                        cooldown.left(CooldownLeftReason.PLAYER_QUIT, this);
                    }
                });
    }

    private void cleanUp(@NonNull Player player) {
        new HashSet<>(cooldownsMap.get(player)).forEach(cooldown -> {

            if (cooldown.isExpired()) {
                cooldown.left(CooldownLeftReason.TIME_EXPIRED, this);

                cooldownsMap.remove(player, cooldown);
            }
        });
    }

    private CompletableFuture<CooldownLeftReason> addCooldown0(@NonNull Player player, @NonNull Cooldown cooldown) {
        cleanUp(player);

        CompletableFuture<CooldownLeftReason> completableFuture = new CompletableFuture<>();

        cooldown.setOnLeft(completableFuture);
        cooldown.setUsableTarget(player);

        cooldown.startLeftActionTask(this);

        cooldownsMap.put(player, cooldown);
        return completableFuture;
    }

    public CompletableFuture<CooldownLeftReason> addCooldown(Player player, Cooldown cooldown) {
        return addCooldown0(player, cooldown);
    }

    public CompletableFuture<CooldownLeftReason> addCooldown(Player player, @NonNull String name, long millisecondsDelay) {
        return addCooldown(player, Cooldown.byMilliseconds(name, millisecondsDelay));
    }

    public long getCooldownDelay(@NonNull Player player, @NonNull String name) {
        cleanUp(player);

        for (Cooldown cooldown : cooldownsMap.get(player)) {

            if (cooldown.name.equals(name)) {
                return cooldown.millisecondsDelay;
            }
        }

        return -1L;
    }

    public long getCooldownLeft(@NonNull Player player, @NonNull String name) {
        cleanUp(player);

        for (Cooldown cooldown : cooldownsMap.get(player)) {

            if (cooldown.name.equals(name)) {
                return cooldown.millisecondsDelay - (System.currentTimeMillis() - cooldown.initMillisecondsTime);
            }
        }

        return -1L;
    }

    public boolean hasCooldown(@NonNull Player player, @NonNull String name) {
        return getCooldownLeft(player, name) > 0;
    }

}
