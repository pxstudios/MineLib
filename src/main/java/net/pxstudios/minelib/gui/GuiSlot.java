package net.pxstudios.minelib.gui;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.location.point.Point2D;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiSlot {

    public static GuiSlot byArray(int slotIndex) {
        int y = slotIndex / 9;
        int x = slotIndex - (y * 9);

        return new GuiSlot(slotIndex, new Point2D(x, y));
    }

    public static GuiSlot bySlot(int slot) {
        return byArray(slot - 1);
    }

    public static GuiSlot byMatrixArray(int x, int y) {
        return new GuiSlot((y + 1) * 9 - (9 - x), new Point2D(x, y));
    }

    public static GuiSlot byMatrix(int x, int y) {
        return byMatrixArray(x - 1, y - 1);
    }

    private final int slot;
    private final Point2D point2D;

    public final int toArraySlot() {
        return slot;
    }

    public final int toSlot() {
        return slot + 1;
    }

    public final int toArrayX() {
        return (int) point2D.x();
    }

    public final int toArrayY() {
        return (int) point2D.y();
    }

    public Point2D toPoint2D() {
        return point2D;
    }

}
