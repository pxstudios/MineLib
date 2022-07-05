package net.pxstudios.minelib.command;

import lombok.AccessLevel;
import lombok.Getter;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

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
        handle.process(new CommandContext(handle.getSettings(), commandSender, args, label));
        return false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return execute(commandSender, label, args);
    }

}
