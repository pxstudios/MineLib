package net.pxstudios.minelib.world;

import com.google.common.collect.Iterables;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.event.EventsSubscriber;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import net.pxstudios.minelib.world.rule.WorldGameRule;
import net.pxstudios.minelib.world.rule.WorldGameRuleType;
import net.pxstudios.minelib.world.time.WorldTimeType;
import net.pxstudios.minelib.world.weather.WorldWeatherType;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
public final class BukkitWorldsApi {

    private final MinecraftPlugin plugin;

    private final Map<World, WrapperBukkitWorld> wrapperWorldsMap = new HashMap<>();

    private final Map<String, CompletableFuture<World>> worldsFuturesMap = new HashMap<>();

    public WrapperBukkitWorld getWrapper(@NonNull World world) {
        return wrapperWorldsMap.computeIfAbsent(world, WrapperBukkitWorld::new);
    }

    public WrapperBukkitWorld getWrapper(@NonNull String name) {
        return getWrapper(plugin.getServer().getWorld(name));
    }

    public void setSpawnLocation(World world, Location location) {
        world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void setSpawnLocation(WrapperBukkitWorld world, Location location) {
        setSpawnLocation(world.getBukkit(), location);
    }

    public World getMainWorld() {
        return plugin.getServer().getWorlds().get(0);
    }

    public World getLastLoadedWorld() {
        return Iterables.getLast(plugin.getServer().getWorlds());
    }

    public void setDefaultGameRule(WrapperBukkitWorld world, WorldGameRuleType type) {
        world.setDefaultGameRuleValue(type);
    }

    public void setDefaultGameRule(World world, WorldGameRuleType type) {
        setDefaultGameRule(getWrapper(world), type);
    }

    public WorldGameRule getGameRule(WrapperBukkitWorld world, WorldGameRuleType type) {
        return world.getGameRuleValue(type);
    }

    public WorldGameRule getGameRule(World world, WorldGameRuleType type) {
        return getGameRule(getWrapper(world), type);
    }

    public WorldGameRuleType getGameRuleByName(String name) {
        for (WorldGameRuleType gameRuleType : WorldGameRuleType.values()) {

            if (gameRuleType.name().equalsIgnoreCase(name) || gameRuleType.getName().equalsIgnoreCase(name)) {
                return gameRuleType;
            }
        }

        return null;
    }

    public WorldWeatherType getWeatherByName(String name) {
        for (WorldWeatherType weatherType : WorldWeatherType.values()) {

            if (weatherType.isNamedBy(name)) {
                return weatherType;
            }
        }

        return null;
    }

    public WorldTimeType getNearbyTimeByTicks(long minecraftTicks) {
        for (WorldTimeType timeType : WorldTimeType.values()) {

            if (timeType.getMinecraftTicks() <= minecraftTicks) {
                return timeType;
            }
        }

        return null;
    }

    public CompletableFuture<World> loadBukkitWorld(WorldCreator worldCreator) {
        World world = worldCreator.createWorld();

        plugin.getServer().getWorlds().add(world);

        if (!isSubscribedOnWorldEvents) {
            this.subscribeWorldEvents(plugin.getMineLibrary().getEventsSubscriber());
        }

        CompletableFuture<World> completableFuture = new CompletableFuture<>();
        worldsFuturesMap.put(worldCreator.name().toLowerCase(), completableFuture);

        return completableFuture;
    }

    public CompletableFuture<World> loadBukkitWorld(String name) {
        return loadBukkitWorld(new WorldCreator(name));
    }

    public CompletableFuture<World> unloadBukkitWorld(World world, boolean canFolderDelete) {
        plugin.getServer().unloadWorld(world, false);
        plugin.getServer().getWorlds().remove(world);

        if (!isSubscribedOnWorldEvents) {
            this.subscribeWorldEvents(plugin.getMineLibrary().getEventsSubscriber());
        }

        CompletableFuture<World> completableFuture = new CompletableFuture<>();

        if (canFolderDelete) {
            completableFuture.thenAccept(handle -> {
                try {
                    FileUtils.forceDelete(handle.getWorldFolder());
                }
                catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            });
        }

        worldsFuturesMap.put(world.getName().toLowerCase(), completableFuture);

        return completableFuture;
    }

    public CompletableFuture<World> unloadBukkitWorld(World world) {
        return unloadBukkitWorld(world, false);
    }

    private boolean isSubscribedOnWorldEvents;

    private void subscribeWorldEvents(EventsSubscriber eventsSubscriber) {
        if (isSubscribedOnWorldEvents) {
            return;
        }

        Consumer<WorldEvent> worldFuturesEventConsumer = (event) -> {
            World world = event.getWorld();

            CompletableFuture<World> completableFuture = worldsFuturesMap.remove(world.getName().toLowerCase());

            if (completableFuture != null) {
                completableFuture.complete(world);
            }
        };

        eventsSubscriber.subscribe(WorldLoadEvent.class, EventPriority.HIGHEST).complete(worldFuturesEventConsumer::accept);
        eventsSubscriber.subscribe(WorldUnloadEvent.class, EventPriority.HIGHEST).complete(worldFuturesEventConsumer::accept);

        BiConsumer<WrapperBukkitWorld.Flag, PlayerEvent> worldJoinEventConsumer = (flag, event) -> {

            Player player = event.getPlayer();

            WrapperBukkitWorld world = getWrapper(event.getPlayer().getWorld());

            if (world.hasFlag(flag)) {
                player.teleport(world.getBukkit().getSpawnLocation());
            }
        };

        eventsSubscriber.subscribe(PlayerJoinEvent.class).complete(event -> worldJoinEventConsumer.accept(WrapperBukkitWorld.Flag.PLAYER_SPAWN_TELEPORT_ON_JOIN, event));
        eventsSubscriber.subscribe(PlayerRespawnEvent.class).complete(event -> worldJoinEventConsumer.accept(WrapperBukkitWorld.Flag.PLAYER_SPAWN_TELEPORT_ON_JOIN, event));
        eventsSubscriber.subscribe(PlayerChangedWorldEvent.class).complete(event -> worldJoinEventConsumer.accept(WrapperBukkitWorld.Flag.PLAYER_SPAWN_TELEPORT_ON_CHANGED, event));

        isSubscribedOnWorldEvents = true;
    }

}
