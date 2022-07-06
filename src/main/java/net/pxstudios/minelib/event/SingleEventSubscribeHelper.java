package net.pxstudios.minelib.event;

import lombok.*;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Predicate;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SingleEventSubscribeHelper<T extends Event> implements Listener, EventExecutor {

    private final Plugin plugin;
    private SingleEventBuilder<T> singleEventBuilder;

    @Getter
    private long useCounter;

    @Getter
    private boolean active;

    @Getter
    private long syncTimeMillis;

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Listener listener, Event event) {
        if (!active) {
            unregisterListener(event.getClass(), listener);
            return;
        }

        if (singleEventBuilder == null || !singleEventBuilder.isCompleted()) {
            return;
        }

        if (singleEventBuilder.getEventClass() != event.getClass()) {
            return;
        }

        T instance = ((T) event);
        for (Map.Entry<Predicate<T>, Boolean> entry : singleEventBuilder.getPredicatesUnregisterMap().entrySet()) {

            Predicate<T> predicate = entry.getKey();
            boolean unregisterFlag = entry.getValue();

            if (predicate.test(instance)) {

                if (unregisterFlag) {
                    active = false;
                }

                return;
            }
        }

        singleEventBuilder.getHandler().accept(instance);
        useCounter++;
    }

    public void sync(@NonNull SingleEventBuilder<T> singleEventBuilder) {
        this.syncTimeMillis = System.currentTimeMillis();
        this.singleEventBuilder = singleEventBuilder;

        this.active = true;

        plugin.getServer().getPluginManager().registerEvent(
                singleEventBuilder.getEventClass(), this, singleEventBuilder.getPriority(), this, plugin
        );
    }

    @SneakyThrows
    private static void unregisterListener(Class<? extends Event> eventClass, Listener listener) {
        Method getHandlerListMethod = eventClass.getMethod("getHandlerList");

        HandlerList handlerList = (HandlerList)getHandlerListMethod.invoke(null);
        handlerList.unregister(listener);
    }

}
