package net.pxstudios.minelib.common.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.nio.file.Files;

@RequiredArgsConstructor
public abstract class PluginConfig<Type> {

    private final PluginConfigProvider<Type> provider;
    private final File file;

    private Type data;

    public final Type data() {
        return data != null ? data : (data = provider.provide(file));
    }

    public final void set(Type value) {
        this.data = value;
    }

    public void reload() {
        provider.cleanUp(file);
        data = null;
    }

    public void save() {
        provider.save(file, data);
    }

    public void copyResource(Plugin plugin, String resourceName) {
        plugin.saveResource(resourceName, false);
    }

    @SneakyThrows
    public void createNewResource() {
        if (!isExists()) {
            Files.createFile(file.toPath());
        }
    }

    public boolean isExists() {
        return file.exists();
    }

}
