package net.pxstudios.minelib.event.player;

import lombok.Getter;
import net.pxstudios.minelib.MineLibrary;
import net.pxstudios.minelib.event.MLEvent;
import org.bukkit.entity.Player;

@Getter
public abstract class MLPlayerEvent extends MLEvent {

    private final Player player;

    public MLPlayerEvent(MineLibrary mineLibrary, Player player) {
        super(mineLibrary);

        this.player = player;
    }
}
