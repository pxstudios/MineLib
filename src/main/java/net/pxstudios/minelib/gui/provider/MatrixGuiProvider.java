package net.pxstudios.minelib.gui.provider;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pxstudios.minelib.gui.GuiItem;
import net.pxstudios.minelib.common.item.BukkitItem;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MatrixGuiProvider extends GuiProvider {

    @Getter
    private char[][] matrix;

    public MatrixGuiProvider(InventoryType inventoryType, String name) {
        super(inventoryType, name, 0);
    }

    public MatrixGuiProvider(String name) {
        this(InventoryType.CHEST, name);
    }

    public MatrixGuiProvider(InventoryType inventoryType) {
        this(inventoryType, "");
    }

    public MatrixGuiProvider() {
        this(InventoryType.CHEST);
    }

    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;

        setSize(matrix.length * matrix[0].length);
    }

    public abstract void draw(Player player, MatrixDrawingSession drawingSession);

    @Override
    public MatrixDrawingSession createDrawingSession() {
        return new MatrixDrawingSession();
    }

    @Override
    public void draw(Player player, DrawingSession drawingSession) {
        int x = 0;
        int y = 0;

        MatrixDrawingSession matrixDrawingSession = (MatrixDrawingSession) drawingSession;

        for (char[] row : matrix) {
            y++;

            for (char sign : row) {
                x++;

                GuiItem guiItem = matrixDrawingSession.getItemBySign(sign);

                if (guiItem != null) {
                    guiItem.setSlot(toSlot(x, y));

                    drawingSession.add(guiItem);
                }
            }
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MatrixDrawingSession extends DrawingSession {

        private final Map<Character, GuiItem> matrixItemsMap = new HashMap<>();

        public void add(char sign, GuiItem guiItem) {
            matrixItemsMap.put(sign, guiItem);
        }

        public void add(char sign, ItemStack itemStack, Consumer<MLInventoryClickEvent> eventHandler) {
            add(sign, new GuiItem(null, itemStack, eventHandler));
        }

        public void add(char sign, BukkitItem bukkitItem, Consumer<MLInventoryClickEvent> eventHandler) {
            add(sign, bukkitItem.getItem(), eventHandler);
        }

        public void add(char sign, Player player, BukkitItem bukkitItem, Consumer<MLInventoryClickEvent> eventHandler) {
            add(sign, bukkitItem.getModifiedItem(player), eventHandler);
        }

        public void add(char sign, ItemStack itemStack) {
            add(sign, itemStack, null);
        }

        public void add(char sign, BukkitItem bukkitItem) {
            add(sign, bukkitItem, null);
        }

        public void add(char sign, Player player, BukkitItem bukkitItem) {
            add(sign, player, bukkitItem, null);
        }

        public final GuiItem getItemBySign(char sign) {
            return matrixItemsMap.get(sign);
        }

        public final Collection<GuiItem> getSignedItems() {
            return Collections.unmodifiableCollection(matrixItemsMap.values());
        }
    }

}
