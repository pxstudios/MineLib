package net.pxstudios.minelib.event.player;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
public class MLPlayerKillEvent extends MLPlayerEvent {

    private final Player killer;

    private final EntityDamageEvent.DamageCause lastDamageCause;
    private final double lastDamage;

    public MLPlayerKillEvent(MineLibrary mineLibrary, Player player, Player killer) {
        super(mineLibrary, player);

        this.killer = killer;

        this.lastDamageCause = player.getLastDamageCause().getCause();
        this.lastDamage = player.getLastDamage();
    }
}
