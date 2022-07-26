package net.pxstudios.minelib.gui.listener;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryCloseEvent;
import net.pxstudios.minelib.gui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class GuiListener implements Listener {

    private final Map<Player, Gui> playersGuiMap = new HashMap<>();

    public void addPlayerGui(Player player, Gui gui) {
        playersGuiMap.put(player, gui);
    }

    @EventHandler
    public void handle(MLInventoryClickEvent event) {
        Player player = event.getPlayer();
        Gui gui = playersGuiMap.get(player);

        if (gui != null) {
            gui.onClick(player, event);
        }
    }

    @EventHandler
    public void handle(MLInventoryCloseEvent event) {
        Player player = event.getPlayer();
        Gui gui = playersGuiMap.remove(player);

        if (gui != null) {
            gui.onClose(player);
        }
    }

}
