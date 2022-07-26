package net.pxstudios.minelib.event.bukkit.inventory;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.inventory.MLInventoryEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
public class MLInventoryCloseEvent extends MLInventoryEvent {

    private final Player player;

    public MLInventoryCloseEvent(MineLibrary mineLibrary, Inventory inventory, Player player) {
        super(mineLibrary, inventory);

        this.player = player;
    }

}
