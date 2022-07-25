package net.pxstudios.minelib.subscription;

import lombok.RequiredArgsConstructor;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

@RequiredArgsConstructor
public final class EventsSubscriber {

    private final MinecraftPlugin plugin;

    public void callEvent(Event event) {
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public <T extends Event> SingleEventBuilder<T> subscribe(Class<T> event, EventPriority priority) {
        return new SingleEventBuilder<>(new EventSubscribeHelper<>(plugin), event, priority);
    }

    public <T extends Event> SingleEventBuilder<T> subscribe(Class<T> event) {
        return subscribe(event, EventPriority.NORMAL);
    }

}
