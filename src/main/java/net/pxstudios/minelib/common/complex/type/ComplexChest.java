package net.pxstudios.minelib.common.complex.type;

import net.pxstudios.minelib.common.complex.ComplexBlockAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class ComplexChest extends ComplexBlockAdapter {

    public ComplexChest(Location location) {
        super(Material.CHEST, location, null);
    }

    public ComplexChest(Block block) {
        this(block.getLocation());
    }

}
