package net.pxstudtios.minelib.test.config;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.config.PluginConfigManager;
import net.pxstudios.minelib.common.config.provider.TextConfigProvider;
import net.pxstudios.minelib.common.config.type.TextPluginConfig;

import java.io.File;

@RequiredArgsConstructor
public class TestTextConfig {

    private final PluginConfigManager configManager;
    private final File file;

    private TextPluginConfig config;

    @SuppressWarnings("ConstantConditions")
    public TextPluginConfig init() {
        if (config == null) {

            config = (TextPluginConfig) configManager.createConfigObject(TextConfigProvider.class, file);
            config.createFile();
        }

        return config;
    }

}
