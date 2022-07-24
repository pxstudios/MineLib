package net.pxstudios.minelib.common.location;

import lombok.Getter;
import lombok.Setter;
import net.pxstudios.minelib.common.location.point.Point2D;
import net.pxstudios.minelib.common.location.point.Point3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.function.Function;

public final class BukkitLocationApi {

    private static final Server SERVER = Bukkit.getServer();

    @Getter
    @Setter
    private String locationToStringFormatSeparator = (", ");

    public String getLocationToStringFormat() {
        return ("world#x#y#z#yaw#pitch").replace("#", locationToStringFormatSeparator);
    }

    public String toString(Location location) {
        String format = getLocationToStringFormat();

        format = format.replace("world", location.getWorld().getName());

        format = format.replace("yaw", Float.toString(location.getYaw()));
        format = format.replace("pitch", Float.toString(location.getPitch()));

        format = format.replace("x", Double.toString(location.getX()));
        format = format.replace("y", Double.toString(location.getY()));
        format = format.replace("z", Double.toString(location.getZ()));

        return format;
    }

    public Location toLocation(String locationString) {
        String[] separatedData = locationString.split(locationToStringFormatSeparator);

        World world = Bukkit.getWorld(separatedData[0]);

        double x = Double.parseDouble(separatedData[1]);
        double y = Double.parseDouble(separatedData[2]);
        double z = Double.parseDouble(separatedData[3]);

        Location parsedLocation = new Location(world, x, y, z);

        if (separatedData.length > 4) {
            parsedLocation.setYaw( Float.parseFloat(separatedData[4]) );

            if (separatedData.length > 5) {
                parsedLocation.setPitch( Float.parseFloat(separatedData[5]) );
            }
        }

        return parsedLocation;
    }

    public Location toLocation(World world, Point2D point) {
        return newBuilder().world(world).x(point.x()).y(point.y()).build();
    }

    public Location toLocation(World world, Point3D point) {
        return newBuilder().world(world).x(point.x()).y(point.y()).z(point.z()).build();
    }

    public Point2D toPoint2D(Location location) {
        return new Point2D(location);
    }

    public Point3D toPoint3D(Location location) {
        return new Point3D(location);
    }

    public BukkitLocationBuilder newBuilder() {
        return new BukkitLocationBuilder(SERVER);
    }

    public Block getHighestBlock(World world, double x, double z) {
        int y = world.getHighestBlockYAt((int) x, (int) z);
        return world.getBlockAt((int) x, y, (int) z);
    }

    public Location toBlockLocation(Location location) {
        return location.getBlock().getLocation();
    }

    public Location toCenterLocation(boolean withHeight, Block block) {
        return block.getLocation().clone().add(.5, withHeight ? .5 : 0, .5);
    }

    public Location toCenterLocation(Block block) {
        return toCenterLocation(false, block);
    }

    public Location toCenterLocation(Location location) {
        return toCenterLocation(location.getBlock());
    }

    public boolean inDistance(double distance, Location first, Location second) {
        return first.distance(second) <= distance;
    }

    private boolean inDistanceByCoordinate(double distance, Location first, Location second, Function<Location, Double> coordinateGetter) {
        double max = Math.max(coordinateGetter.apply(first), coordinateGetter.apply(second));
        double min = Math.min(coordinateGetter.apply(first), coordinateGetter.apply(second));

        return (max - min) <= distance;
    }

    public boolean inDistanceByX(double distance, Location first, Location second) {
        return inDistanceByCoordinate(distance, first, second, Location::getX);
    }

    public boolean inDistanceByY(double distance, Location first, Location second) {
        return inDistanceByCoordinate(distance, first, second, Location::getY);
    }

    public boolean inDistanceByZ(double distance, Location first, Location second) {
        return inDistanceByCoordinate(distance, first, second, Location::getZ);
    }

    public boolean inDistanceByXZ(double distance, Location first, Location second) {
        return inDistanceByX(distance, first, second) && inDistanceByZ(distance, first, second);
    }

    public boolean containsInCuboid(Location firstPoint, Location secondPoint, Location target) {
        double maxX = Math.max(firstPoint.getX(), secondPoint.getX());
        double minX = Math.min(firstPoint.getX(), secondPoint.getX());

        double maxY = Math.max(firstPoint.getY(), secondPoint.getY());
        double minY = Math.min(firstPoint.getY(), secondPoint.getY());

        double maxZ = Math.max(firstPoint.getZ(), secondPoint.getZ());
        double minZ = Math.min(firstPoint.getZ(), secondPoint.getZ());

        return maxX >= target.getX() && target.getX() <= minX
                && maxY >= target.getY() && target.getY() <= minY
                && maxZ >= target.getZ() && target.getZ() <= minZ;
    }
}
