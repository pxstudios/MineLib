package net.pxstudios.minelib.beat;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.pxstudios.minelib.beat.wrapper.WrapperBukkitTask;
import net.pxstudios.minelib.beat.wrapper.WrapperBukkitTaskTimer;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import java.lang.reflect.Field;
import java.util.List;

@RequiredArgsConstructor
public final class BukkitBeater {

    private final MinecraftPlugin plugin;

    private WrapperBukkitTaskTimer wrapTimer(BukkitTask bukkitTask) {
        return new WrapperBukkitTaskTimer(this, bukkitTask);
    }

    private WrapperBukkitTask wrap(BukkitTask bukkitTask) {
        return new WrapperBukkitTask(this, bukkitTask);
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

    public void cancel(WrapperBukkitTask bukkitTask) {
        cancel(bukkitTask.getTaskId());
    }

    public void cancelAll() {
        plugin.getServer().getScheduler().cancelTasks(plugin);
    }

    public WrapperBukkitTask runSync(Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTask(plugin, runnable));
    }

    public WrapperBukkitTask runAsync(Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    public WrapperBukkitTask runLater(long delay, Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay));
    }

    public WrapperBukkitTask runLaterAsync(long delay, Runnable runnable) {
        return wrap(plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    public WrapperBukkitTaskTimer runTimer(long delay, long period, Runnable runnable) {
        return wrapTimer(plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period));
    }

    public WrapperBukkitTaskTimer runTimer(long period, Runnable runnable) {
        return runTimer(0, period, runnable);
    }

    public WrapperBukkitTaskTimer runTimerAsync(long delay, long period, Runnable runnable) {
        return wrapTimer(plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

    public WrapperBukkitTaskTimer runTimerAsync(long period, Runnable runnable) {
        return runTimerAsync(0, period, runnable);
    }

    public WrapperBukkitTaskTimer runCancellableTimer(long delay, long period, BukkitRunnable cancellable) {
        WrapperBukkitTaskTimer task = runTimer(delay, period, cancellable);
        task.setup(cancellable);

        return task;
    }

    public WrapperBukkitTaskTimer runCancellableTimer(long period, BukkitRunnable cancellable) {
        WrapperBukkitTaskTimer task = runTimer(period, cancellable);
        task.setup(cancellable);

        return task;
    }

    public WrapperBukkitTaskTimer runCancellableTimerAsync(long delay, long period, BukkitRunnable cancellable) {
        WrapperBukkitTaskTimer task = runTimerAsync(delay, period, cancellable);
        task.setup(cancellable);

        return task;
    }

    public WrapperBukkitTaskTimer runCancellableTimerAsync(long period, BukkitRunnable cancellable) {
        WrapperBukkitTaskTimer task = runTimerAsync(period, cancellable);
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
