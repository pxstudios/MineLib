package net.pxstudios.minelib.event.plugin;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.MLEvent;
import net.pxstudios.minelib.plugin.MinecraftPlugin;

@Getter
public abstract class MLMinecraftPluginEvent extends MLEvent {

    private final MinecraftPlugin plugin;

    public MLMinecraftPluginEvent(MineLibrary mineLibrary, MinecraftPlugin plugin) {
        super(mineLibrary);

        this.plugin = plugin;
    }
}
