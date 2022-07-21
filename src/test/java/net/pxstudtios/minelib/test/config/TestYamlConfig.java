package net.pxstudtios.minelib.test.config;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.config.PluginConfigManager;
import net.pxstudios.minelib.common.config.provider.YamlConfigProvider;
import net.pxstudios.minelib.common.config.type.YamlPluginConfig;

import java.io.File;

@RequiredArgsConstructor
public class TestYamlConfig {

    private final PluginConfigManager configManager;
    private final File file;

    private YamlPluginConfig config;

    @SuppressWarnings("ConstantConditions")
    public YamlPluginConfig init() {
        if (config == null) {

            config = (YamlPluginConfig) configManager.createConfigObject(YamlConfigProvider.class, file);
            config.createFile();
        }

        return config;
    }

}
