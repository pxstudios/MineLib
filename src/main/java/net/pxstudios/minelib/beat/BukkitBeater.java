package net.pxstudios.minelib.beat;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitFuture;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTimerTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public final class BukkitBeater {

    private final Plugin plugin;

    private WrappedBukkitTimerTask wrapTimer(BukkitTask bukkitTask) {
        return new WrappedBukkitTimerTask(this, bukkitTask);
    }

    private WrappedBukkitTask wrap(BukkitTask bukkitTask) {
        return new WrappedBukkitTask(this, bukkitTask);
    }

    @SneakyThrows
    public void setupTask(BukkitRunnable runnableFor, BukkitTask taskTarget) {
        Field cachedTaskField = runnableFor.getClass().getDeclaredField("task");

        cachedTaskField.setAccessible(true);
        cachedTaskField.set(runnableFor, taskTarget);

        cachedTaskField.setAccessible(false);
    }

    public void cancel(int taskID) {
        plugin.getServer().getScheduler().cancelTask(taskID);
    }

    public void cancel(BukkitTask bukkitTask) {
        cancel(bukkitTask.getTaskId());
    }

    public void cancel(WrappedBukkitTask bukkitTask) {
        cancel(bukkitTask.getTaskId());
    }

    public void cancelAll() {
        plugin.getServer().getScheduler().cancelTasks(plugin);
    }

    public <T> WrappedBukkitFuture<T> callSync(Callable<T> callable) {
        return new WrappedBukkitFuture<T>(this, plugin.getServer().getScheduler().callSyncMethod(plugin, callable));
    }

    public WrappedBukkitTask runSync(Runnable command) {
        return wrap(plugin.getServer().getScheduler().runTask(plugin, command));
    }

    public WrappedBukkitTask runAsync(Runnable command) {
        return wrap(plugin.getServer().getScheduler().runTaskAsynchronously(plugin, command));
    }

    public WrappedBukkitTask runLater(long delay, Runnable command) {
        return wrap(plugin.getServer().getScheduler().runTaskLater(plugin, command, delay));
    }

    public WrappedBukkitTimerTask runTimer(long delay, long period, Runnable command) {
        return wrapTimer(plugin.getServer().getScheduler().runTaskTimer(plugin, command, delay, period));
    }

    public WrappedBukkitTimerTask runTimer(long period, Runnable command) {
        return runTimer(0, period, command);
    }

    public WrappedBukkitTask runLaterAsync(long delay, Runnable command) {
        return wrap(plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, command, delay));
    }

    public WrappedBukkitTimerTask runTimerAsync(long delay, long period, Runnable command) {
        return wrapTimer(plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, command, delay, period));
    }

    public WrappedBukkitTimerTask runTimerAsync(long period, Runnable command) {
        return runTimerAsync(0, period, command);
    }

    public List<BukkitTask> getPendingTasks() {
        return plugin.getServer().getScheduler().getPendingTasks();
    }

    public List<BukkitWorker> getActiveWorkers() {
        return plugin.getServer().getScheduler().getActiveWorkers();
    }
}
