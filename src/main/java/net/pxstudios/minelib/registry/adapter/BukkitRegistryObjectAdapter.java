package net.pxstudios.minelib.registry.adapter;

import net.pxstudios.minelib.registry.BukkitRegistryObject;
import org.bukkit.plugin.Plugin;

public abstract class BukkitRegistryObjectAdapter<T> {

    public abstract void fireRegister(Plugin plugin, T obj);

    public abstract T newObjectInstance(BukkitRegistryObject<T> bukkitRegistryObject);
}
