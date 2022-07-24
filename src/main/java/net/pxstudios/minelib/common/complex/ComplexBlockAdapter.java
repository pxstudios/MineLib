package net.pxstudios.minelib.common.complex;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

import java.util.function.Function;

public abstract class ComplexBlockAdapter {

    private static final BlockFace[] FACES = new BlockFace[]{
            BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.SELF
    };

    @Getter
    private final Location[] locations;

    public ComplexBlockAdapter(Material material, Location location, Function<MaterialData, Boolean> headDetectFunction) {
        locations = new Location[2];

        if (headDetectFunction == null) {
            headDetectFunction = (data) -> locations[0] == null;
        }

        for (BlockFace face : FACES) {
            Block relative = location.getBlock().getRelative(face);

            if (relative.getType() == material) {
                locations[headDetectFunction.apply(relative.getState().getData()) ? 0 : 1] = relative.getLocation();
            }
        }
    }

    public Location getHeadPart() {
        return locations[0];
    }

    public Location getBodyPart() {
        return locations[1];
    }

    public boolean contains(Location location) {
        for (Location part : getLocations()) {
            if (location.equals(part)) {
                return true;
            }
        }

        return false;
    }

}
