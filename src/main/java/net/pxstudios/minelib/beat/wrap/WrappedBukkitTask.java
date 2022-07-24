package net.pxstudios.minelib.beat.wrap;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.pxstudios.minelib.beat.BukkitBeater;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class WrappedBukkitTask {

    protected final BukkitBeater beater;

    @Delegate
    protected final BukkitTask handle;

    public void waitAfter(Runnable afterCommand) {
        beater.runCancellableTimer(1, new BukkitRunnable() {

            @Override
            public void run() {

                if (handle.isCancelled()) {

                    afterCommand.run();
                    super.cancel();
                }
            }
        });
    }

    public void setup(BukkitRunnable bukkitRunnable) {
        beater.setupTask(bukkitRunnable, handle);
    }

}
