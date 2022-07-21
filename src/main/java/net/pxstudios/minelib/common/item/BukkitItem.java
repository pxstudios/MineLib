package net.pxstudios.minelib.common.item;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class BukkitItem {

    private final ItemStack itemStack;
    private BukkitItemModifySession modifySession;


    private void addToInventory0(Inventory inventory, ItemStack itemStack) {
        inventory.addItem(itemStack);
    }

    public final void addToInventory(Inventory inventory) {
        addToInventory0(inventory, getItem());
    }

    public final void addToPlayerInventory(Player player) {
        addToInventory0(player.getInventory(), getModifiedItem(player));
    }

    private void setToInventory0(int slot, Inventory inventory, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public final void setToInventory(int slot, Inventory inventory) {
        setToInventory0(slot, inventory, getItem());
    }

    public final void setToPlayerInventory(int slot, Player player) {
        setToInventory0(slot, player.getInventory(), getModifiedItem(player));
    }

    private void multisetToInventory0(int[] multiSlots, Inventory inventory, ItemStack itemStack) {
        for (int slot : multiSlots) {
            setToInventory0(slot, inventory, itemStack);
        }
    }

    public final void multisetToInventory(int[] multiSlots, Inventory inventory) {
        multisetToInventory0(multiSlots, inventory, getItem());
    }

    public final void multisetToPlayerInventory(int[] multiSlots, Player player) {
        multisetToInventory0(multiSlots, player.getInventory(), getModifiedItem(player));
    }

    public final Item dropNaturally(Location location) {
        return location.getWorld().dropItemNaturally(location, getItem());
    }

    public ItemStack getItem() {
        return itemStack.clone();
    }

    public ItemStack getModifiedItem(Player player) {
        ItemStack item = getItem();

        BukkitItem bukkitItem = modifySession.getCustomItemModifier().apply(player, new BukkitItemModifySession(new BukkitItem(item), item));
        return bukkitItem.getItem();
    }

    public final BukkitItemModifySession getModifySession() {
        return modifySession == null ? (modifySession = new BukkitItemModifySession(this, itemStack)) : modifySession;
    }
}
