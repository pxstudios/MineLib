package net.pxstudtios.minelib.test;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudios.minelib.beat.BukkitBeater;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTimerTask;
import net.pxstudios.minelib.common.chat.ChatApi;
import net.pxstudios.minelib.common.chat.ChatDirection;
import net.pxstudios.minelib.common.config.PluginConfigManager;
import net.pxstudios.minelib.common.config.type.PropertiesPluginConfig;
import net.pxstudios.minelib.common.config.type.TextPluginConfig;
import net.pxstudios.minelib.common.config.type.YamlPluginConfig;
import net.pxstudios.minelib.common.item.BukkitItemFactory;
import net.pxstudios.minelib.common.location.BukkitLocationApi;
import net.pxstudios.minelib.common.location.point.Point2D;
import net.pxstudios.minelib.common.location.point.Point3D;
import net.pxstudios.minelib.registry.BukkitRegistryManager;
import net.pxstudtios.minelib.test.command.TestAbstractBukkitCommand;
import net.pxstudtios.minelib.test.command.TestAbstractContextCommand;
import net.pxstudtios.minelib.test.command.TestAbstractPlayerBukkitCommand;
import net.pxstudtios.minelib.test.complex.TestComplexBlockListener;
import net.pxstudtios.minelib.test.config.TestPropertiesConfig;
import net.pxstudtios.minelib.test.config.TestTextConfig;
import net.pxstudtios.minelib.test.config.TestYamlConfig;
import net.pxstudtios.minelib.test.item.TestBukkitItemListener;
import net.pxstudtios.minelib.test.registry.TestRegistryCommand;
import net.pxstudtios.minelib.test.registry.TestRegistryListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class MineLibTest extends JavaPlugin {

    private final MineLibrary mineLibrary = MineLibrary.getLibrary();

    private void registerTestCommands() {
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractBukkitCommand(this), "bukkittest", "btest");
        // -> that command is not contains internal labels.

        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractPlayerBukkitCommand(this));
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractContextCommand(this));
    }

    private void registerItemListener() {
        BukkitItemFactory bukkitItemFactory = mineLibrary.getItemFactory();
        getServer().getPluginManager().registerEvents(new TestBukkitItemListener(bukkitItemFactory), this);
    }

    private void testAsyncCatcherBypass() {
        AsyncCatcherBypass asyncCatcherBypass = mineLibrary.getAsyncCatcherBypass();
        asyncCatcherBypass.enableSpigotBypass();

        getServer().getScheduler().runTaskAsynchronously(this, () -> {

            for (Entity entity : getServer().getWorld("world").getEntities()) {
                asyncCatcherBypass.sync(entity::remove);
            }
        });
    }

    private void testBukkitBeater() {
        BukkitBeater bukkitBeater = mineLibrary.getBeater();

        WrappedBukkitTimerTask timerTask = bukkitBeater.runTimer(2L, () -> Bukkit.broadcastMessage("Hello world!"));
        timerTask.waitFor(20L, () -> {

            Bukkit.getLogger().info("Task #" + timerTask.getTaskId() + " was cancelled!");
        });

        List<BukkitTask> pendingTasks = bukkitBeater.callSync(bukkitBeater::getPendingTasks).get();
        pendingTasks.forEach(bukkitTask -> {

            WrappedBukkitTask wrappedCancellingTask = bukkitBeater.runLater(60L, () -> bukkitBeater.cancel(bukkitTask));
            wrappedCancellingTask.waitAfter(() -> Bukkit.getLogger().info("Task #" + wrappedCancellingTask.getTaskId() + " was cancelled!"));
        });
    }

    private void testEventsSubscriber() {
        mineLibrary.getEventsSubscriber().subscribe(PlayerJoinEvent.class, EventPriority.HIGHEST)
                .ignoreCancelled() // если ивент ранее был отменен, то он не будет использоваться

                .useFilter(event -> event.getPlayer().hasPermission("join.announce")) // ивент не будет работать на игроков, у которых нет права join.announce

                .useExpireMaxCount(15) // если ивент использовался 15 раз или больше, то он разрегистрируется
                .useExpireTime(5, TimeUnit.SECONDS) // если ивент работает уже 5 секунд, то он разрегистрируется
                .useExpire(event -> event.getPlayer().getLevel() > 50) // если хоть один игрок зашел с уровнем > 50, то ивент больше не работает

                // обработчик события
                .complete(event -> {

                    Player player = event.getPlayer();
                    event.setJoinMessage(String.format("Player %s has joined!", player.getName()));
                });
    }

    private void testBukkitRegistry() {
        BukkitRegistryManager bukkitRegistryManager = mineLibrary.getRegistryManager();

        bukkitRegistryManager.register(TestRegistryCommand.class);
        bukkitRegistryManager.register(TestRegistryListener.class);
    }

    private void testChatApi() {
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

    private void testLocationApi() {
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
            System.out.println("Its work!");
        }
    }

    private void testConfigs() {
        PluginConfigManager configManager = mineLibrary.getConfigManager();

        // Properties configs.
        PropertiesPluginConfig propertiesConfig = new TestPropertiesConfig(configManager, getDataFolder().toPath().resolve("config.properties").toFile())
                .init();

        propertiesConfig.set("minelib-author", "pxstudios");
        propertiesConfig.save();

        // Text configs.
        TextPluginConfig textConfig = new TestTextConfig(configManager, getDataFolder().toPath().resolve("motd.txt").toFile())
                .init();

        textConfig.append("&bHello world!").appendNewLine().append("This is &ccolorized &eMotd!").colorize('&');
        textConfig.save();

        // Yaml configs.
        YamlPluginConfig yamlConfig = new TestYamlConfig(configManager, getDataFolder().toPath().resolve("settings.yml").toFile())
                .init();

        yamlConfig.set("locations.cuboid.firstPosition", new Point3D(5, 5, 5));
        yamlConfig.set("locations.cuboid.secondPosition", new Point3D(10, 5, 10));
        yamlConfig.save();
    }

    @Override
    public void onEnable() {
        registerTestCommands();

        registerItemListener();

        testAsyncCatcherBypass();

        testBukkitBeater();

        testEventsSubscriber();

        testBukkitRegistry();

        testChatApi();

        testComplex();

        testLocationApi();

        testConfigs();
    }

}
