package net.pxstudios.minelib.common.config;

import net.pxstudios.minelib.common.config.PluginConfig;

import java.io.File;
import java.nio.file.Path;

public interface PluginConfigProvider<Type> {

    default Type provide(Path path) {
        return provide(path.toFile());
    }

    Type provide(File file);

    default PluginConfig<Type> createConfig(Path path) {
        return createConfig(path.toFile());
    }

    PluginConfig<Type> createConfig(File file);

    default void save(Path path, Type type) {
        save(path.toFile(), type);
    }

    void save(File file, Type type);

    default void cleanUp(Path path) {
        cleanUp(path.toFile());
    }

    void cleanUp(File file);
}
