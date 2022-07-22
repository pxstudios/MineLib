package net.pxstudios.minelib.common.cooldown;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.NonNull;
import net.pxstudios.minelib.MineLibrary;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public final class PlayerCooldownApi {

    public static final long DEFAULT_RETURN_VALUE = -1L;

    static final Multimap<Player, Cooldown> cooldownsMultimap = HashMultimap.create();

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
        cooldown.setPlayer(player);

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

            if (cooldown.getName().equals(name)) {
                return cooldown.getMillisecondsDelay();
            }
        }

        return DEFAULT_RETURN_VALUE;
    }

    public long getLeftTime(@NonNull Player player, @NonNull String name) {
        cleanUp(player);

        for (Cooldown cooldown : cooldownsMultimap.get(player)) {

            if (cooldown.getName().equals(name)) {
                return cooldown.getMillisecondsDelay() - (System.currentTimeMillis() - cooldown.getInitMillisecondsTime());
            }
        }

        return DEFAULT_RETURN_VALUE;
    }

    public boolean hasCooldown(@NonNull Player player, @NonNull String name) {
        return getLeftTime(player, name) > 0;
    }

}
