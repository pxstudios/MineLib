package net.pxstudios.minelib.command.type;

import net.pxstudios.minelib.command.CommandSettings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class AbstractPlayerBukkitCommand extends AbstractBukkitCommand {

    public AbstractPlayerBukkitCommand(Plugin plugin) {
        super(plugin);
        super.addSetting(CommandSettings.SENDER_TYPE, Player.class);
    }

    public abstract void process(Player player, String label, String... args);

    @Override
    public void process(CommandSender sender, String label, String... args) {
        this.process((Player) sender, label, args);
    }

}
