package net.pxstudios.minelib.common.gui;

import lombok.NonNull;
import net.pxstudios.minelib.common.gui.listener.GuiListener;
import net.pxstudios.minelib.common.gui.provider.GuiProvider;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.event.inventory.InventoryType;

public final class GuiFactory {

    private static GuiListener GUI_LISTENER;

    public int getDefaultSize(InventoryType inventoryType) {
        return inventoryType.getDefaultSize();
    }

    public int getDefaultSize() {
        return getDefaultSize(InventoryType.CHEST);
    }

    public Gui createGui(@NonNull GuiProvider provider) {
        Gui gui = new Gui(provider);

        provider.setGui(gui);

        return gui;
    }

    public void registerListener(@NonNull MinecraftPlugin plugin) {
        if (GUI_LISTENER == null) {
            plugin.getServer().getPluginManager().registerEvents(GUI_LISTENER = new GuiListener(), plugin);
        }
    }

}
