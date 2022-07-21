package net.pxstudios.minelib.registry.provider;

import net.pxstudios.minelib.registry.BukkitRegistryObject;
import org.bukkit.plugin.Plugin;

public interface BukkitRegistryObjectProvider<T> {

    void fireRegister(Plugin plugin, T obj);

    T newObjectInstance(Plugin plugin, BukkitRegistryObject<T> bukkitRegistryObject);
}
