package net.pxstudios.minelib.registry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import net.pxstudios.minelib.registry.adapter.BukkitRegistryObjectAdapter;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

@AllArgsConstructor
public class BukkitRegistryObject<T> {

    @Setter(AccessLevel.PACKAGE)
    private Function<BukkitRegistryManager, BukkitRegistryObjectAdapter<T>> adapterFunction;

    private T newInstance() {
        return null; // TODO: 19.07.2022
    }

    void register(BukkitRegistryManager manager, Plugin plugin) {
        BukkitRegistryObjectAdapter<T> adapter = adapterFunction.apply(manager);

        if (adapter != null) {
            adapter.fireRegister(plugin, newInstance());
        }
    }

}
