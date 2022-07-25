package net.pxstudios.minelib.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.MineLibrary;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public abstract class MLEvent extends Event {

    private final MineLibrary mineLibrary;

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
