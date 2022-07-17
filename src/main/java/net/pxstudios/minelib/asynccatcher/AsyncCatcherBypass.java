package net.pxstudios.minelib.asynccatcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.spigotmc.AsyncCatcher;

@RequiredArgsConstructor
public final class AsyncCatcherBypass {
    private final Plugin plugin;

    @Getter
    private boolean bypassEnabled = false;

    public void enableSpigotBypass() {
        AsyncCatcher.enabled = false;
        bypassEnabled = true;
    }

    public void sync(Runnable synchronizer) {
        if (!bypassEnabled) {
            throw new IllegalArgumentException("async-catcher bypass is not enabled");
        }

        synchronized (plugin) {
            plugin.getServer().getScheduler().runTask(plugin, synchronizer);
        }
    }

}
