package net.pxstudios.minelib.test.registry;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.registry.type.EventListenerRegistryObject;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.MaterialData;

public final class TestRegistryListener extends EventListenerRegistryObject {

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void handle(BlockBreakEvent event) {
        Block block = event.getBlock();
        MaterialData materialData = block.getState().getData();

        event.setDropItems(false);
    }
}
