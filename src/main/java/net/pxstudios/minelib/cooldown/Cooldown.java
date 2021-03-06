package net.pxstudios.minelib.cooldown;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.pxstudios.minelib.beat.wrapper.WrapperBukkitTask;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Cooldown {

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
    @Setter(AccessLevel.PACKAGE)
    Player player;

    @NonFinal
    @Setter(AccessLevel.PACKAGE)
    CompletableFuture<CooldownLeftReason> onLeft;

    @NonFinal
    WrapperBukkitTask automaticallyExpirationTask;

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

    void enableAutoExpirationTask(MinecraftPlugin plugin) {
        long delayAsTicks = (millisecondsDelay / 50L);

        automaticallyExpirationTask = plugin.getMineLibrary().getBeater().runLater(delayAsTicks, () -> left(CooldownLeftReason.TIME_EXPIRED));
    }

    void left(CooldownLeftReason leftReason) {
        if (automaticallyExpirationTask != null) {
            automaticallyExpirationTask.cancel();
        }

        if (onLeft != null) {
            onLeft.complete(leftReason);
        }

        if (player != null) {
            PlayerCooldownApi.cooldownsMultimap.remove(player, Cooldown.this);
        }
    }
}