package net.pxstudios.minelib.registry;

import java.lang.reflect.ParameterizedType;

public class BukkitRegistryAdaptiveObject<T> extends BukkitRegistryObject<T> {

    public BukkitRegistryAdaptiveObject() {
        super(null);
        super.setAdapterFunction(manager -> manager.getAdapter(getAdapterType()));
    }

    @SuppressWarnings("unchecked")
    protected final Class<T> getAdapterType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
