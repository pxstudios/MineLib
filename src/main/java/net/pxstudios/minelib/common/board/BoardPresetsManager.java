package net.pxstudios.minelib.common.board;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class BoardPresetsManager {

    private final Map<String, Supplier<String>> presetsMap = new HashMap<>();

    public void addPreset(String key, Supplier<String> presetSupplier) {
        presetsMap.put(key.toLowerCase(), presetSupplier);
    }

    public void addPreset(String key, String preset) {
        addPreset(key.toLowerCase(), () -> preset);
    }

    public void removePreset(String key) {
        presetsMap.remove(key.toLowerCase());
    }

    public Supplier<String> getPresetAsSupplier(String key) {
        return presetsMap.getOrDefault(key.toLowerCase(), () -> null);
    }

    public String getPresetAsString(String key) {
        return getPresetAsSupplier(key).get();
    }

    public Set<String> getPresetsKeys() {
        return Collections.unmodifiableSet(presetsMap.keySet());
    }

    public void addAll(BoardPresetsManager presetsManager) {
        presetsMap.putAll(presetsManager.presetsMap);
    }

    public void removeAll() {
        presetsMap.clear();
    }
}