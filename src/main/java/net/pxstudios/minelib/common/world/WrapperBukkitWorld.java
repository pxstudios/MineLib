package net.pxstudios.minelib.common.world;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.location.point.Point3D;
import net.pxstudios.minelib.common.world.rule.WorldGameRule;
import net.pxstudios.minelib.common.world.rule.WorldGameRuleType;
import net.pxstudios.minelib.common.world.time.WorldTimeType;
import net.pxstudios.minelib.common.world.weather.WorldWeatherType;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class WrapperBukkitWorld {

    public enum Flag {

        PLAYER_SPAWN_TELEPORT_ON_JOIN,

        PLAYER_SPAWN_TELEPORT_ON_CHANGED,

        PLAYER_SPAWN_TELEPORT_ON_RESPAWN,
    }

    private final Set<Flag> flagsSet = new HashSet<>();

    @Getter
    private final World bukkit;

    public final Set<Flag> getActiveFlags() {
        return Collections.unmodifiableSet(flagsSet);
    }

    public final boolean addFlag(@NonNull Flag flag) {
        return flagsSet.add(flag);
    }

    public final boolean removeFlag(@NonNull Flag flag) {
        return flagsSet.remove(flag);
    }

    public final boolean hasFlag(@NonNull Flag flag) {
        return flagsSet.contains(flag);
    }

    public final void setSpawnLocation(double x, double y, double z) {
        bukkit.setSpawnLocation((int) x, (int) y, (int) z);
    }

    public final void setSpawnLocation(@NonNull Point3D point3D) {
        setSpawnLocation(point3D.x(), point3D.y(), point3D.z());
    }

    public final void setSpawnLocation(@NonNull Location location) {
        setSpawnLocation(new Point3D(location));
    }

    public final void setGameRuleValue(WorldGameRuleType type, String value) {
        bukkit.setGameRuleValue(type.getName(), value);
    }

    public final void setGameRuleValue(WorldGameRuleType type, boolean value) {
        bukkit.setGameRuleValue(type.getName(), Boolean.toString(value));
    }

    public final void setGameRuleValue(WorldGameRuleType type, int value) {
        bukkit.setGameRuleValue(type.getName(), Integer.toString(value));
    }

    public final void setGameRuleValue(WorldGameRuleType type, double value) {
        bukkit.setGameRuleValue(type.getName(), Double.toString(value));
    }

    public final void setDefaultGameRuleValue(WorldGameRuleType type) {
        setGameRuleValue(type, type.getDefaultValue());
    }

    public final WorldGameRule getGameRuleValue(WorldGameRuleType type) {
        return new WorldGameRule(type, bukkit.getGameRuleValue(type.getName()));
    }

    public final void setWeatherByBukkit(WeatherType weather) {
        bukkit.setWeatherDuration(weather == WeatherType.CLEAR ? 0 : 1);
    }

    public final void setWeather(WorldWeatherType weather) {
        if (weather == WorldWeatherType.THUNDER) {
            weather = WorldWeatherType.RAIN;

            bukkit.setThundering(true);
            bukkit.setThunderDuration(1);
        }

        bukkit.setWeatherDuration(weather.ordinal());
    }

    public final void setTime(WorldTimeType timeType) {
        bukkit.setTime(timeType.getMinecraftTicks());
    }

    public final void setFullTime(WorldTimeType timeType) {
        bukkit.setFullTime(timeType.getMinecraftTicks());
    }

}
