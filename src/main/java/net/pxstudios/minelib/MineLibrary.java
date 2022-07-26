package net.pxstudios.minelib;

import lombok.Getter;
import lombok.NonNull;
import net.pxstudios.minelib.asynccatcher.AsyncCatcherBypass;
import net.pxstudios.minelib.beat.BukkitBeater;
import net.pxstudios.minelib.beat.wrapper.WrapperBukkitTask;
import net.pxstudios.minelib.board.BoardApi;
import net.pxstudios.minelib.command.CommandRegistry;
import net.pxstudios.minelib.common.chat.ChatApi;
import net.pxstudios.minelib.common.config.PluginConfigManager;
import net.pxstudios.minelib.gui.GuiManager;
import net.pxstudios.minelib.common.item.BukkitItemApi;
import net.pxstudios.minelib.common.item.event.BukkitItemEventsHandler;
import net.pxstudios.minelib.common.location.BukkitLocationApi;
import net.pxstudios.minelib.cooldown.PlayerCooldownApi;
import net.pxstudios.minelib.event.bukkit.BukkitEventsWrapperListener;
import net.pxstudios.minelib.subscription.EventsSubscriber;
import net.pxstudios.minelib.motd.ServerMotdApi;
import net.pxstudios.minelib.permission.PlayerPermissionApi;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import net.pxstudios.minelib.registry.BukkitRegistryManager;
import net.pxstudios.minelib.world.BukkitWorldsApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public final class MineLibrary {

    @Getter
    private AsyncCatcherBypass asyncCatcherBypass;

    @Getter
    private CommandRegistry commandRegistry;

    @Getter
    private BukkitBeater beater;

    @Getter
    private BoardApi boardApi;

    @Getter
    private ChatApi chatApi;

    @Getter
    private PluginConfigManager configManager;

    @Getter
    private GuiManager guiManager;

    @Getter
    private PlayerCooldownApi playerCooldownApi;

    @Getter
    private EventsSubscriber eventsSubscriber;

    @Getter
    private BukkitItemApi itemApi;

    @Getter
    private BukkitLocationApi locationApi;

    @Getter
    private ServerMotdApi serverMotdApi;

    @Getter
    private PlayerPermissionApi permissionApi;

    @Getter
    private BukkitWorldsApi worldsApi;

    @Getter
    private BukkitRegistryManager registryManager;

    private WrapperBukkitTask autoGarbageCollectorTask;

    public void init(@NonNull MinecraftPlugin plugin) {

        // Init library sub-systems by plugin.
        asyncCatcherBypass = new AsyncCatcherBypass(plugin);
        commandRegistry = new CommandRegistry(plugin);
        beater = new BukkitBeater(plugin);
        eventsSubscriber = new EventsSubscriber(plugin);
        registryManager = new BukkitRegistryManager(plugin);
        boardApi = new BoardApi(plugin);
        worldsApi = new BukkitWorldsApi(plugin);
        playerCooldownApi = new PlayerCooldownApi(plugin);
        guiManager = new GuiManager(plugin);
        serverMotdApi = new ServerMotdApi(plugin);
        permissionApi = new PlayerPermissionApi(plugin);

        // Init library common sub-systems.
        chatApi = new ChatApi();
        configManager = new PluginConfigManager();
        itemApi = new BukkitItemApi();
        locationApi = new BukkitLocationApi();

        // Register default bukkit-objects registry providers.
        registryManager.addDefaultProviders();

        // Register default plugin-configs providers.
        configManager.addDefaultProviders();

        // Acceptation a plugin-manager.
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        // Register BukkitItem`s internal events-storage listener.
        pluginManager.registerEvents(new BukkitItemEventsHandler(), plugin);

        // Register bukkit-events wrappers listener.
        pluginManager.registerEvents(new BukkitEventsWrapperListener(this), plugin);

        // Register gui actions listener.
        pluginManager.registerEvents(guiManager.getListener(), plugin);
    }

    public void runAutoGarbageCollector() {
        autoGarbageCollectorTask = beater.runTimerAsync(20L * 15, System::gc);
        autoGarbageCollectorTask.waitAfter(() -> Bukkit.getLogger().info("Auto garbage-collector is disabled!"));
    }

    public void destroyAutoGarbageCollector() {
        if (!autoGarbageCollectorTask.isCancelled()) {
            autoGarbageCollectorTask.cancel();
        }
    }

}
