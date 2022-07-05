package net.pxstudtios.minelib.test;

import lombok.SneakyThrows;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudios.minelib.beat.BukkitBeater;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTimerTask;
import net.pxstudtios.minelib.test.command.TestAbstractBukkitCommand;
import net.pxstudtios.minelib.test.command.TestAbstractContextCommand;
import net.pxstudtios.minelib.test.command.TestAbstractPlayerBukkitCommand;
import net.pxstudtios.minelib.test.item.TestBukkitItemListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public final class MineLibTest extends JavaPlugin {

    private final MineLibrary mineLibrary = MineLibrary.getLibrary();

    private void registerTestCommands() {
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractBukkitCommand(this), "bukkittest", "btest");
        // -> that command is not contains internal labels.

        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractPlayerBukkitCommand(this));
        mineLibrary.getCommandRegistry().registerCommand(new TestAbstractContextCommand(this));
    }

    private void registerItemListener() {
        getServer().getPluginManager().registerEvents(new TestBukkitItemListener(), this);
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

    @Override
    public void onEnable() {
        registerTestCommands();

        registerItemListener();

        testAsyncCatcherBypass();

        testBukkitBeater();
    }

}
