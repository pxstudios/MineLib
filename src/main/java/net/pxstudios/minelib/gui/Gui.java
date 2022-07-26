package net.pxstudios.minelib.gui;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import net.pxstudios.minelib.gui.provider.GuiProvider;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Gui {

    private final MinecraftPlugin plugin;

    private final GuiProvider provider;

    private final Map<Player, Inventory> inventoryByPlayersMap = new HashMap<>();
    private final Map<Player, GuiProvider.DrawingSession> sessionByPlayersMap = new HashMap<>();

    Gui initialize() {
        provider.setPlugin(plugin);
        provider.setGui(this);

        return this;
    }

    private Inventory getBukkitGui(@NonNull Player player) {
        return inventoryByPlayersMap.get(player);
    }

    public final boolean isOpened(Player player) {
        return getBukkitGui(player) != null;
    }

    private void updateGui0(Player player, Inventory bukkit) {
        GuiProvider.DrawingSession drawingSession = provider.createDrawingSession();

        provider.draw(player, drawingSession);
        provider.setup(bukkit, drawingSession);

        sessionByPlayersMap.put(player, drawingSession);
    }

    private Inventory createInventory(Player player) {
        Inventory bukkit;

        InventoryType inventoryType = provider.getInventoryType();

        String title = provider.getTitle();

        if (inventoryType != InventoryType.CHEST) {
            bukkit = Bukkit.createInventory(player, inventoryType, title);
        }
        else {
            bukkit = Bukkit.createInventory(player, provider.getSize(), title);
        }

        inventoryByPlayersMap.put(player, bukkit);
        return bukkit;
    }

    public final void openGui(@NonNull Player player) {
        Inventory bukkit = createInventory(player);

        plugin.getMineLibrary().getGuiManager().getListener().addPlayerGui(player, this);
        updateGui0(player, bukkit);
    }

    public final void updateGui(@NonNull Player player) {
        Inventory bukkit = getBukkitGui(player);

        if (bukkit != null) {
            updateGui0(player, bukkit);
        }
    }

    public void onClose(@NonNull Player player) {
        inventoryByPlayersMap.remove(player);
        sessionByPlayersMap.remove(player);
    }

    public void onClick(@NonNull Player player, @NonNull MLInventoryClickEvent event) {
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
