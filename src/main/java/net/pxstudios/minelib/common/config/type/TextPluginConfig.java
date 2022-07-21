package net.pxstudios.minelib.common.config.type;

import net.pxstudios.minelib.common.config.PluginConfig;
import net.pxstudios.minelib.common.config.provider.TextConfigProvider;
import org.bukkit.ChatColor;

import java.io.File;

public class TextPluginConfig extends PluginConfig<String> {

    public TextPluginConfig(TextConfigProvider provider, File file) {
        super(provider, file);
    }

    public String get() {
        return data();
    }

    public TextPluginConfig append(String text) {
        set(get() + text);
        return this;
    }

    public TextPluginConfig appendNewLine() {
        return append("\n");
    }

    public TextPluginConfig substring(int beginIndex, int endIndex) {
        set(get().substring(beginIndex, endIndex));
        return this;
    }

    public TextPluginConfig substring(int beginIndex) {
        return substring(beginIndex, 0);
    }

    public TextPluginConfig colorize(char altColorChar) {
        set(ChatColor.translateAlternateColorCodes(altColorChar, get()));
        return this;
    }

    public void clean() {
        set("");
    }
}
