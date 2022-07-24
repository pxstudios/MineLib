package net.pxstudios.minelib.common.complex.type;

import net.pxstudios.minelib.common.complex.ComplexBlockAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Bed;

public class ComplexBed extends ComplexBlockAdapter {

    public ComplexBed(Location location) {
        super(Material.BED_BLOCK, location, materialData -> ((Bed) materialData).isHeadOfBed());
    }

    public ComplexBed(Block block) {
        this(block.getLocation());
    }
}
