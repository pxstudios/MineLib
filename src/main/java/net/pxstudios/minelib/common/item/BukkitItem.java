package net.pxstudios.minelib.common.item;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class BukkitItem {

    private final ItemStack itemStack;
    private BukkitItemModifySession modifySession;

    public final void addToInventory(Inventory inventory) {
        inventory.addItem(itemStack);
    }

    public final void setToInventory(int slot, Inventory inventory) {
        inventory.setItem(slot, itemStack);
    }

    public final void multisetToInventory(int[] multiSlots, Inventory inventory) {
        for (int slot : multiSlots) {
            setToInventory(slot, inventory);
        }
    }

    public final Item dropNaturally(Location location) {
        return location.getWorld().dropItemNaturally(location, itemStack);
    }

    public final BukkitItemModifySession getModifySession() {
        return modifySession == null ? (modifySession = new BukkitItemModifySession(this, itemStack)) : modifySession;
    }
}
