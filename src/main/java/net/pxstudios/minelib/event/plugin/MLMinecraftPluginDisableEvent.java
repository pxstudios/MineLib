package net.pxstudios.minelib.event.plugin;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.plugin.MinecraftPlugin;

public class MLMinecraftPluginDisableEvent extends MLMinecraftPluginEvent {

    public MLMinecraftPluginDisableEvent(MineLibrary mineLibrary, MinecraftPlugin plugin) {
        super(mineLibrary, plugin);
    }
}
