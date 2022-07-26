package net.pxstudios.minelib.gui;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiSlot {

    public static GuiSlot byArray(int slotIndex) {
        return new GuiSlot(slotIndex);
    }

    public static GuiSlot bySlot(int slot) {
        return byArray(slot - 1);
    }

    public static GuiSlot byMatrixArray(int x, int y) {
        return new GuiSlot((y + 1) * 9 - (9 - x));
    }

    public static GuiSlot byMatrix(int x, int y) {
        return byMatrixArray(x - 1, y - 1);
    }

    private final int slot;

    public final int normalize() {
        return slot + 1;
    }
}
