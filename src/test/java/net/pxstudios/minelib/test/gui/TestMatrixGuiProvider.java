package net.pxstudios.minelib.test.gui;

import net.pxstudios.minelib.common.gui.GuiSlot;
import net.pxstudios.minelib.common.gui.provider.MatrixGuiProvider;
import net.pxstudios.minelib.common.item.BukkitItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public final class TestMatrixGuiProvider extends MatrixGuiProvider {

    public TestMatrixGuiProvider() {
        super(InventoryType.CHEST, "Test GUI");

        setMatrix(new char[][]
                {
                        { ' ', ' ', ' ', ' ', 'O', ' ', ' ', ' ', ' ' },
                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                        { ' ', ' ', ' ', 'a', ' ', 'a', ' ', ' ', ' ' },
                });
    }

    @Override
    public void draw(Player player, MatrixDrawingSession drawingSession) {
        drawingSession.add('O', getOwnerHeadItem());

        drawingSession.add('a', getAnvilItem(), event -> {

            Player clicked = event.getPlayer();

            GuiSlot slot = event.getSlot();

            clicked.sendMessage("You are clicked on " + slot.normalize().toSlotIndex() + " slot");
            event.setCancelled(true);

            // update gui for player.
            super.getGui().updateGui(player);
        });
    }

    private BukkitItem getOwnerHeadItem() {
        return toItem(Material.SKULL_ITEM).withDurabilityAsInt(3).withColoredName('&', "&eMineLibrary Owner")
                .withSkullOwner("itzstonlex").complete();
    }

    private BukkitItem getAnvilItem() {
        return toItem(Material.ANVIL).withLoreArray("Click to me!").complete();
    }

}
