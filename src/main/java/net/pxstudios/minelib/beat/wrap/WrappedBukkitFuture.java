package net.pxstudios.minelib.beat.wrap;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.pxstudios.minelib.beat.BukkitBeater;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class WrappedBukkitFuture<T> {

    protected final BukkitBeater beater;
    protected final Future<T> future;

    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isDone() {
        return future.isDone();
    }

    @SneakyThrows
    public T get() {
        return future.get();
    }

    @SneakyThrows
    public T get(long timeout, TimeUnit unit) {
        return future.get(timeout, unit);
    }
}
