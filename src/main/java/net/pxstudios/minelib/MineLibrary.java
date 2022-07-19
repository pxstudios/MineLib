package net.pxstudios.minelib;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudios.minelib.beat.BukkitBeater;
import net.pxstudios.minelib.beat.wrap.WrappedBukkitTask;
import net.pxstudios.minelib.command.CommandRegistry;
import net.pxstudios.minelib.event.EventsSubscriber;
import net.pxstudios.minelib.item.BukkitItemFactory;
import net.pxstudios.minelib.registry.BukkitRegistryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MineLibrary {

    private static final MineLibrary instance = new MineLibrary();

    public static MineLibrary getLibrary() {
        return instance;
    }

    @Getter
    private AsyncCatcherBypass asyncCatcherBypass;

    @Getter
    private CommandRegistry commandRegistry;

    @Getter
    private BukkitBeater beater;

    @Getter
    private EventsSubscriber eventsSubscriber;

    @Getter
    private BukkitItemFactory itemFactory;

    @Getter
    private BukkitRegistryManager registryManager;

    private WrappedBukkitTask autoGarbageCollectorTask;

    void init(@NonNull Plugin plugin) {
        asyncCatcherBypass = new AsyncCatcherBypass(plugin);
        commandRegistry = new CommandRegistry(plugin);
        beater = new BukkitBeater(plugin);
        eventsSubscriber = new EventsSubscriber(plugin);
        registryManager = new BukkitRegistryManager(plugin);

        itemFactory = new BukkitItemFactory();

        runAutoGarbageCollector();

        // ...
    }

    private void runAutoGarbageCollector() {
        autoGarbageCollectorTask = beater.runTimerAsync(20L * 5, System::gc);
        autoGarbageCollectorTask.waitAfter(() -> Bukkit.getLogger().info("Auto garbage-collector is disabled!"));
    }

    public void destroyAutoGarbageCollector() {
        if (!autoGarbageCollectorTask.isCancelled()) {
            autoGarbageCollectorTask.cancel();
        }
    }

}
