package net.pxstudios.minelib.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class SingleEventBuilder<T extends Event> {

    private final EventSubscribeHelper<T> eventSubscribeHelper;

    private final Class<T> eventClass;
    private final EventPriority priority;

    private final Map<Predicate<T>, Boolean> predicatesUnregisterMap = new HashMap<>();

    private boolean ignoreCancelled;

    private Consumer<T> handler;

    public SingleEventBuilder<T> withIgnoreCancelled() {
        ignoreCancelled = true;
        return this;
    }

    public SingleEventBuilder<T> withPredication(Predicate<T> eventFilter) {
        predicatesUnregisterMap.put(eventFilter, false);
        return this;
    }

    public SingleEventBuilder<T> withExpiration(Predicate<T> eventFilter) {
        predicatesUnregisterMap.put(eventFilter, true);
        return this;
    }

    public SingleEventBuilder<T> withExpirationTime(long delay, TimeUnit timeUnit) {
        return withExpiration(event -> (System.currentTimeMillis() - eventSubscribeHelper.getRegisteredTimeMillis()) >= timeUnit.toMillis(delay));
    }

    public SingleEventBuilder<T> withMaxUseCount(int maxCountUse) {
        return withExpiration(event -> eventSubscribeHelper.getUsingCounter() >= maxCountUse);
    }

    public void complete(Consumer<T> eventHandler) {
        handler = eventHandler;
        eventSubscribeHelper.registerInBukkit(this);
    }

}
