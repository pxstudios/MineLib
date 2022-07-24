package net.pxstudios.minelib.common.complex;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Bed;

public class ComplexBed implements ComplexBlock {

    private static final BlockFace[] FACES = new BlockFace[]{
            BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.SELF
    };

    private final Location[] bedLocations;

    public ComplexBed(Location location) {
        this(location.getBlock());
    }

    public ComplexBed(Block block) {
        bedLocations = new Location[2];

        for (BlockFace face : FACES) {
            Block relative = block.getRelative(face);

            if (relative.getType() == Material.BED_BLOCK) {
                Bed bed = (Bed) relative.getState().getData();
                bedLocations[bed.isHeadOfBed() ? 0 : 1] = relative.getLocation();
            }
        }
    }

    public Location getHeadPart() {
        return bedLocations[0];
    }

    public Location getBodyPart() {
        return bedLocations[1];
    }

    public boolean contains(Location location) {
        for (Location part : getLocations()) {
            if (location.equals(part)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Location[] getLocations() {
        return bedLocations;
    }
}
