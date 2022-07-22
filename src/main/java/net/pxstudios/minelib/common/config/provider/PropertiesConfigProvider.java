package net.pxstudios.minelib.common.config.provider;

import lombok.SneakyThrows;
import net.pxstudios.minelib.common.config.PluginConfigProvider;
import net.pxstudios.minelib.common.config.type.PropertiesPluginConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigProvider implements PluginConfigProvider<Properties> {

    private final Map<File, Properties> typesByFileMap = new HashMap<>();

    @Override
    public boolean validateFileFormat(File file) {
        return file != null && file.getName().endsWith(".properties");
    }

    @SneakyThrows
    @Override
    public Properties provide(File file) {
        Properties properties = typesByFileMap.get(file);

        if (properties == null) {
            properties = new Properties();

            try (FileInputStream inputStream = new FileInputStream(file)) {
                properties.load(inputStream);
            }

            typesByFileMap.put(file, properties);
        }

        return properties;
    }

    @Override
    public PropertiesPluginConfig createConfig(File file) {
        return new PropertiesPluginConfig(this, file);
    }

    @SneakyThrows
    @Override
    public void save(File file, Properties properties) {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, "");
        }
    }

    @Override
    public void cleanUp(File file) {
        typesByFileMap.remove(file);
    }

}
