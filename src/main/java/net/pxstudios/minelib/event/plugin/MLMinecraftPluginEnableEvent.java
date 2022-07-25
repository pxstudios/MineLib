package net.pxstudios.minelib.event.plugin;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.plugin.MinecraftPlugin;

public class MLMinecraftPluginEnableEvent extends MLMinecraftPluginEvent {

    public MLMinecraftPluginEnableEvent(MineLibrary mineLibrary, MinecraftPlugin plugin) {
        super(mineLibrary, plugin);
    }
}
