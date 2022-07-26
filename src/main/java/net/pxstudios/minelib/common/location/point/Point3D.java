package net.pxstudios.minelib.common.location.point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Point3D {

    public Point3D(Location location) {
        this(location.getX(), location.getY(), location.getZ());
    }
    double x, y, z;

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public Point3D x(double x) {
        this.x = x;
        return this;
    }

    public Point3D y(double y) {
        this.y = y;
        return this;
    }

    public Point3D z(double z) {
        this.z = z;
        return this;
    }

    public Point3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Point3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public Point3D subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    public Point3D divide(double x, double y, double z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;

        return this;
    }

    public Point3D multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;

        return this;
    }

    public Point3D zero() {
        return set(0, 0, 0);
    }

    public Point2D to2D() {
        return new Point2D(x, y);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    @Override
    public final Point3D clone() {
        return new Point3D(x, y, z);
    }
}
