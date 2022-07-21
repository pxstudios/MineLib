package net.pxstudios.minelib.common.config.type;

import net.pxstudios.minelib.common.config.PluginConfig;
import net.pxstudios.minelib.common.config.provider.PropertiesConfigProvider;

import java.io.File;
import java.util.Properties;

public class PropertiesPluginConfig extends PluginConfig<Properties> {

    public PropertiesPluginConfig(PropertiesConfigProvider provider, File file) {
        super(provider, file);
    }

    public void remove(String key) {
        data().remove(key);
    }

    public void set(String key, Object value) {
        if (value == null) {
            remove(key);
        }
        else {
            data().put(key, value);
        }
    }

    public Object get(String key) {
        return data().get(key);
    }

}
