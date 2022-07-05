package net.pxstudios.minelib.command;

import net.pxstudios.minelib.command.type.AbstractContextCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BukkitCommandExecuteWrapper extends Command implements CommandExecutor {
    private final AbstractContextCommand handle;

    protected BukkitCommandExecuteWrapper(String name, AbstractContextCommand handle) {
        super(name);
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
