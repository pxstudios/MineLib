package net.pxstudios.minelib.registry.provider.type;

import net.pxstudios.minelib.subscription.EventsSubscriber;
import net.pxstudios.minelib.subscription.SingleEventBuilder;
import net.pxstudios.minelib.plugin.MinecraftPlugin;
import net.pxstudios.minelib.registry.BukkitRegistryObject;
import net.pxstudios.minelib.registry.provider.BukkitRegistryObjectProvider;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class EventListenerRegistryObjectProvider implements BukkitRegistryObjectProvider<Listener> {

    @Override
    public void fireRegister(MinecraftPlugin plugin, Listener obj) {
        if (obj != null) {
            plugin.getServer().getPluginManager().registerEvents(obj, plugin);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Listener newObjectInstance(MinecraftPlugin plugin, BukkitRegistryObject<Listener> bukkitRegistryObject) {
        for (Method method : bukkitRegistryObject.getClass().getMethods()) {
            if (method.getParameterCount() != 1) {
                continue;
            }

            EventHandler eventHandler = method.getDeclaredAnnotation(EventHandler.class);
            if (eventHandler != null) {

                Class<?> parameter = method.getParameterTypes()[0];

                if (!parameter.isAssignableFrom(Event.class)) {
                    continue;
                }

                subscribe(plugin.getMineLibrary().getEventsSubscriber(), (Class<? extends Event>) parameter, eventHandler, event -> {

                    try {
                        method.invoke(bukkitRegistryObject, event);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }
        }
        return null;
    }

    private <E extends Event> void subscribe(EventsSubscriber eventsSubscriber, Class<E> cls, EventHandler eventHandler, Consumer<E> completable) {
        SingleEventBuilder<E> singleEventBuilder = eventsSubscriber.subscribe(cls, eventHandler.priority());

        if (eventHandler.ignoreCancelled()) {
            singleEventBuilder.withIgnoreCancelled();
        }

        singleEventBuilder.complete(completable);
    }

}
