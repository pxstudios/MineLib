package net.pxstudios.minelib.test.command;

import net.pxstudios.minelib.command.CommandSettings;
import net.pxstudios.minelib.command.type.AbstractPlayerBukkitCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class TestAbstractPlayerBukkitCommand extends AbstractPlayerBukkitCommand {

    public TestAbstractPlayerBukkitCommand() {
        super.addLabels("playertest", "ptest");

        super.addSetting(CommandSettings.USE_COOLDOWN_DELAY, 1500L);
    }

    @Override
    public void process(Player player, String label, String... args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Use - /test <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player isn`t online!");
            return;
        }

        PotionEffectType type = PotionEffectType.ABSORPTION;

        int duration = 40;
        int amplifier = 1;

        target.addPotionEffect(new PotionEffect(type, duration, amplifier));

        player.sendMessage("Potion effect success given for " + target.getName());
    }
}
