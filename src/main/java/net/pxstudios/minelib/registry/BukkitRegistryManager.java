package net.pxstudios.minelib.registry;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import net.pxstudios.minelib.registry.adapter.BukkitRegistryObjectAdapter;
import net.pxstudios.minelib.registry.adapter.type.CommandRegistryObjectAdapter;
import net.pxstudios.minelib.registry.adapter.type.EventListenerRegistryObjectAdapter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class BukkitRegistryManager {

    private final Plugin plugin;
    private final Map<Class<?>, BukkitRegistryObjectAdapter<?>> adaptersByTypeMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> BukkitRegistryObjectAdapter<T> getAdapter(Class<T> cls) {
        return (BukkitRegistryObjectAdapter<T>) adaptersByTypeMap.get(cls);
    }

    public <T> void addAdapter(Class<T> cls, BukkitRegistryObjectAdapter<T> adapter) {
        adaptersByTypeMap.put(cls, adapter);
    }

    public void register(Class<? extends BukkitRegistryObject<?>> cls) {
        BukkitRegistryObject<?> bukkitRegistryObject = null;
        try {
            bukkitRegistryObject = cls.newInstance();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        if (bukkitRegistryObject != null) {
            bukkitRegistryObject.register(this, plugin);
        }
    }

    public void addDefaultAdapters() {
        addAdapter(AbstractContextCommand.class, new CommandRegistryObjectAdapter());
        addAdapter(Listener.class, new EventListenerRegistryObjectAdapter());
    }

}
