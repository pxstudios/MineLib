package net.pxstudios.minelib.common.location.point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Point2D {

    public Point2D(Location location) {
        this(location.getX(), location.getY());
    }
    double x, y;

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public Point2D x(double x) {
        this.x = x;
        return this;
    }

    public Point2D y(double y) {
        this.y = y;
        return this;
    }

    public Point2D set(double x, double y) {
        this.x = x;
        this.y = y;

        return this;
    }

    public Point2D add(double x, double y) {
        this.x += x;
        this.y += y;

        return this;
    }

    public Point2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;

        return this;
    }

    public Point2D divide(double x, double y) {
        this.x /= x;
        this.y /= y;

        return this;
    }

    public Point2D multiply(double x, double y) {
        this.x *= x;
        this.y *= y;

        return this;
    }

    public Point2D zero() {
        return set(0, 0);
    }

    public Point3D to3D(double z) {
        return new Point3D(x, y, z);
    }

    public Point3D to3D() {
        return to3D(0);
    }

}
