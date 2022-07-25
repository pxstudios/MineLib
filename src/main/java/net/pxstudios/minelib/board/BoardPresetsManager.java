package net.pxstudios.minelib.board;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class BoardPresetsManager {

    private final Map<String, Supplier<String>> map = new HashMap<>();

    public void add(String key, Supplier<String> presetSupplier) {
        map.put(key.toLowerCase(), presetSupplier);
    }

    public void add(String key, String preset) {
        add(key.toLowerCase(), () -> preset);
    }

    public void delete(String key) {
        map.remove(key.toLowerCase());
    }

    public Supplier<String> getAsSupplier(String key) {
        return map.getOrDefault(key.toLowerCase(), () -> null);
    }

    public String getAsString(String key) {
        return getAsSupplier(key).get();
    }

    public Set<String> getKeys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public void addAll(BoardPresetsManager presetsManager) {
        map.putAll(presetsManager.map);
    }

    public void removeAll() {
        map.clear();
    }
}