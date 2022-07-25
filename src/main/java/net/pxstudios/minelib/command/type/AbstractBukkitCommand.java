package net.pxstudios.minelib.command.type;

import net.pxstudios.minelib.command.CommandContext;
import org.bukkit.command.CommandSender;

public abstract class AbstractBukkitCommand extends AbstractContextCommand {

    public abstract void process(CommandSender sender, String label, String... args);

    @Override
    public void process(CommandContext context) {
        this.process(context.sender(), context.getLabel(), context.getArguments());
    }

}
