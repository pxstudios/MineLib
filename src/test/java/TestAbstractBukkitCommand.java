import net.pxstudios.minelib.command.CommandSettings;
import net.pxstudios.minelib.command.type.AbstractBukkitCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class TestAbstractBukkitCommand extends AbstractBukkitCommand {

    public TestAbstractBukkitCommand(Plugin plugin) {
        super(plugin);

        super.addSetting(CommandSettings.SENDER_TYPE, Player.class);
        super.addSetting(CommandSettings.USE_COOLDOWN_DELAY, 1500L);
    }

    @Override
    public void process(CommandSender sender, String label, String... args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Use - /test <player>");
            return;
        }

        Player player = ((Player) sender);
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That player isn`t online!");
            return;
        }

        PotionEffectType type = PotionEffectType.ABSORPTION;

        int duration = 40;
        int amplifier = 1;

        target.addPotionEffect(new PotionEffect(type, duration, amplifier));

        player.sendMessage("Potion effect success given for " + target.getName());
    }
}
