package net.pxstudios.minelib.common.config.type;

import lombok.experimental.Delegate;
import net.pxstudios.minelib.common.config.PluginConfig;
import net.pxstudios.minelib.common.config.provider.YamlConfigProvider;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlPluginConfig extends PluginConfig<YamlConfiguration> {

    @Delegate
    private final YamlConfiguration configuration;

    public YamlPluginConfig(YamlConfigProvider provider, File file) {
        super(provider, file);
        this.configuration = data();
    }

}
