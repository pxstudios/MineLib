package net.pxstudios.minelib.registry.adapter.type;

import net.pxstudios.minelib.command.type.AbstractContextCommand;
import net.pxstudios.minelib.registry.BukkitRegistryObject;
import net.pxstudios.minelib.registry.adapter.BukkitRegistryObjectAdapter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EventListenerRegistryObjectAdapter extends BukkitRegistryObjectAdapter<Listener> {

    @Override
    public void fireRegister(Plugin plugin, Listener obj) {
        plugin.getServer().getPluginManager().registerEvents(obj, plugin);
    }

    @Override
    public Listener newObjectInstance(BukkitRegistryObject<Listener> bukkitRegistryObject) {
        return null;
    }
}
