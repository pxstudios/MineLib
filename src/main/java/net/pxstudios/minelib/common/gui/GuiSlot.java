package net.pxstudios.minelib.common.gui;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.pxstudios.minelib.common.location.point.Point2D;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiSlot {

    public static Point2D toMatrix2D(int slot) {
        int y = slot / 9;
        int x = slot - (y * 9);

        return new Point2D(x, y);
    }

    public static int toIntSlot(int x, int y) {
        return (y + 1) * 9 - (9 - x);
    }

    public static GuiSlot byArray(int slot) {
        return new GuiSlot(slot, toMatrix2D(slot));
    }

    public static GuiSlot first() {
        return byArray(0);
    }

    public static GuiSlot bySlot(int slot) {
        return byArray(slot - 1);
    }

    public static GuiSlot byMatrixArray(int x, int y) {
        return new GuiSlot(toIntSlot(x, y), new Point2D(x, y));
    }

    public static GuiSlot byMatrix(int x, int y) {
        return byMatrixArray(x - 1, y - 1);
    }

    private int slot;
    private Point2D point2D;

    public final int toSlotIndex() {
        return slot;
    }

    public final int toMatrixX() {
        return (int) point2D.x();
    }

    public final int toMatrixY() {
        return (int) point2D.y();
    }

    public final Point2D toPoint2D() {
        return point2D;
    }

    public final GuiSlot normalize() {
        return new GuiSlot(slot + 1, point2D.clone().add(1, 1));
    }

    public final GuiSlot right(int count) {
        this.slot += count;
        this.point2D = GuiSlot.toMatrix2D(slot);

        return this;
    }

    public final GuiSlot left(int count) {
        int newValue = slot - count;
        if (newValue < 0) {
            throw new IllegalArgumentException("GuiSlot value cannot be < 0");
        }

        this.slot = newValue;
        this.point2D = GuiSlot.toMatrix2D(newValue);

        return this;
    }

    public final GuiSlot up(int count) {
        return left(count * 9);
    }

    public final GuiSlot down(int count) {
        return right(count * 9);
    }

    @Override
    public final GuiSlot clone() {
        return new GuiSlot(slot, point2D);
    }

}
