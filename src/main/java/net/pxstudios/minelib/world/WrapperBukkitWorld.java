package net.pxstudios.minelib.world;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.common.location.point.Point3D;
import net.pxstudios.minelib.world.rule.WorldGameRule;
import net.pxstudios.minelib.world.rule.WorldGameRuleType;
import net.pxstudios.minelib.world.time.WorldTimeType;
import net.pxstudios.minelib.world.weather.WorldWeatherType;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

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

    public final void setSpawnLocation(int x, int y, int z) {
        bukkit.setSpawnLocation(x, y, z);
    }

    public final void setSpawnLocation(double x, double y, double z) {
        setSpawnLocation((int) x, (int) y, (int) z);
    }

    public final void setSpawnLocation(@NonNull Vector vector) {
        setSpawnLocation(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public final void setSpawnLocation(@NonNull Point3D point3D) {
        setSpawnLocation(point3D.toVector());
    }

    public final void setSpawnLocation(@NonNull Location location) {
        setSpawnLocation(new Point3D(location));
    }

    public final void setSpawnLocation(@NonNull Block block) {
        setSpawnLocation(block.getLocation());
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
        boolean isThundering = weather == WorldWeatherType.THUNDER;

        if (isThundering) {
            weather = WorldWeatherType.RAIN;
        }

        bukkit.setWeatherDuration(weather.ordinal());

        if (isThundering) {

            bukkit.setThundering(true);
            bukkit.setThunderDuration(1);
        }
    }

    public final void setTime(WorldTimeType timeType) {
        bukkit.setTime(timeType.getMinecraftTicks());
    }

    public final void setFullTime(WorldTimeType timeType) {
        bukkit.setFullTime(timeType.getMinecraftTicks());
    }

    public final Collection<Entity> getNearbyEntities(Location origin, double radiusX, double radiusY, double radiusZ) {
        return bukkit.getNearbyEntities(origin, radiusX, radiusY, radiusZ);
    }

    public final Collection<Entity> getNearbyEntities(Point3D origin, double radiusX, double radiusY, double radiusZ) {
        Location location = MineLibrary.getLibrary().getLocationApi().toLocation(bukkit, origin);
        return getNearbyEntities(location, radiusX, radiusY, radiusZ);
    }

    public final Collection<Entity> getNearbyEntities(EntityType entityType, Location origin, double radiusX, double radiusY, double radiusZ) {
        return getNearbyEntities(origin, radiusX, radiusY, radiusZ).stream().filter(entity -> entity.getType() == entityType).collect(Collectors.toList());
    }

    public final Collection<Entity> getNearbyEntities(EntityType entityType, Point3D origin, double radiusX, double radiusY, double radiusZ) {
        return getNearbyEntities(origin, radiusX, radiusY, radiusZ).stream().filter(entity -> entity.getType() == entityType).collect(Collectors.toList());
    }

    public final Collection<Entity> getNearbyEntities(Location origin, double radius) {
        return getNearbyEntities(origin, radius, radius, radius);
    }

    public final Collection<Entity> getNearbyEntities(Point3D origin, double radius) {
        return getNearbyEntities(origin, radius, radius, radius);
    }

    public final Collection<Entity> getNearbyEntities(EntityType entityType, Location origin, double radius) {
        return getNearbyEntities(entityType, origin, radius, radius, radius);
    }

    public final Collection<Entity> getNearbyEntities(EntityType entityType, Point3D origin, double radius) {
        return getNearbyEntities(entityType, origin, radius, radius, radius);
    }

    public final Collection<Entity> getNearbyEntities(Location origin) {
        return getNearbyEntities(origin, 1);
    }

    public final Collection<Entity> getNearbyEntities(Point3D origin) {
        return getNearbyEntities(origin, 1);
    }

    public final Collection<Entity> getNearbyEntities(EntityType entityType, Location origin) {
        return getNearbyEntities(entityType, origin, 1);
    }

    public final Collection<Entity> getNearbyEntities(EntityType entityType, Point3D origin) {
        return getNearbyEntities(entityType, origin, 1);
    }

    public final Collection<Player> getNearbyPlayers(Location origin, double radiusX, double radiusY, double radiusZ) {
        return getNearbyEntities(EntityType.PLAYER, origin, radiusX, radiusY, radiusZ).stream().map(entity -> (Player) entity).collect(Collectors.toList());
    }

    public final Collection<Player> getNearbyPlayers(Point3D origin, double radiusX, double radiusY, double radiusZ) {
        return getNearbyEntities(EntityType.PLAYER, origin, radiusX, radiusY, radiusZ).stream().map(entity -> (Player) entity).collect(Collectors.toList());
    }

    public final Collection<Player> getNearbyPlayers(Location origin, double radius) {
        return getNearbyPlayers(origin, radius, radius, radius);
    }

    public final Collection<Player> getNearbyPlayers(Point3D origin, double radius) {
        return getNearbyPlayers(origin, radius, radius, radius);
    }

    public final Collection<Player> getNearbyPlayers(Location origin) {
        return getNearbyPlayers(origin, 1);
    }

    public final Collection<Player> getNearbyPlayers(Point3D origin) {
        return getNearbyPlayers(origin, 1);
    }
    
    public final List<Player> getPlayers() {
        return bukkit.getPlayers();
    }
    
    public final List<Entity> getEntities() {
        return bukkit.getEntities();
    }

    public final List<LivingEntity> getLivingEntities() {
        return bukkit.getLivingEntities();
    }

    public final <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        return bukkit.getEntitiesByClass(cls);
    }

    @SafeVarargs
    public final Collection<Entity> getEntitiesByClasses(Class<? extends Entity>... cls) {
        return bukkit.getEntitiesByClasses(cls);
    }

    public final Collection<Entity> getEntitiesByType(EntityType entityType) {
        return getEntitiesByClasses(entityType.getEntityClass());
    }

    public final Location getLocationAt(double x, double y, double z, float yaw, float pitch) {
        return new Location(bukkit, x, y, z, yaw, pitch);
    }
    
    public final Location getLocationAt(double x, double y, double z) {
        return getLocationAt(x, y, z, 0f, 0f);
    }

    public final Location getLocationAt(Vector vector) {
        return MineLibrary.getLibrary().getLocationApi().toLocation(bukkit, vector);
    }

    public final Location getLocationAt(Point3D point3D) {
        return getLocationAt(point3D.toVector());
    }

    public final Block getBlockAt(int x, int y, int z) {
        return getLocationAt(x, y, z).getBlock();
    }

    public final Block getBlockAt(Vector vector) {
        return getLocationAt(vector).getBlock();
    }

    public final Block getBlockAt(Point3D point3D) {
        return getLocationAt(point3D).getBlock();
    }
}
