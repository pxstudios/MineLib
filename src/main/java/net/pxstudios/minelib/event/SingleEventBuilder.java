package net.pxstudios.minelib.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class SingleEventBuilder<T extends Event> {

    private final SingleEventSubscribeHelper<T> eventSubscribeHelper;

    private final Class<T> eventClass;
    private final EventPriority priority;

    private final Map<Predicate<T>, Boolean> predicatesUnregisterMap = new HashMap<>();

    private boolean completed;
    private boolean ignoreCancelled;

    private Consumer<T> handler;

    public SingleEventBuilder<T> ignoreCancelled() {
        ignoreCancelled = true;
        return this;
    }

    public SingleEventBuilder<T> useFilter(Predicate<T> eventFilter) {
        predicatesUnregisterMap.put(eventFilter, false);
        return this;
    }

    public SingleEventBuilder<T> useExpire(Predicate<T> eventFilter) {
        predicatesUnregisterMap.put(eventFilter, true);
        return this;
    }

    public SingleEventBuilder<T> useExpireMaxCount(int maxCountUse) {
        return useExpire(event -> eventSubscribeHelper.getUseCounter() >= maxCountUse);
    }

    public SingleEventBuilder<T> useExpireTime(long delay, TimeUnit timeUnit) {
        return useExpire(event -> (System.currentTimeMillis() - eventSubscribeHelper.getSyncTimeMillis()) >= timeUnit.toMillis(delay));
    }

    public void complete(Consumer<T> eventHandler) {
        completed = true;
        handler = eventHandler;

        eventSubscribeHelper.sync(this);
    }

}
