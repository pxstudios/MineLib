package net.pxstudtios.minelib.test.item;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.item.BukkitItem;
import net.pxstudios.minelib.common.item.BukkitItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;

@RequiredArgsConstructor
public final class TestBukkitItemFactoryListener implements Listener {

    private final BukkitItemFactory bukkitItemFactory;
    private BukkitItem cachedBukkitItem;

    private BukkitItem createCompassItem(String playerName) {
        if (cachedBukkitItem != null) {
            return cachedBukkitItem;
        }

        cachedBukkitItem = bukkitItemFactory.getByType(Material.COMPASS);
        cachedBukkitItem.getModifySession()
                .withUnbreakable()
                .withAmount(15)

                .withCustomModifications((player, bukkitItemModifySession) -> bukkitItemModifySession
                        .withColoredName('&', "&eCompass of " + playerName)
                        .withColoredLoreList('&', Arrays.asList(
                                "&7That item created by MineLib",
                                "&7in tests module for " + playerName
                        ))
                        .complete())

                .withFlag(ItemFlag.HIDE_ATTRIBUTES);

        cachedBukkitItem.getEventsStorage().listenEvents();
        cachedBukkitItem.getEventsStorage().setOnInteract(event -> {

            Player player = event.getPlayer();
            player.performCommand("servers");
        });

        return cachedBukkitItem;
    }

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BukkitItem compassItem = createCompassItem(player.getName());
        compassItem.setToPlayerInventory(4, player);
    }

}
