package net.pxstudios.minelib.test;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudios.minelib.beat.BukkitBeater;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTimerTask;
import net.pxstudios.minelib.board.Board;
import net.pxstudios.minelib.board.BoardApi;
import net.pxstudios.minelib.board.BoardFlag;
import net.pxstudios.minelib.common.chat.ChatApi;
import net.pxstudios.minelib.common.chat.ChatDirection;
import net.pxstudios.minelib.common.config.PluginConfigManager;
import net.pxstudios.minelib.common.config.type.PropertiesPluginConfig;
import net.pxstudios.minelib.common.config.type.TextPluginConfig;
import net.pxstudios.minelib.common.config.type.YamlPluginConfig;
import net.pxstudios.minelib.common.item.BukkitItemApi;
import net.pxstudios.minelib.common.location.BukkitLocationApi;
import net.pxstudios.minelib.common.location.point.Point2D;
import net.pxstudios.minelib.common.location.point.Point3D;
import net.pxstudios.minelib.cooldown.PlayerCooldownApi;
import net.pxstudios.minelib.motd.ServerMotdApi;
import net.pxstudios.minelib.permission.PlayerPermissionApi;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import net.pxstudios.minelib.registry.BukkitRegistryManager;
import net.pxstudios.minelib.test.command.TestAbstractBukkitCommand;
import net.pxstudios.minelib.test.command.TestAbstractContextCommand;
import net.pxstudios.minelib.test.command.TestAbstractPlayerBukkitCommand;
import net.pxstudios.minelib.test.complex.TestComplexBlockListener;
import net.pxstudios.minelib.test.cooldown.TestPlayerCooldownListener;
import net.pxstudios.minelib.test.event.TestMLEventsListener;
import net.pxstudios.minelib.test.item.TestBukkitItemFactoryListener;
import net.pxstudios.minelib.test.permission.TestPermissionDatabaseProvider;
import net.pxstudios.minelib.test.registry.TestRegistryCommand;
import net.pxstudios.minelib.test.registry.TestRegistryListener;
import net.pxstudios.minelib.world.BukkitWorldsApi;
import net.pxstudios.minelib.world.WrapperBukkitWorld;
import net.pxstudios.minelib.world.rule.WorldGameRuleType;
import net.pxstudios.minelib.world.time.WorldTimeType;
import net.pxstudios.minelib.world.weather.WorldWeatherType;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class MineLibTest extends MinecraftPlugin {

    private void testContextCommands(MineLibrary mineLibrary) {
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractBukkitCommand(this), "bukkittest", "btest");
        // -> this command is not contains internal labels.

        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractPlayerBukkitCommand(this));
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractContextCommand(this));
    }

    private void testItemApi(MineLibrary mineLibrary) {
        BukkitItemApi bukkitItemApi = mineLibrary.getItemApi();
        getServer().getPluginManager().registerEvents(new TestBukkitItemFactoryListener(bukkitItemApi.getFactory()), this);

        Player player = Bukkit.getPlayerExact("itzstonlex");

        if (bukkitItemApi.hasInventoryItem(player.getInventory(), Material.DIAMOND)) {

            bukkitItemApi.fastWithdraw(player.getInventory(), Material.DIAMOND, 5);
            bukkitItemApi.depositItem(player.getInventory(), Material.DIAMOND, 2);
        }

        if (bukkitItemApi.isInventoryFull(player.getInventory(), Material.EMERALD)) {
            bukkitItemApi.fastWithdraw(player.getInventory(), Material.EMERALD, 64 * 5);
        }

        boolean equals = bukkitItemApi.equalsItems(new ItemStack(Material.ANVIL), new ItemStack(Material.ANVIL, 2));
        boolean equalsWithoutAmount = bukkitItemApi.equalsItemsWithoutAmount(new ItemStack(Material.ANVIL), new ItemStack(Material.ANVIL, 2));

        int totalAmountByPlayer = bukkitItemApi.search(player, new ItemStack(Material.ARROW));
        int totalAmountByInventory = bukkitItemApi.search(player.getInventory(), Material.ARROW);

        ItemStack glowingStickItem = bukkitItemApi.withGlowing(new ItemStack(Material.STICK));

        ItemStack donateItemIcon = bukkitItemApi.parseItem(getConfig().getConfigurationSection("Items.DonateItem"));
    }

    private void testMLEvents() {
        getServer().getPluginManager().registerEvents(new TestMLEventsListener(), this);
    }

    private void testAsyncCatcherBypass(MineLibrary mineLibrary) {
        AsyncCatcherBypass asyncCatcherBypass = mineLibrary.getAsyncCatcherBypass();
        asyncCatcherBypass.enableSpigotBypass();

        getServer().getScheduler().runTaskAsynchronously(this, () -> {

            for (Entity entity : getServer().getWorld("world").getEntities()) {
                asyncCatcherBypass.sync(entity::remove);
            }
        });
    }

    private void testBukkitBeater(MineLibrary mineLibrary) {
        BukkitBeater bukkitBeater = mineLibrary.getBeater();

        WrappedBukkitTimerTask timerTask = bukkitBeater.runTimer(2L, () -> Bukkit.broadcastMessage("Hello world!"));
        timerTask.waitFor(20L, () -> {

            Bukkit.getLogger().info("Task #" + timerTask.getTaskId() + " was cancelled!");
        });

        List<BukkitTask> pendingTasks = bukkitBeater.getPendingTasks();
        pendingTasks.forEach(bukkitTask -> {

            WrappedBukkitTask wrappedCancellingTask = bukkitBeater.runLater(60L, () -> bukkitBeater.cancel(bukkitTask));
            wrappedCancellingTask.waitAfter(() -> Bukkit.getLogger().info("Task #" + wrappedCancellingTask.getTaskId() + " was cancelled!"));
        });
    }

    private void testEventsSubscriber(MineLibrary mineLibrary) {
        mineLibrary.getEventsSubscriber().subscribe(PlayerJoinEvent.class, EventPriority.HIGHEST)
                .withIgnoreCancelled() // если ивент ранее был отменен, то он не будет использоваться

                .withPredication(event -> event.getPlayer().hasPermission("join.announce")) // ивент не будет работать на игроков, у которых нет права join.announce

                .withMaxUseCount(15) // если ивент использовался 15 раз или больше, то он разрегистрируется
                .withExpirationTime(5, TimeUnit.SECONDS) // если ивент работает уже 5 секунд, то он разрегистрируется
                .withExpiration(event -> event.getPlayer().getLevel() > 50) // если хоть один игрок зашел с уровнем > 50, то ивент больше не работает

                // обработчик события
                .complete(event -> {

                    Player player = event.getPlayer();
                    event.setJoinMessage(String.format("Player %s has joined!", player.getName()));
                });
    }

    private void testBukkitRegistry(MineLibrary mineLibrary) {
        BukkitRegistryManager bukkitRegistryManager = mineLibrary.getRegistryManager();

        bukkitRegistryManager.register(TestRegistryCommand.class);
        bukkitRegistryManager.register(TestRegistryListener.class);
    }

    private void testChatApi(MineLibrary mineLibrary) {
        ChatApi chat = mineLibrary.getChatApi();

        String testPermission = ("minelib.chat.test");

        // Messages send by anyone CommandSender.
        Player player = Bukkit.getPlayerExact("itzstonlex");

        chat.sendMessage(player, "Hello world!");
        chat.sendMessage(ChatDirection.CHAT, player, "Hello world!");
        chat.sendMessage(ChatDirection.TITLE, player, testPermission, "Hello world!");
        chat.sendMessage(player, testPermission, "Hello world!");

        // Messages broadcasting.
        boolean inConsole = true;

        chat.broadcastMessage("BROADCAST: Hello world!");
        chat.broadcastMessage("BROADCAST: Hello world!", ServerOperator::isOp);
        chat.broadcastMessage(testPermission, "BROADCAST: Hello world!");
        chat.broadcastMessage(inConsole, ChatDirection.ACTIONBAR, "BROADCAST: Hello world!", Player::isFlying);
    }

    private void testComplex() {
        getServer().getPluginManager().registerEvents(new TestComplexBlockListener(), this);
    }

    private void testLocationApi(MineLibrary mineLibrary) {
        BukkitLocationApi locationApi = mineLibrary.getLocationApi();

        // locations builders.
        Location firstLocation = locationApi.newBuilder().worldMain().x(356.4).y(67).z(93.15).build();
        Location secondLocation = locationApi.newBuilder().worldMain().x(358).y(30.5).z(8.445).build();

        // locations distance checking.
        boolean distanceX = locationApi.inDistanceByX(5, firstLocation, secondLocation);
        boolean distanceY = locationApi.inDistanceByY(100, firstLocation, secondLocation);
        boolean distanceZ = locationApi.inDistanceByZ(40.5, firstLocation, secondLocation);

        boolean distanceXZ = locationApi.inDistanceByXZ(250, firstLocation, secondLocation);

        boolean distanceXYZ = locationApi.inDistance(250, firstLocation, secondLocation);

        // geometry maths.
        boolean isInCuboid = locationApi.containsInCuboid(firstLocation, secondLocation,
                locationApi.newBuilder().worldMain().x(357).y(50).z(50).build());

        // geometry points.
        Point2D point2D = locationApi.newBuilder().x(49.1).y(90)
                .build2D()
                .add(0.9, 10)
                .divide(1, 2);

        Point3D point3D = point2D.to3D().add(0, 0, 50);
        Location location3D = locationApi.toLocation(firstLocation.getWorld(), point3D);

        // locations centralization.
        firstLocation = locationApi.toCenterLocation(firstLocation);
        secondLocation = locationApi.toCenterLocation(true, secondLocation.getBlock());

        // location parsing to string.
        locationApi.setLocationToStringFormatSeparator(";");

        String locationToString = locationApi.toString(firstLocation);
        Location locationFromString = locationApi.toLocation(locationToString);

        if (locationFromString.equals(firstLocation)) {
            super.log(Level.FINE, "Its work!");
        }
    }

    private void testConfigs(MineLibrary mineLibrary) {
        PluginConfigManager configManager = mineLibrary.getConfigManager();

        // Properties configs.
        PropertiesPluginConfig propertiesConfig = configManager.createPropertiesConfig(getDataFolder().toPath().resolve("config.properties").toFile());

        propertiesConfig.createNewResource();

        propertiesConfig.set("minelib-author", "pxstudios");
        propertiesConfig.save();

        // Text configs.
        TextPluginConfig textConfig = configManager.createTextConfig(getDataFolder().toPath().resolve("motd.txt").toFile());

        textConfig.createNewResource();

        textConfig.append("&bHello world!")
                .appendNewLine()
                .append("This is &ccolorized &eMotd!")
                .colorize('&');

        textConfig.save();

        // Yaml configs.
        YamlPluginConfig yamlConfig = configManager.createYamlConfig(getDataFolder().toPath().resolve("settings.yml").toFile());

        yamlConfig.createNewResource();

        yamlConfig.set("locations.cuboid.firstPosition", new Point3D(5, 5, 5));
        yamlConfig.set("locations.cuboid.secondPosition", new Point3D(10, 5, 10));
        yamlConfig.save();
    }

    private void testPlayerCooldownApi(MineLibrary mineLibrary) {
        PlayerCooldownApi playerCooldownApi = mineLibrary.getPlayerCooldownApi();
        getServer().getPluginManager().registerEvents(new TestPlayerCooldownListener(playerCooldownApi), this);
    }

    private void testPermissionsApi(MineLibrary mineLibrary) {
        PlayerPermissionApi permissionApi = mineLibrary.getPermissionApi();

        permissionApi.setEnabled(true);
        permissionApi.setEnabledOperatorsSystem(false);

        permissionApi.setPermissionDatabaseProvider(new TestPermissionDatabaseProvider("playerPermissions.yml", this));

        // Players management.
        String testPermission = "bukkit.broadcast";
        Player player = Bukkit.getPlayerExact("itzstonlex");

        permissionApi.addPermission(player, testPermission);

        boolean hasPermission = permissionApi.hasPermission(player, testPermission);
        permissionApi.removePermission(player, testPermission);

        if (hasPermission) {
            Bukkit.broadcast(ChatColor.YELLOW + "Test broadcast message", testPermission);
        }
    }

    private void testBoardApi(MineLibrary mineLibrary) {
        BoardApi boardApi = mineLibrary.getBoardApi();

        boardApi.getGlobalPresetsManager().add("Website", "&ewww.plazmix.net");

        // Create a new board
        Board board = boardApi.createOrGetBoard(DisplaySlot.SIDEBAR, "hub");
        board.getLocalPresetsManager().add("server_name", () -> Bukkit.getServerName().toUpperCase()); // u can use a supplier
        board.getLocalPresetsManager().add("server_ip", Bukkit.getIp()); // or common string

        board.addFlag(BoardFlag.REMOVE_ON_PLAYER_JOIN);
        board.addFlag(BoardFlag.REMOVE_ON_CHANGED);
        board.addFlag(BoardFlag.WITH_AUTOMATICALLY_STATIC_LINES_COLORIZE);
        board.addFlag(BoardFlag.USE_DISABLED_WORLD_SYSTEM);
        board.addFlag(BoardFlag.USE_GLOBAL_PRESETS);

        board.subscribePlayerEvents(mineLibrary.getEventsSubscriber());

        // Add disabled worlds.
        board.addDisabledWorld("survival");
        board.addDisabledWorld("bw_arena_1");

        // Set a display-name for objective of board.
        board.setDisplayName("preset:server_name"); // with presets support
        board.setDisplayName("HUB"); // without presets

        // Set a lines for objective of board.
        board.setLineSmart(5);
        board.setLineSmart(4, "&7Your name:");
        board.setLineSmart(3, HumanEntity::getName);
        board.setLineSmart(2);
        board.setLineSmart(1, "preset:Website");

        // Add automatically updater to board.
        board.update(10, () -> {

            ChatColor[] chatColors = ChatColor.values();
            ChatColor randomColor = chatColors[ThreadLocalRandom.current().nextInt(chatColors.length)];

            board.setDisplayName(randomColor + ChatColor.stripColor(board.getObjective().getDisplayName()));

            return true; // if continue the next repeat of update-action?
        });

        // Show board to the player.
        Player player = Bukkit.getPlayerExact("itzstonlex");
        boolean isViewing = board.addPlayerView(player);

        if (isViewing) {
            player.sendMessage("Board `hub` was success showed for you!");
        }
    }

    private void testServerMotdApi(MineLibrary mineLibrary) {
        ServerMotdApi serverMotdApi = mineLibrary.getServerMotdApi();

        serverMotdApi.enableApiEvents();
        serverMotdApi.setMotdColorAltChar('&');

        serverMotdApi.setMotd(Arrays.asList(
                "&b&lMINELIB &bTEST SERVER &7(1.8 - NEW)",
                "&fWeb-site: &ewww.plazmix.net"
        ));

        serverMotdApi.setMaxPlayers(2022);

        serverMotdApi.setServerIcon(getDataFolder().toPath().resolve("icon.png"));
    }

    private void testBukkitWorldsApi(MineLibrary mineLibrary) {
        BukkitWorldsApi bukkitWorldsApi = mineLibrary.getWorldsApi();
        WrapperBukkitWorld wrappedWorld = bukkitWorldsApi.getWrapper(bukkitWorldsApi.getMainWorld());

        wrappedWorld.addFlag(WrapperBukkitWorld.Flag.PLAYER_SPAWN_TELEPORT_ON_JOIN);
        wrappedWorld.addFlag(WrapperBukkitWorld.Flag.PLAYER_SPAWN_TELEPORT_ON_RESPAWN);
        // ... and more

        wrappedWorld.setSpawnLocation(new Point3D(0, 50, 0));

        wrappedWorld.setWeather(WorldWeatherType.SUN);
        wrappedWorld.setWeather(WorldWeatherType.THUNDER);
        // ... and more

        wrappedWorld.setTime(WorldTimeType.EARLY_SUNRISE);
        wrappedWorld.setTime(WorldTimeType.LATE_MORNING);
        wrappedWorld.setTime(WorldTimeType.MORNING);

        wrappedWorld.setFullTime(WorldTimeType.EARLY_EVENING);
        wrappedWorld.setFullTime(WorldTimeType.LATE_SUNRISE);
        wrappedWorld.setFullTime(WorldTimeType.SUNRISE);
        // ... and more

        wrappedWorld.setGameRuleValue(WorldGameRuleType.DO_MOB_SPAWNING, false);
        wrappedWorld.setGameRuleValue(WorldGameRuleType.MOB_GRIEF, false);
        wrappedWorld.setGameRuleValue(WorldGameRuleType.DO_DAYLIGHT_CYCLE, false);
        wrappedWorld.setGameRuleValue(WorldGameRuleType.DO_WEATHER_CYCLE, false);
        wrappedWorld.setGameRuleValue(WorldGameRuleType.SPAWN_RADIUS, 1);
        wrappedWorld.setGameRuleValue(WorldGameRuleType.RANDOM_TICK_SPEED, 0);
        // ... and more

        Location originLocation = wrappedWorld.getLocationAt(50, 120, 3);
        Point3D originPoint = new Point3D(originLocation);

        Collection<Entity> nearbyEntities1 = wrappedWorld.getNearbyEntities(originPoint);
        Collection<Entity> nearbyEntities2 = wrappedWorld.getNearbyEntities(originLocation, 5);
        Collection<Entity> nearbyEntities3 = wrappedWorld.getNearbyEntities(originPoint, 5, 5, 5);
        Collection<Entity> nearbyEntities4 = wrappedWorld.getNearbyEntities(EntityType.ZOMBIE, originPoint, 4);
        Collection<Entity> nearbyEntities5 = wrappedWorld.getNearbyEntities(EntityType.SKELETON, originLocation);

        List<Player> playersInWorld = wrappedWorld.getPlayers();
        Collection<Player> nearbyPlayers2 = wrappedWorld.getNearbyPlayers(originLocation);
        Collection<Player> nearbyPlayers3 = wrappedWorld.getNearbyPlayers(originPoint, 5, 5, 5);

        Collection<Sheep> sheepsInWorld = wrappedWorld.getEntitiesByClass(Sheep.class);
        Collection<Entity> cowsInWorld = wrappedWorld.getEntitiesByType(EntityType.COW);

        WorldGameRuleType worldGameRuleType = bukkitWorldsApi.getGameRuleByName("doFireTick");
        WorldWeatherType worldWeatherType = bukkitWorldsApi.getWeatherByName("storm");
        WorldTimeType nearbyWorldTimeType = bukkitWorldsApi.getNearbyTimeByTicks(1024);

        super.log(Level.INFO, worldGameRuleType);
        super.log(Level.INFO, worldWeatherType);
        super.log(Level.INFO, nearbyWorldTimeType);

        boolean canFolderDelete = true;

        bukkitWorldsApi.unloadBukkitWorld(bukkitWorldsApi.getLastLoadedWorld(), canFolderDelete)
                .thenAccept(unloadedWorld -> {

                    System.out.printf("World \"%s\" was success unloaded & deleted from server%n", unloadedWorld.getName());

                    bukkitWorldsApi.loadBukkitWorld(new WorldCreator(unloadedWorld.getName()).type(WorldType.FLAT))
                            .thenAccept(loadedWorld -> {

                                System.out.printf("World \"%s\" was success loaded%n", loadedWorld.getName());
                            });
                });
    }

    @Override
    public void postEnable(MineLibrary mineLibrary) {
        testContextCommands(mineLibrary);

        testItemApi(mineLibrary);

        testMLEvents();

        testAsyncCatcherBypass(mineLibrary);

        testBukkitBeater(mineLibrary);

        testEventsSubscriber(mineLibrary);

        testBukkitRegistry(mineLibrary);

        testChatApi(mineLibrary);

        testComplex();

        testLocationApi(mineLibrary);

        testConfigs(mineLibrary);

        testPlayerCooldownApi(mineLibrary);

        testPermissionsApi(mineLibrary);

        testBoardApi(mineLibrary);

        testServerMotdApi(mineLibrary);

        testBukkitWorldsApi(mineLibrary);
    }

}
