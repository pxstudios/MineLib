package net.pxstudios.minelib.test.cooldown;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.cooldown.Cooldown;
import net.pxstudios.minelib.cooldown.CooldownFlag;
import net.pxstudios.minelib.cooldown.PlayerCooldownApi;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public final class TestPlayerCooldownListener implements Listener {

    private static final String BLOCK_BREAK_COOLDOWN_NAME = "BlockBreak";
    private static final String INTERACT_COOLDOWN_NAME = "interact001";

    private final PlayerCooldownApi playerCooldownApi;

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        String cooldownName = BLOCK_BREAK_COOLDOWN_NAME;

        if (playerCooldownApi.hasCooldown(player, cooldownName)) {
            long millisLeft = playerCooldownApi.getLeftTime(player, cooldownName);

            player.sendMessage(ChatColor.RED + "COOLDOWN: Please wait " + TimeUnit.MILLISECONDS.toSeconds(millisLeft) + " seconds for repeat this action!");
            event.setCancelled(true);

            return;
        }

        playerCooldownApi.addCooldown(player, Cooldown.bySeconds(cooldownName, 5)
                        .withFlag(CooldownFlag.WITH_AUTO_EXPIRATION)
                        .withFlag(CooldownFlag.REMOVE_ON_PLAYER_QUIT))

                .thenAccept((cooldownLeftReason) -> player.sendMessage(ChatColor.GREEN + "You can to blocks placing now! (" + cooldownLeftReason + ")"));
    }

    @EventHandler
    public void handle(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        String cooldownName = INTERACT_COOLDOWN_NAME;

        if (playerCooldownApi.hasCooldown(player, cooldownName)) {
            event.setCancelled(true);
            return;
        }

        playerCooldownApi.addCooldown(player, Cooldown.byTicks(cooldownName, 30L)
                .withFlag(CooldownFlag.REMOVE_ON_PLAYER_QUIT));

        long millisDelay = playerCooldownApi.getCachedDelay(player, cooldownName);

        player.sendMessage(ChatColor.RED + "COOLDOWN: Please wait " + millisDelay + "ms for repeat this action!");
    }

}
