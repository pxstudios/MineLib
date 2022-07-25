package net.pxstudios.minelib.event.bukkit.player;

import lombok.Getter;
import lombok.Setter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.player.MLPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
public class MLPlayerDamageEvent extends MLPlayerEvent implements Cancellable {

    private final double damage;
    private final EntityDamageEvent.DamageCause cause;

    @Setter
    private boolean cancelled;

    public MLPlayerDamageEvent(MineLibrary mineLibrary, Player player, EntityDamageEvent.DamageCause cause, double damage) {
        super(mineLibrary, player);

        this.cause = cause;
        this.damage = damage;
    }

}
