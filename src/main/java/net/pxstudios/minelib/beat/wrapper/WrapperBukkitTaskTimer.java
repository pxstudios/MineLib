package net.pxstudios.minelib.beat.wrapper;

import net.pxstudios.minelib.beat.BukkitBeater;
import org.bukkit.scheduler.BukkitTask;

public class WrapperBukkitTaskTimer extends WrapperBukkitTask {

    public WrapperBukkitTaskTimer(BukkitBeater beater, BukkitTask bukkitTask) {
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
