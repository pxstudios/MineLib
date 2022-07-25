package net.pxstudios.minelib.event.wrapper.player;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.player.MLPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

@Getter
public class MLPlayerProjectileLaunchEvent extends MLPlayerEvent {

    private final Projectile projectile;

    public MLPlayerProjectileLaunchEvent(MineLibrary mineLibrary, Player player, Projectile projectile) {
        super(mineLibrary, player);

        this.projectile = projectile;
    }

}
