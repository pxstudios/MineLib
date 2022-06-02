package net.pxstudios.minelib;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginImpl extends JavaPlugin {
    private final MineLibrary mineLibrary = MineLibrary.getLibrary();

    @Override
    public void onEnable() {
        mineLibrary.init(this);
    }

    @Override
    public void onDisable() {
        // ...
    }

}
