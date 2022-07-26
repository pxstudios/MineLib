package net.pxstudios.minelib.event.bukkit.inventory;

import lombok.Getter;
import lombok.Setter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.inventory.MLInventoryEvent;
import net.pxstudios.minelib.common.gui.GuiSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class MLInventoryClickEvent extends MLInventoryEvent implements Cancellable {

    private final Player player;

    private final ClickType clickType;
    private final InventoryAction inventoryAction;

    private final InventoryType.SlotType slotType;

    private ItemStack currentItem;

    private final GuiSlot slot;
    private final int rawSlot;
    private final int hotbarButton;


    private boolean cancelled;

    public MLInventoryClickEvent(MineLibrary mineLibrary, Inventory inventory, Player player, ClickType clickType, InventoryAction inventoryAction, InventoryType.SlotType slotType,
                                 ItemStack currentItem, GuiSlot slot, int rawSlot, int hotbarButton) {

        super(mineLibrary, inventory);

        this.player = player;

        this.clickType = clickType;
        this.inventoryAction = inventoryAction;

        this.slotType = slotType;

        this.currentItem = currentItem;

        this.slot = slot;
        this.rawSlot = rawSlot;
        this.hotbarButton = hotbarButton;
    }

}
