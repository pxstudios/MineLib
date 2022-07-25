package net.pxstudios.minelib.registry;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import net.pxstudios.minelib.registry.provider.BukkitRegistryObjectProvider;
import net.pxstudios.minelib.registry.provider.type.CommandRegistryObjectProvider;
import net.pxstudios.minelib.registry.provider.type.EventListenerRegistryObjectProvider;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class BukkitRegistryManager {

    private final MinecraftPlugin plugin;
    private final Map<Class<?>, BukkitRegistryObjectProvider<?>> providersByTypeMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> BukkitRegistryObjectProvider<T> getProvider(Class<T> cls) {
        return (BukkitRegistryObjectProvider<T>) providersByTypeMap.get(cls);
    }

    public <T> void addProvider(Class<T> cls, BukkitRegistryObjectProvider<T> provider) {
        providersByTypeMap.put(cls, provider);
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

    public void addDefaultProviders() {
        addProvider(AbstractContextCommand.class, new CommandRegistryObjectProvider());
        addProvider(Listener.class, new EventListenerRegistryObjectProvider());
    }

}
