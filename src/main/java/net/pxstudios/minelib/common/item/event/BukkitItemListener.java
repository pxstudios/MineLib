package net.pxstudios.minelib.common.item.event;

import net.pxstudios.minelib.common.item.BukkitItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public final class BukkitItemListener implements Listener {

    private static final Set<BukkitItem> registeredBukkitItems = new HashSet<>();

    static void listen(BukkitItem bukkitItem) {
        registeredBukkitItems.add(bukkitItem);
    }

    private <E extends Event> void checkAccessWithPlayer(Player player, E event, ItemStack filter, Function<BukkitItemEventsStorage, Consumer<E>> accessorApplier) {
        if (filter == null) {
            return;
        }

        for (BukkitItem bukkitItem : registeredBukkitItems) {
            BukkitItemEventsStorage bukkitItemEventsStorage = bukkitItem.getEventsStorage();

            if (bukkitItem.getModifiedItem(player).isSimilar(filter)) {

                Consumer<E> eventAccessor = accessorApplier.apply(bukkitItemEventsStorage);

                if (eventAccessor != null) {
                    eventAccessor.accept(event);
                }
            }
        }
    }

    private <E extends PlayerEvent> void checkAccessWithPlayer(E event, ItemStack filter, Function<BukkitItemEventsStorage, Consumer<E>> accessorApplier) {
        checkAccessWithPlayer(event.getPlayer(), event, filter, accessorApplier);
    }

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        checkAccessWithPlayer(event.getPlayer(), event, event.getPlayer().getInventory().getItemInMainHand(), BukkitItemEventsStorage::getOnPlaceAsBlock);
    }

    @EventHandler
    public void handle(PlayerDropItemEvent event) {
        checkAccessWithPlayer(event, event.getItemDrop().getItemStack(), BukkitItemEventsStorage::getOnDrop);
    }

    @EventHandler
    public void handle(PlayerPickupItemEvent event) {
        checkAccessWithPlayer(event, event.getItem().getItemStack(), BukkitItemEventsStorage::getOnPickUp);
    }

    @EventHandler
    public void handle(PlayerInteractEvent event) {
        if (event.hasItem()) {
            checkAccessWithPlayer(event, event.getItem(), BukkitItemEventsStorage::getOnInteract);
        }
    }

    @EventHandler
    public void handle(InventoryClickEvent event) {
        if (event.getWhoClicked().getType() == EntityType.PLAYER) {
            checkAccessWithPlayer((Player) event.getWhoClicked(), event, event.getCurrentItem(), BukkitItemEventsStorage::getOnInventoryClick);
        }
    }

    @EventHandler
    public void handle(InventoryPickupItemEvent event) {
        if (event.getInventory().getHolder() instanceof Player) {
            checkAccessWithPlayer((Player) event.getInventory().getHolder(), event, event.getItem().getItemStack(), BukkitItemEventsStorage::getOnInventoryPickup);
        }
    }

    @EventHandler
    public void handle(InventoryMoveItemEvent event) {
        if (event.getInitiator().getHolder() instanceof Player) {
            checkAccessWithPlayer((Player) event.getInitiator().getHolder(), event, event.getItem(), BukkitItemEventsStorage::getOnInventoryMove);
        }
    }

    @EventHandler
    public void handle(EntityShootBowEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) {
            checkAccessWithPlayer((Player) event.getEntity(), event, event.getBow(), BukkitItemEventsStorage::getOnShootBow);
        }
    }

}
