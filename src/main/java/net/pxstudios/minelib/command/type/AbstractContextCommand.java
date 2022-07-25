package net.pxstudios.minelib.command.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.command.CommandContext;
import net.pxstudios.minelib.command.CommandSettings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class AbstractContextCommand {

    private final Set<String> activeLabels = new HashSet<>();

    private final Set<CommandSettings<?>> settings = new HashSet<>();

    public void addLabels(String... labels) {
        activeLabels.addAll(Arrays.stream(labels).map(String::toLowerCase).collect(Collectors.toSet()));
    }

    public void addSetting(CommandSettings<?> commandSetting) {
        settings.add(commandSetting);
    }

    public <R> void addSetting(CommandSettings<R> commandSetting, R value) {
        settings.add(commandSetting.set(value));
    }

    public <T> T getSettingValue(CommandSettings<T> commandSetting) {
        for (CommandSettings<?> commandSettings : getSettings()) {

            if (commandSettings.getName().equals(commandSetting.getName())) {
                return commandSetting.value();
            }
        }

        return null;
    }

    public abstract void process(CommandContext context);

}
