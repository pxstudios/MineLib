package net.pxstudios.minelib;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginInitializer extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.AQUA + "PluginInitializer has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "PluginInitializer has been disabled!");
    }

}
