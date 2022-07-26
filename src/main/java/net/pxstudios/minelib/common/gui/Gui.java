package net.pxstudios.minelib.common.gui;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pxstudios.minelib.common.gui.listener.GuiListener;
import net.pxstudios.minelib.common.gui.provider.GuiProvider;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Gui {

    private final GuiProvider provider;

    @Setter(AccessLevel.PACKAGE)
    private GuiListener listener;

    private final Map<Player, Inventory> inventoryByPlayersMap = new HashMap<>();
    private final Map<Player, GuiProvider.DrawingSession> sessionByPlayersMap = new HashMap<>();

    public final Inventory getInventory(@NonNull Player player) {
        return inventoryByPlayersMap.get(player);
    }

    public final void openGui(@NonNull Player player) {
        createInventory(player);

        listener.addPlayerGui(player, this);
        onOpen(player);
    }

    public final void updateGui(@NonNull Player player) {
        Inventory inventory = getInventory(player);

        if (inventory != null) {
            GuiProvider.DrawingSession drawingSession = provider.createDrawingSession();

            provider.draw(player, drawingSession);
            provider.setup(inventory, drawingSession);

            sessionByPlayersMap.put(player, drawingSession);
        }
    }

    void createInventory(Player player) {
        Inventory inventory;

        InventoryType inventoryType = provider.getInventoryType();

        if (inventoryType != InventoryType.CHEST || inventoryType.getDefaultSize() == provider.getSize()) {
            inventory = Bukkit.createInventory(player, provider.getInventoryType(), provider.getTitle());
        }
        else {
            inventory = Bukkit.createInventory(player, provider.getSize(), provider.getTitle());
        }

        inventoryByPlayersMap.put(player, inventory);
    }

    private void onOpen(Player player) {
        Inventory inventory = inventoryByPlayersMap.get(player);

        if (inventory != null) {
            updateGui(player);
        }
    }

    public void onClose(@NonNull Player player) {
        inventoryByPlayersMap.remove(player);
        sessionByPlayersMap.remove(player);
    }

    public void onClick(@NonNull MLInventoryClickEvent event) {
        Player player = event.getPlayer();

        GuiProvider.DrawingSession drawingSession = sessionByPlayersMap.get(player);

        if (drawingSession != null) {
            GuiItem item = drawingSession.getItem(event.getSlot());

            if (item == null) {
                event.setCancelled(true);
                return;
            }

            item.getClickActionConsumer().accept(event);
        }
    }

}
