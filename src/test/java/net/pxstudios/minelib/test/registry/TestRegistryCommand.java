package net.pxstudios.minelib.test.registry;

import net.pxstudios.minelib.command.CommandContext;
import net.pxstudios.minelib.command.CommandSettings;
import net.pxstudios.minelib.registry.type.CommandRegistryObject;

import java.util.HashSet;
import java.util.Set;

@CommandRegistryObject.Alias("test")
@CommandRegistryObject.Alias("testcmd")
@CommandRegistryObject.Alias("mlibtest")
public class TestRegistryCommand extends CommandRegistryObject {

    @SettingsMethod
    public Set<CommandSettings<?>> settings() {
        Set<CommandSettings<?>> commandSettings = new HashSet<>();

        commandSettings.add(CommandSettings.NO_PERMISSION_MESSAGE);
        commandSettings.add(CommandSettings.USE_PERMISSION.set("minelib.testcmd"));

        return commandSettings;
    }

    public void process(CommandContext context) {
        context.sender().sendMessage("Success");
    }

}
