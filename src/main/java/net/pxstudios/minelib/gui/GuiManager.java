package net.pxstudios.minelib.gui;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.gui.listener.GuiListener;
import net.pxstudios.minelib.gui.provider.GuiProvider;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.event.inventory.InventoryType;

@RequiredArgsConstructor
public final class GuiManager {

    private final MinecraftPlugin plugin;

    @Getter
    private final GuiListener listener = new GuiListener();

    public Gui createGui(@NonNull GuiProvider guiProvider) {
        return new Gui(plugin, guiProvider).initialize();
    }

    public int getDefaultSize(InventoryType inventoryType) {
        return inventoryType.getDefaultSize();
    }

    public int getDefaultSize() {
        return getDefaultSize(InventoryType.CHEST);
    }

}
