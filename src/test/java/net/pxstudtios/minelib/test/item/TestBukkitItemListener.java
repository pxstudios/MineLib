package net.pxstudtios.minelib.test.item;

import net.pxstudios.minelib.item.BukkitItem;
import net.pxstudios.minelib.item.BukkitItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;

public final class TestBukkitItemListener implements Listener {

    private BukkitItem createCompassItem(String playerName) {
        BukkitItem bukkitItem = BukkitItemFactory.getByType(Material.COMPASS);
        bukkitItem.getModifySession()
                .withUnbreakable()
                .withAmount(15)

                .withColoredName('&', "&eCompass of " + playerName)
                .withColoredLoreList('&', Arrays.asList(
                        "&7That item created by MineLib",
                        "&7in tests module"
                ))
                .withFlag(ItemFlag.HIDE_ATTRIBUTES);

        return bukkitItem;
    }

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BukkitItem compassItem = createCompassItem(player.getName());
        compassItem.setToInventory(4, player.getInventory());
    }

}
