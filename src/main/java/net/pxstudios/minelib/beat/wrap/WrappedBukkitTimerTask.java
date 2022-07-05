package net.pxstudios.minelib.beat.wrap;

import net.pxstudios.minelib.beat.BukkitBeater;
import org.bukkit.scheduler.BukkitTask;

public class WrappedBukkitTimerTask extends WrappedBukkitTask {

    public WrappedBukkitTimerTask(BukkitBeater beater, BukkitTask bukkitTask) {
        super(beater, bukkitTask);
    }

    public void waitFor(long cancelDelay, Runnable after) {
        beater.runLater(cancelDelay, () -> {

            handle.cancel();

            if (after != null) {
                after.run();
            }
        });
    }

    public void waitFor(long cancelDelay) {
        waitFor(cancelDelay, null);
    }

}
