package net.pxstudios.minelib.event.player;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import org.bukkit.entity.Player;

@Getter
public class MLPlayerCooldownAddEvent extends MLPlayerEvent {

    private final String name;
    private final long millisecondsDelay;

    public MLPlayerCooldownAddEvent(MineLibrary mineLibrary, Player player, String name, long millisecondsDelay) {
        super(mineLibrary, player);

        this.name = name;
        this.millisecondsDelay = millisecondsDelay;
    }
}
