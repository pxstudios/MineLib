package net.pxstudios.minelib.event.bukkit.player;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.player.MLPlayerEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

@Getter
public class MLPlayerProjectileHitEvent extends MLPlayerEvent {

    private final Projectile projectile;

    private final Entity hitEntity;
    private final Block hitBlock;

    public MLPlayerProjectileHitEvent(MineLibrary mineLibrary, Player player, Projectile projectile, Entity hitEntity, Block hitBlock) {
        super(mineLibrary, player);

        this.projectile = projectile;

        this.hitEntity = hitEntity;
        this.hitBlock = hitBlock;
    }

}
