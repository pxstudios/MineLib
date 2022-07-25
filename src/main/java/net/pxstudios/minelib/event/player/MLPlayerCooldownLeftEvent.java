package net.pxstudios.minelib.event.player;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.cooldown.CooldownLeftReason;
import org.bukkit.entity.Player;

@Getter
public class MLPlayerCooldownLeftEvent extends MLPlayerEvent {

    private final String name;
    private final long millisecondsDelay;

    private final CooldownLeftReason reason;

    public MLPlayerCooldownLeftEvent(MineLibrary mineLibrary, Player player, String name, long millisecondsDelay,
                                     CooldownLeftReason reason) {
        super(mineLibrary, player);

        this.name = name;
        this.millisecondsDelay = millisecondsDelay;

        this.reason = reason;
    }
}
