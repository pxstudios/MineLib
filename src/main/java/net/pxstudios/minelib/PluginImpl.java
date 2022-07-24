package net.pxstudios.minelib;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginImpl extends JavaPlugin {

    private final MineLibrary mineLibrary = MineLibrary.getLibrary();

    public void log(String message, Object... replacements) {
        super.getLogger().info(ChatColor.WHITE + String.format(message, replacements));
    }

    @Override
    public void onEnable() {
        long startEnableMillis = System.currentTimeMillis();

        // Log message of library enabling.
        log("Initializing library data`s & api`s...");

        // Init library data & api`s.
        mineLibrary.init(this);

        log("Running automatically memory cleanup task...");

        // Start automatically memory cleanup task.
        mineLibrary.runAutoGarbageCollector();

        log("Init custom server-motd api...");

        // Register automatically server-motd changing.
        mineLibrary.getServerMotdApi().initDefaults(getServer());

        log(ChatColor.GREEN + "MineLibrary was success enabled (%sms)", (System.currentTimeMillis() - startEnableMillis));
    }

}
