package net.pxstudtios.minelib.test.command;

import net.pxstudios.minelib.command.CommandContext;
import net.pxstudios.minelib.command.CommandSettings;
import net.pxstudios.minelib.command.type.AbstractContextCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class TestAbstractContextCommand extends AbstractContextCommand {

    public TestAbstractContextCommand(Plugin plugin) {
        super(plugin);

        super.addLabels("contexttest", "ctest");

        super.addSetting(CommandSettings.SENDER_TYPE, Player.class);
        super.addSetting(CommandSettings.USE_COOLDOWN_DELAY, 1500L);
        super.addSetting(CommandSettings.EMPTY_ARGS_MESSAGE);
    }

    @Override
    public void process(CommandContext context) {
        Player sender = context.senderAsPlayer();

        context.withArgumentsOrFail(1, () -> {

            // arguments index starts with 1
            context.argument(1).asOnlinePlayerOrFail(() -> sender.sendMessage(ChatColor.RED + "That player isn`t online!"))
                    .ifPresent(player -> {

                        PotionEffectType type = context.argument(2).asOrFail(PotionEffectType::getByName, () -> PotionEffectType.ABSORPTION);

                        int duration = context.argument(3).asInt().orElse(40);
                        int amplifier = context.argument(4).asInt().orElse(1);

                        player.addPotionEffect(new PotionEffect(type, duration, amplifier));
                    });

        }, () -> sender.sendMessage(ChatColor.RED + "Usage - /test <player>* <potion> <duration> <amplifier>"));
    }

}
