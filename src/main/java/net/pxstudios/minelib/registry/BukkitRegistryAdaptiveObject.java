package net.pxstudios.minelib.registry;

import java.lang.reflect.ParameterizedType;

public class BukkitRegistryAdaptiveObject<T> extends BukkitRegistryObject<T> {

    public BukkitRegistryAdaptiveObject() {
        super(null);
        super.setProviderFunction(manager -> manager.getProvider(getProviderType()));
    }

    @SuppressWarnings("unchecked")
    protected final Class<T> getProviderType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
