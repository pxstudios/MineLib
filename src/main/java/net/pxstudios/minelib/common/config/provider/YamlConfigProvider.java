package net.pxstudios.minelib.common.config.provider;

import lombok.SneakyThrows;
import net.pxstudios.minelib.common.config.PluginConfigProvider;
import net.pxstudios.minelib.common.config.type.YamlPluginConfig;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class YamlConfigProvider implements PluginConfigProvider<YamlConfiguration> {
    
    private final Map<File, YamlConfiguration> typesByFileMap = new HashMap<>();

    @Override
    public boolean validateFileFormat(File file) {
        return file.getName().endsWith(".yml") || file.getName().endsWith(".yaml");
    }

    @Override
    public YamlConfiguration provide(File file) {
        YamlConfiguration configuration = typesByFileMap.get(file);
        
        if (configuration == null) {

            configuration = YamlConfiguration.loadConfiguration(file);
            typesByFileMap.put(file, configuration);
        }
        
        return configuration;
    }

    @Override
    public YamlPluginConfig createConfig(File file) {
        return new YamlPluginConfig(this, file);
    }

    @SneakyThrows
    @Override
    public void save(File file, YamlConfiguration configuration) {
        configuration.save(file);
    }

    @Override
    public void cleanUp(File file) {
        typesByFileMap.remove(file);
    }
    
}
