package net.pxstudios.minelib.common.location;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.common.location.point.Point2D;
import net.pxstudios.minelib.common.location.point.Point3D;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BukkitLocationBuilder {

    private final Server server;

    private World world;

    private double x, y, z;
    private float yaw, pitch;

    public BukkitLocationBuilder world(World world) {
        this.world = world;
        return this;
    }

    public BukkitLocationBuilder world(String worldName) {
        return world(server.getWorld(worldName));
    }

    public BukkitLocationBuilder world(int worldIndex) {
        return world(server.getWorlds().get(worldIndex));
    }

    public BukkitLocationBuilder worldMain() {
        return world(0);
    }

    public BukkitLocationBuilder x(double x) {
        this.x = x;
        return this;
    }

    public BukkitLocationBuilder y(double y) {
        this.y = y;
        return this;
    }

    public BukkitLocationBuilder z(double z) {
        this.z = z;
        return this;
    }

    public BukkitLocationBuilder yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public BukkitLocationBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public Location build() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Point2D build2D() {
        return new Point2D(x, y);
    }

    public Point3D build3D() {
        return new Point3D(x, y, z);
    }
}
