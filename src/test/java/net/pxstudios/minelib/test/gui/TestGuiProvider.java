package net.pxstudios.minelib.test.gui;

import net.pxstudios.minelib.gui.provider.GuiProvider;
import net.pxstudios.minelib.gui.GuiSlot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public final class TestGuiProvider extends GuiProvider {

    public TestGuiProvider() {
        super(InventoryType.CHEST, "Test GUI", toSize(5));
    }

    @Override
    public void draw(Player player, DrawingSession drawingSession) {
        drawingSession.add(toSlot(1, 2), toItem(Material.SKULL_ITEM)
                .withDurabilityAsInt(3)

                .withColoredName('&', "&eMineLibrary Owner")

                .withSkullOwner("itzstonlex")
                .complete());

        drawingSession.add(toSlot(3, 5), toItem(Material.ANVIL)
                .withLoreArray("Click to me!")
                .complete(),

                event -> {

                    Player clicked = event.getPlayer();

                    GuiSlot slot = event.getSlot();

                    clicked.sendMessage("You are clicked on " + slot.normalize().toSlotIndex() + " slot");
                    event.setCancelled(true);

                    // update gui for player.
                    super.getGui().updateGui(player);
                });
    }

}
