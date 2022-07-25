package net.pxstudios.minelib.registry.provider;

import net.pxstudios.minelib.plugin.MinecraftPlugin;
import net.pxstudios.minelib.registry.BukkitRegistryObject;

public interface BukkitRegistryObjectProvider<T> {

    void fireRegister(MinecraftPlugin plugin, T obj);

    T newObjectInstance(MinecraftPlugin plugin, BukkitRegistryObject<T> bukkitRegistryObject);
}
