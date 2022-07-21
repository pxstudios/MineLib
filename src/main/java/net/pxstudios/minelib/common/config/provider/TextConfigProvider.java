package net.pxstudios.minelib.common.config.provider;

import lombok.SneakyThrows;
import net.pxstudios.minelib.common.config.PluginConfigProvider;
import net.pxstudios.minelib.common.config.type.TextPluginConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TextConfigProvider implements PluginConfigProvider<String> {
    
    private final Map<File, String> typesByFileMap = new HashMap<>();
    
    @SneakyThrows
    @Override
    public String provide(File file) {
        String text = typesByFileMap.get(file);
        
        if (text == null) {

            text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            typesByFileMap.put(file, text);
        }
        
        return text;
    }

    @Override
    public TextPluginConfig createConfig(File file) {
        return new TextPluginConfig(this, file);
    }

    @SneakyThrows
    @Override
    public void save(File file, String text) {
        FileUtils.writeStringToFile(file, text, StandardCharsets.UTF_8);
    }

    @Override
    public void cleanUp(File file) {
        typesByFileMap.remove(file);
    }
    
}
