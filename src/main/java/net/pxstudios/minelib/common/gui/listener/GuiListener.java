package net.pxstudios.minelib.common.gui.listener;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.gui.Gui;
import net.pxstudios.minelib.common.gui.GuiFactory;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryCloseEvent;
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
        Gui gui = playersGuiMap.get(event.getPlayer());

        if (gui != null) {
            gui.onClick(event);
        }
    }

    @EventHandler
    public void handle(MLInventoryCloseEvent event) {
        Player player = event.getPlayer();

        Gui gui = playersGuiMap.remove(event.getPlayer());

        if (gui != null) {
            gui.onClose(player);
        }
    }

}
