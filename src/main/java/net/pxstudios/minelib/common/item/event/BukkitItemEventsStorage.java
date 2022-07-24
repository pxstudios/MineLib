package net.pxstudios.minelib.common.item.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pxstudios.minelib.common.item.BukkitItem;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.function.Consumer;

@Setter
@RequiredArgsConstructor
public final class BukkitItemEventsStorage {

    private final BukkitItem bukkitItem;

    @Getter
    private Consumer<BlockPlaceEvent> onPlaceAsBlock;

    @Getter
    private Consumer<PlayerDropItemEvent> onDrop;

    @Getter
    private Consumer<PlayerPickupItemEvent> onPickUp;

    @Getter
    private Consumer<PlayerInteractEvent> onInteract;

    @Getter
    private Consumer<InventoryClickEvent> onInventoryClick;

    @Getter
    private Consumer<InventoryPickupItemEvent> onInventoryPickup;

    @Getter
    private Consumer<InventoryMoveItemEvent> onInventoryMove;

    @Getter
    private Consumer<EntityShootBowEvent> onShootBow;

    public void listenEvents() {
        BukkitItemEventsHandler.listen(bukkitItem);
    }

}
