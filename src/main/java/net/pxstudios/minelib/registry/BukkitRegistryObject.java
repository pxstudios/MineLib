package net.pxstudios.minelib.registry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import net.pxstudios.minelib.registry.provider.BukkitRegistryObjectProvider;

import java.util.function.Function;

@AllArgsConstructor
public class BukkitRegistryObject<T> {

    @Setter(AccessLevel.PACKAGE)
    private Function<BukkitRegistryManager, BukkitRegistryObjectProvider<T>> providerFunction;

    private T newInstance(BukkitRegistryManager manager, MinecraftPlugin plugin) {
        BukkitRegistryObjectProvider<T> provider = providerFunction.apply(manager);

        if (provider != null) {
            return provider.newObjectInstance(plugin, this);
        }

        return null;
    }

    void register(BukkitRegistryManager manager, MinecraftPlugin plugin) {
        BukkitRegistryObjectProvider<T> provider = providerFunction.apply(manager);

        if (provider != null) {
            provider.fireRegister(plugin, newInstance(manager, plugin));
        }
    }

}
