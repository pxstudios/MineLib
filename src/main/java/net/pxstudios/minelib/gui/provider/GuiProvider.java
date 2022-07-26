package net.pxstudios.minelib.gui.provider;

import lombok.*;
import net.pxstudios.minelib.gui.Gui;
import net.pxstudios.minelib.gui.GuiItem;
import net.pxstudios.minelib.gui.GuiSlot;
import net.pxstudios.minelib.common.item.BukkitItem;
import net.pxstudios.minelib.common.item.BukkitItemModifySession;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Consumer;

public abstract class GuiProvider {

    protected static int toSize(int rows) {
        return rows * 9;
    }

    protected static GuiSlot toSlot(int slot) {
        return GuiSlot.bySlot(slot);
    }

    protected static GuiSlot toSlot(int x, int y) {
        return GuiSlot.byMatrix(x, y);
    }

    protected static BukkitItemModifySession toItem(ItemStack itemStack) {
        return new BukkitItem(itemStack).getModifySession();
    }

    protected static BukkitItemModifySession toItem(MaterialData materialData) {
        return toItem(materialData.toItemStack(1));
    }

    protected static BukkitItemModifySession toItem(Material material) {
        return toItem(new MaterialData(material));
    }

    @Getter
    private final InventoryType inventoryType;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String title;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int size;

    @Getter
    @Setter
    private Gui gui;

    @Getter
    @Setter
    private MinecraftPlugin plugin;

    public GuiProvider(InventoryType inventoryType, String title, int size) {
        this.inventoryType = inventoryType;
        this.title = title;
        this.size = size;
    }

    public GuiProvider(String title, int size) {
        this(InventoryType.CHEST, title, size);
    }

    public GuiProvider(int size) {
        this("", size);
    }

    public GuiProvider(String title) {
        this(title, 27);
    }

    public abstract void draw(Player player, DrawingSession session);

    public DrawingSession createDrawingSession() {
        return new DrawingSession();
    }

    public final void update(Player player) {
        if (gui != null) {
            gui.updateGui(player);
        }
    }

    public final void update(int ticks, Player player) {
        plugin.getMineLibrary().getBeater().runCancellableTimer(ticks, new BukkitRunnable() {

            @Override
            public void run() {

                if (player.isOnline() && gui.isOpened(player)) {
                    update(player);
                }
                else {
                    cancel();
                }
            }
        });
    }

    public void setup(Inventory bukkit, DrawingSession session) {
        bukkit.clear();

        for (GuiItem item : session.getItems()) {
            bukkit.setItem(item.getSlot().toSlotIndex(), item.getItemStack());
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DrawingSession {

        protected final Map<GuiSlot, GuiItem> itemsBySlotsMap = new HashMap<>();

        public void add(GuiItem guiItem) {
            itemsBySlotsMap.put(guiItem.getSlot(), guiItem);
        }

        public void add(GuiSlot guiSlot, ItemStack itemStack, Consumer<MLInventoryClickEvent> eventHandler) {
            add(new GuiItem(guiSlot, itemStack, eventHandler));
        }

        public void add(GuiSlot guiSlot, BukkitItem bukkitItem, Consumer<MLInventoryClickEvent> eventHandler) {
            add(new GuiItem(guiSlot, bukkitItem.getItem(), eventHandler));
        }

        public void add(GuiSlot guiSlot, Player player, BukkitItem bukkitItem, Consumer<MLInventoryClickEvent> eventHandler) {
            add(new GuiItem(guiSlot, bukkitItem.getModifiedItem(player), eventHandler));
        }

        public void add(GuiSlot guiSlot, ItemStack itemStack) {
            add(guiSlot, itemStack, null);
        }

        public void add(GuiSlot guiSlot, BukkitItem bukkitItem) {
            add(guiSlot, bukkitItem.getItem());
        }

        public void add(GuiSlot guiSlot, Player player, BukkitItem bukkitItem) {
            add(guiSlot, bukkitItem.getModifiedItem(player));
        }

        public final GuiItem getItem(GuiSlot slot) {
            return itemsBySlotsMap.get(slot);
        }

        public final Collection<GuiItem> getItems() {
            return Collections.unmodifiableCollection(itemsBySlotsMap.values());
        }
    }

}
