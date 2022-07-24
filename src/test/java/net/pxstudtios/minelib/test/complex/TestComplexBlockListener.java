package net.pxstudtios.minelib.test.complex;

import net.pxstudios.minelib.common.complex.ComplexBed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class TestComplexBlockListener implements Listener {

    private boolean isBedType(Block block) {
        return block.getType() == Material.BED_BLOCK;
    }

    private ComplexBed complex(Block block) {
        return new ComplexBed(block);
    }

    @EventHandler
    public void handle(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!isBedType(block)) {
            return;
        }

        ComplexBed complex = complex(block);

        event.setDropItems(false);

        for (Location part : complex.getLocations()) {
            part.getBlock().setType(Material.AIR);
        }
    }
}
