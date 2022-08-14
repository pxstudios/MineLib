package net.pxstudios.minelib.fastutil;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.fastutil.blockplace.FastBlockPlaceSession;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.World;

@RequiredArgsConstructor
public final class FastUtils {

    private final MinecraftPlugin plugin;

    public FastBlockPlaceSession createBlockPlaceSession(@NonNull World world) {
        return new FastBlockPlaceSession(world);
    }
}
