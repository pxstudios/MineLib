package net.pxstudios.minelib.plugin;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.plugin.MLMinecraftPluginDisableEvent;
import net.pxstudios.minelib.event.plugin.MLMinecraftPluginEnableEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public abstract class MinecraftPlugin extends JavaPlugin {

    private long onLoadTimeMillis;

    @Getter
    private MineLibrary mineLibrary;

    public final void log(String message, Object... placeholders) {
        getLogger().info(ChatColor.WHITE + String.format(message, placeholders));
    }

    public final void log(Level level, String message, Object... placeholders) {
        getLogger().log(level, ChatColor.WHITE + String.format(message, placeholders));
    }

    public final void log(Object message) {
        log(message.toString());
    }

    public final void log(Level level, Object message) {
        log(level, message.toString());
    }

    public void postLoad(MineLibrary mineLibrary) {
        // override me.
    }

    public void postDisable(MineLibrary mineLibrary) {
        // override me.
    }

    public abstract void postEnable(MineLibrary mineLibrary);

    @Override
    public final void onLoad() {
        mineLibrary = new MineLibrary();
        onLoadTimeMillis = System.currentTimeMillis();

        // Log message of library enabling.
        log("Initializing library data`s & api`s...");

        // Init library data & api`s.
        mineLibrary.init(this);

        postLoad(mineLibrary);
    }

    @Override
    public final void onEnable() {
        log("Running automatically memory cleanup task...");

        // Start automatically memory cleanup task.
        mineLibrary.runAutoGarbageCollector();

        log("Init custom server-motd api...");

        // Register automatically server-motd changing.
        mineLibrary.getServerMotdApi().initDefaults(getServer());

        log(ChatColor.GREEN + "MineLibrary was success enabled (%sms)", (System.currentTimeMillis() - onLoadTimeMillis));
        postEnable(mineLibrary);

        // Call event.
        mineLibrary.getEventsSubscriber().callEvent(new MLMinecraftPluginEnableEvent(mineLibrary, this));
    }

    @Override
    public final void onDisable() {
        postDisable(mineLibrary);

        // Call event.
        mineLibrary.getEventsSubscriber().callEvent(new MLMinecraftPluginDisableEvent(mineLibrary, this));

        mineLibrary = null;
    }

}
