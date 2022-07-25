package net.pxstudios.minelib.event.bukkit;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.player.MLPlayerKillEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerDamageEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerProjectileHitEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerProjectileLaunchEvent;
import net.pxstudios.minelib.event.bukkit.player.MLPlayerShootBowEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.projectiles.ProjectileSource;

@RequiredArgsConstructor
public final class BukkitEventsWrapperListener implements Listener {

    private final MineLibrary mineLibrary;

    public MLPlayerDamageEvent callPlayerDamageEvent(EntityDamageEvent event) {
        MLPlayerDamageEvent wrapperEvent = new MLPlayerDamageEvent(mineLibrary, (Player) event.getEntity(), event.getCause(), event.getDamage());

        mineLibrary.getEventsSubscriber().callEvent(wrapperEvent);

        return wrapperEvent;
    }

    public MLPlayerShootBowEvent callPlayerShootBowEvent(EntityShootBowEvent event) {
        MLPlayerShootBowEvent wrapperEvent = new MLPlayerShootBowEvent(mineLibrary, (Player) event.getEntity(), event.getBow(), event.getProjectile(), event.getForce());

        mineLibrary.getEventsSubscriber().callEvent(wrapperEvent);

        return wrapperEvent;
    }

    public void callPlayerProjectileHitEvent(ProjectileHitEvent event) {
        mineLibrary.getEventsSubscriber().callEvent(
                new MLPlayerProjectileHitEvent(mineLibrary, (Player) event.getEntity(), event.getEntity(), event.getHitEntity(), event.getHitBlock())
        );
    }

    public void callPlayerProjectileLaunchEvent(ProjectileLaunchEvent event) {
        mineLibrary.getEventsSubscriber().callEvent(
                new MLPlayerProjectileLaunchEvent(mineLibrary, (Player) event.getEntity(), event.getEntity())
        );
    }

    public void callPlayerKillEvent(PlayerDeathEvent event) {
        mineLibrary.getEventsSubscriber().callEvent(
                new MLPlayerKillEvent(mineLibrary, event.getEntity(), event.getEntity().getKiller())
        );
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.PLAYER) {
            event.setCancelled(callPlayerDamageEvent(event).isCancelled());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(EntityShootBowEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.PLAYER) {
            event.setCancelled(callPlayerShootBowEvent(event).isCancelled());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(ProjectileHitEvent event) {
        ProjectileSource entity = event.getEntity().getShooter();

        if (entity instanceof Player) {
            callPlayerProjectileHitEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(ProjectileLaunchEvent event) {
        ProjectileSource entity = event.getEntity().getShooter();

        if (entity instanceof Player) {
            callPlayerProjectileLaunchEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(PlayerDeathEvent event) {

        if (event.getEntity().getKiller() != null) {
            callPlayerKillEvent(event);
        }
    }

}