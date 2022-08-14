package net.pxstudios.minelib.asynccatcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.spigotmc.AsyncCatcher;

@RequiredArgsConstructor
public final class AsyncCatcherBypass {
    private final MinecraftPlugin plugin;

    @Getter
    private boolean bypassEnabled = false;

    public void enableSpigotBypass() {
        // AsyncCatcher.enabled = false;
        bypassEnabled = true;
    }

    public synchronized void sync(Runnable synchronizer) {
        if (!bypassEnabled) {
            throw new IllegalArgumentException("async-catcher bypass is not enabled");
        }

        plugin.getMineLibrary().getBeater().runSync(synchronizer);
    }

}
