package net.pxstudios.minelib.world.weather;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum WorldWeatherType {

    SUN(Arrays.asList("sun", "sunlight", "morning", "light")),

    RAIN(Arrays.asList("downfall", "water", "waterfall", "fall")),

    THUNDER(Collections.singletonList("storm")),
    ;

    private final List<String> aliases;

    public boolean isNamedBy(String name) {
        return name.equalsIgnoreCase(name()) || aliases.contains(name.toLowerCase());
    }
}
