package net.pxstudios.minelib.event;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public final class EventsSubscriber {

    private final Plugin plugin;

    public void callEvent(Event event) {
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public <T extends Event> SingleEventBuilder<T> subscribe(Class<T> event, EventPriority priority) {
        return new SingleEventBuilder<>(new SingleEventSubscribeHelper<>(plugin), event, priority);
    }

    public <T extends Event> SingleEventBuilder<T> subscribe(Class<T> event) {
        return subscribe(event, EventPriority.NORMAL);
    }

}
