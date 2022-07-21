package net.pxstudios.minelib.common.config;

import net.pxstudios.minelib.common.config.provider.PropertiesConfigProvider;
import net.pxstudios.minelib.common.config.provider.TextConfigProvider;
import net.pxstudios.minelib.common.config.provider.YamlConfigProvider;
import net.pxstudios.minelib.common.config.type.PropertiesPluginConfig;
import net.pxstudios.minelib.common.config.type.TextPluginConfig;
import net.pxstudios.minelib.common.config.type.YamlPluginConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class PluginConfigManager {

    private final Map<Class<?>, PluginConfigProvider<?>> providersByTypeMap = new HashMap<>();

    public void addProvider(PluginConfigProvider<?> provider) {
        providersByTypeMap.put(provider.getClass(), provider);
    }

    public void addDefaultProviders() {
        addProvider(new PropertiesConfigProvider());
        addProvider(new TextConfigProvider());
        addProvider(new YamlConfigProvider());
    }

    @SuppressWarnings("unchecked")
    public <Type> PluginConfigProvider<Type> getProvider(Class<? extends PluginConfigProvider<Type>> cls) {
        return (PluginConfigProvider<Type>) providersByTypeMap.get(cls);
    }

    public <Type> PluginConfig<Type> createConfigObject(Class<? extends PluginConfigProvider<Type>> cls, File file) {
        PluginConfigProvider<Type> provider = getProvider(cls);

        if (provider != null) {
            return provider.createConfig(file);
        }

        return null;
    }

    public PropertiesPluginConfig createPropertiesConfig(File file) {
        return (PropertiesPluginConfig) createConfigObject(PropertiesConfigProvider.class, file);
    }

    public TextPluginConfig createTextConfig(File file) {
        return (TextPluginConfig) createConfigObject(TextConfigProvider.class, file);
    }

    public YamlPluginConfig createYamlConfig(File file) {
        return (YamlPluginConfig) createConfigObject(YamlConfigProvider.class, file);
    }
}
