package net.pxstudios.minelib.beat;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTimerTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
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

    public WrappedBukkitTask runSync(Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTask(plugin, runnable));
    }

    public WrappedBukkitTask runAsync(Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    public WrappedBukkitTask runLater(long delay, Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay));
    }

    public WrappedBukkitTask runLaterAsync(long delay, Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    public WrappedBukkitTimerTask runTimer(long delay, long period, Runnable runnable) {
        return wrapTimer(plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period));
    }

    public WrappedBukkitTimerTask runTimer(long period, Runnable runnable) {
        return runTimer(0, period, runnable);
    }

    public WrappedBukkitTimerTask runTimerAsync(long delay, long period, Runnable runnable) {
        return wrapTimer(plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

    public WrappedBukkitTimerTask runTimerAsync(long period, Runnable runnable) {
        return runTimerAsync(0, period, runnable);
    }

    public WrappedBukkitTimerTask runCancellableTimer(long delay, long period, BukkitRunnable cancellable) {
        WrappedBukkitTimerTask task = runTimer(delay, period, cancellable);
        task.setup(cancellable);

        return task;
    }

    public WrappedBukkitTimerTask runCancellableTimer(long period, BukkitRunnable cancellable) {
        WrappedBukkitTimerTask task = runTimer(period, cancellable);
        task.setup(cancellable);

        return task;
    }

    public WrappedBukkitTimerTask runCancellableTimerAsync(long delay, long period, BukkitRunnable cancellable) {
        WrappedBukkitTimerTask task = runTimerAsync(delay, period, cancellable);
        task.setup(cancellable);

        return task;
    }

    public WrappedBukkitTimerTask runCancellableTimerAsync(long period, BukkitRunnable cancellable) {
        WrappedBukkitTimerTask task = runTimerAsync(period, cancellable);
        task.setup(cancellable);

        return task;
    }

    public List<BukkitTask> getPendingTasks() {
        return plugin.getServer().getScheduler().getPendingTasks();
    }

    public List<BukkitWorker> getActiveWorkers() {
        return plugin.getServer().getScheduler().getActiveWorkers();
    }
}
