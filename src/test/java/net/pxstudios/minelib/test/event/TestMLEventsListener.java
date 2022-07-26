package net.pxstudios.minelib.test.event;

import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.cooldown.CooldownLeftReason;
import net.pxstudios.minelib.event.bukkit.inventory.MLInventoryClickEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerDamageEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerProjectileHitEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerProjectileLaunchEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerShootBowEvent;
import net.pxstudios.minelib.event.player.MLPlayerCooldownAddEvent;
import net.pxstudios.minelib.event.player.MLPlayerCooldownLeftEvent;
import net.pxstudios.minelib.event.player.MLPlayerKillEvent;
import net.pxstudios.minelib.event.plugin.MLMinecraftPluginDisableEvent;
import net.pxstudios.minelib.event.plugin.MLMinecraftPluginEnableEvent;
import net.pxstudios.minelib.gui.GuiSlot;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public final class TestMLEventsListener implements Listener {

    @EventHandler
    public void handle(MLInventoryClickEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();

        ClickType clickType = event.getClickType();
        InventoryAction inventoryAction = event.getInventoryAction();

        ItemStack currentItem = event.getCurrentItem();
        event.setCurrentItem(currentItem);

        GuiSlot slot = event.getSlot().normalize();

        int rawSlot = event.getRawSlot();
        int hotbarButton = event.getHotbarButton();

        boolean cancelled = event.isCancelled();
        event.setCancelled(cancelled);
    }

    @EventHandler
    public void handle(MLPlayerDamageEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();

        double damage = event.getDamage();

        EntityDamageEvent.DamageCause cause = event.getCause();

        boolean cancelled = event.isCancelled();
        event.setCancelled(cancelled);
    }

    @EventHandler
    public void handle(MLPlayerProjectileHitEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();

        Projectile projectile = event.getProjectile();

        Block hitBlock = event.getHitBlock();
        Entity hitEntity = event.getHitEntity();
    }

    @EventHandler
    public void handle(MLPlayerProjectileLaunchEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();

        Projectile projectile = event.getProjectile();
    }

    @EventHandler
    public void handle(MLPlayerShootBowEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();

        ItemStack bow = event.getBow();

        Entity projectile = event.getProjectile();
        event.setProjectile(projectile);

        boolean cancelled = event.isCancelled();
        event.setCancelled(cancelled);
    }

    @EventHandler
    public void handle(MLPlayerCooldownAddEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();

        String cooldownName = event.getName();
        long millisecondsDelay = event.getMillisecondsDelay();
    }

    @EventHandler
    public void handle(MLPlayerCooldownLeftEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();

        String cooldownName = event.getName();
        long millisecondsDelay = event.getMillisecondsDelay();

        CooldownLeftReason reason = event.getReason();
    }

    @EventHandler
    public void handle(MLPlayerKillEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        Player player = event.getPlayer();
        Player killer = event.getKiller();

        double damage = event.getLastDamage();

        EntityDamageEvent.DamageCause cause = event.getLastDamageCause();
    }

    @EventHandler
    public void handle(MLMinecraftPluginEnableEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        MinecraftPlugin plugin = event.getPlugin();
    }

    @EventHandler
    public void handle(MLMinecraftPluginDisableEvent event) {
        MineLibrary mineLibrary = event.getMineLibrary();

        MinecraftPlugin plugin = event.getPlugin();
    }
}
