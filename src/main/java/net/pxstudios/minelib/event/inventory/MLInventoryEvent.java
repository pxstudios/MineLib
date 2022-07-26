package net.pxstudios.minelib.event.inventory;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.MLEvent;
import org.bukkit.inventory.Inventory;

@Getter
public abstract class MLInventoryEvent extends MLEvent {

    private final Inventory inventory;

    public MLInventoryEvent(MineLibrary mineLibrary, Inventory inventory) {
        super(mineLibrary);

        this.inventory = inventory;
    }
}
