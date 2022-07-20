package net.pxstudtios.minelib.test;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudios.minelib.beat.BukkitBeater;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTimerTask;
import net.pxstudios.minelib.common.chat.ChatApi;
import net.pxstudios.minelib.common.chat.ChatDirection;
import net.pxstudios.minelib.common.item.BukkitItemFactory;
import net.pxstudios.minelib.registry.BukkitRegistryManager;
import net.pxstudtios.minelib.test.command.TestAbstractBukkitCommand;
import net.pxstudtios.minelib.test.command.TestAbstractContextCommand;
import net.pxstudtios.minelib.test.command.TestAbstractPlayerBukkitCommand;
import net.pxstudtios.minelib.test.complex.TestComplexBlockListener;
import net.pxstudtios.minelib.test.item.TestBukkitItemListener;
import net.pxstudtios.minelib.test.registry.TestRegistryCommand;
import net.pxstudtios.minelib.test.registry.TestRegistryListener;
import org.bukkit.Bukkit;
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
    }

}
