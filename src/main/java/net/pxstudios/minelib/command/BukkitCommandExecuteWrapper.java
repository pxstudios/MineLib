package net.pxstudios.minelib.command;

import lombok.AccessLevel;
import lombok.Getter;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.Set;

public class BukkitCommandExecuteWrapper extends Command implements CommandExecutor {

    private static final String DEF_DESCRIPTION = "Command created & registered by MineLib";

    @Getter(AccessLevel.PACKAGE)
    private final AbstractContextCommand handle;

    protected BukkitCommandExecuteWrapper(AbstractContextCommand handle, String name, String[] aliases) {
        super(name, DEF_DESCRIPTION, "", Arrays.asList(aliases));

        this.handle = handle;
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        if (applySettings(commandSender, args)) {
            handle.process(new CommandContext(handle.getSettings(), commandSender, args, label));
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return execute(commandSender, label, args);
    }

    private boolean applySettings(CommandSender commandSender, String[] args) {
        Set<CommandSettings<?>> settings = handle.getSettings();
        boolean resultFlag = true;

        // Check sender class type
        if (settings.contains(CommandSettings.SENDER_TYPE)) {
            Class<? extends CommandSender> value = handle.getSettingValue(CommandSettings.SENDER_TYPE);

            resultFlag = commandSender.getClass().isAssignableFrom(value);

            // If sender type as console is disabled
            if (!resultFlag && settings.contains(CommandSettings.CONSOLE_DISABLED_MESSAGE) && value.isAssignableFrom(ConsoleCommandSender.class)) {

                String message = handle.getSettingValue(CommandSettings.CONSOLE_DISABLED_MESSAGE);
                commandSender.sendMessage(message);
            }
        }

        // Check command arguments length
        if (resultFlag && settings.contains(CommandSettings.EMPTY_ARGS_MESSAGE) && args.length == 0) {

            String message = handle.getSettingValue(CommandSettings.EMPTY_ARGS_MESSAGE);
            commandSender.sendMessage(message);
        }

        // Check sender general permissions
        if (resultFlag && settings.contains(CommandSettings.USE_PERMISSION)) {

            String permission = handle.getSettingValue(CommandSettings.USE_PERMISSION);
            resultFlag = commandSender.hasPermission(permission);

            // If sender is no has permissions then check next setting & send value as message
            if (!resultFlag && settings.contains(CommandSettings.NO_PERMISSION_MESSAGE)) {

                String message = handle.getSettingValue(CommandSettings.NO_PERMISSION_MESSAGE);
                commandSender.sendMessage(message);
            }
        }

        return resultFlag;
    }

}
