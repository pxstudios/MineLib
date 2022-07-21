package net.pxstudtios.minelib.test.config;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.config.PluginConfigManager;
import net.pxstudios.minelib.common.config.provider.PropertiesConfigProvider;
import net.pxstudios.minelib.common.config.type.PropertiesPluginConfig;

import java.io.File;

@RequiredArgsConstructor
public class TestPropertiesConfig {

    private final PluginConfigManager configManager;
    private final File file;

    private PropertiesPluginConfig config;

    @SuppressWarnings("ConstantConditions")
    public PropertiesPluginConfig init() {
        if (config == null) {

            config = (PropertiesPluginConfig) configManager.createConfigObject(PropertiesConfigProvider.class, file);
            config.createFile();
        }

        return config;
    }

}
