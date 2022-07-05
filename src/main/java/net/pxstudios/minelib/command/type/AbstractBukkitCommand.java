package net.pxstudios.minelib.command.type;

import net.pxstudios.minelib.command.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class AbstractBukkitCommand extends AbstractContextCommand {

    public AbstractBukkitCommand(Plugin plugin) {
        super(plugin);
    }

    public abstract void process(CommandSender sender, String label, String... args);

    @Override
    public void process(CommandContext context) {
        this.process(context.sender(), context.getLabel(), context.getArguments());
    }

}
