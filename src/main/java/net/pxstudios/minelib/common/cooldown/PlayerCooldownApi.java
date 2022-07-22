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
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class PlayerCooldownApi {

    public enum CooldownFlag {

        WITH_AUTO_EXPIRATION,
        REMOVE_ON_PLAYER_QUIT,
    }

    public enum CooldownLeftReason {

        PLAYER_QUIT,
        TIME_EXPIRED,
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static final class Cooldown {

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

        Set<CooldownFlag> flagsSet = new HashSet<>();

        @NonFinal
        @Setter(AccessLevel.PRIVATE)
        Player usableTarget;

        @NonFinal
        @Setter(AccessLevel.PRIVATE)
        CompletableFuture<CooldownLeftReason> onLeft;

        @NonFinal
        WrappedBukkitTask automaticallyExpirationTask;

        public Cooldown withFlag(@NonNull CooldownFlag flag) {
            flagsSet.add(flag);
            return this;
        }

        boolean hasFlag(CooldownFlag flag) {
            return flagsSet.contains(flag);
        }

        boolean isExpired() {
            return System.currentTimeMillis() - initMillisecondsTime >= millisecondsDelay;
        }

        void enableAutoExpirationTask() {
            long delayAsTicks = (millisecondsDelay / 50L);

            automaticallyExpirationTask = MineLibrary.getLibrary().getBeater().runLater(delayAsTicks, () -> left(CooldownLeftReason.TIME_EXPIRED));
        }

        void left(CooldownLeftReason leftReason) {
            if (automaticallyExpirationTask != null) {
                automaticallyExpirationTask.cancel();
            }

            if (usableTarget != null) {
                cooldownsMultimap.remove(usableTarget, Cooldown.this);

                if (onLeft != null) {
                    onLeft.complete(leftReason);
                }
            }
        }
    }

    public static final long DEFAULT_RETURN_VALUE = -1L;

    private static final Multimap<Player, Cooldown> cooldownsMultimap = HashMultimap.create();

    public PlayerCooldownApi() {
        MineLibrary.getLibrary().getEventsSubscriber()
                .subscribe(PlayerQuitEvent.class, EventPriority.HIGHEST)
                .complete(event -> {

                    for (Cooldown cooldown : new HashSet<>(cooldownsMultimap.get(event.getPlayer()))) {

                        if (cooldown.hasFlag(CooldownFlag.REMOVE_ON_PLAYER_QUIT)) {
                            cooldown.left(CooldownLeftReason.PLAYER_QUIT);
                        }
                    }
                });
    }

    private void cleanUp(@NonNull Player player) {
        new HashSet<>(cooldownsMultimap.get(player)).forEach(cooldown -> {

            if (cooldown.isExpired()) {
                cooldown.left(CooldownLeftReason.TIME_EXPIRED);
            }
        });
    }

    private CompletableFuture<CooldownLeftReason> addCooldown0(@NonNull Player player, @NonNull Cooldown cooldown) {
        cleanUp(player);

        CompletableFuture<CooldownLeftReason> completableFuture = new CompletableFuture<>();

        cooldown.setOnLeft(completableFuture);
        cooldown.setUsableTarget(player);

        if (cooldown.hasFlag(CooldownFlag.WITH_AUTO_EXPIRATION)) {
            cooldown.enableAutoExpirationTask();
        }

        cooldownsMultimap.put(player, cooldown);
        return completableFuture;
    }

    public CompletableFuture<CooldownLeftReason> addCooldown(Player player, Cooldown cooldown) {
        return addCooldown0(player, cooldown);
    }

    public CompletableFuture<CooldownLeftReason> addCooldown(Player player, @NonNull String name, long millisecondsDelay) {
        return addCooldown(player, Cooldown.byMilliseconds(name, millisecondsDelay));
    }

    public long getCachedDelay(@NonNull Player player, @NonNull String name) {
        cleanUp(player);

        for (Cooldown cooldown : cooldownsMultimap.get(player)) {

            if (cooldown.name.equals(name)) {
                return cooldown.millisecondsDelay;
            }
        }

        return DEFAULT_RETURN_VALUE;
    }

    public long getLeftTime(@NonNull Player player, @NonNull String name) {
        cleanUp(player);

        for (Cooldown cooldown : cooldownsMultimap.get(player)) {

            if (cooldown.name.equals(name)) {
                return cooldown.millisecondsDelay - (System.currentTimeMillis() - cooldown.initMillisecondsTime);
            }
        }

        return DEFAULT_RETURN_VALUE;
    }

    public boolean hasCooldown(@NonNull Player player, @NonNull String name) {
        return getLeftTime(player, name) > 0;
    }

}
