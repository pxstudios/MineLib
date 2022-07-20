package net.pxstudios.minelib.registry.adapter;

import net.pxstudios.minelib.registry.BukkitRegistryObject;
import org.bukkit.plugin.Plugin;

public interface BukkitRegistryObjectAdapter<T> {

    void fireRegister(Plugin plugin, T obj);

    T newObjectInstance(Plugin plugin, BukkitRegistryObject<T> bukkitRegistryObject);
}
