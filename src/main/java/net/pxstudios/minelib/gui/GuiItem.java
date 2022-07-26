package net.pxstudios.minelib.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class GuiItem {

    @Setter
    private GuiSlot slot;

    private final ItemStack itemStack;

    private final Consumer<MLInventoryClickEvent> clickActionConsumer;
}
