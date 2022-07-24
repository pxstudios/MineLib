package net.pxstudios.minelib.common.world.time;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorldTimeType {

    // morning
    EARLY_MORNING(0, "06:00", "00:00"),
    MORNING(2000, "08:00", "01:40"),
    LATE_MORNING(5000, "10:00", "4:30"),

    // dinner / lunch
    EARLY_DINNER(6000, "12:00", "04:46"),
    DINNER(6000, "12:00", "04:46"),
    LATE_DINNER(9000, "15:00", "07:30"),

    // evening
    EARLY_EVENING(11_834, "17:50", "09:41"),
    EVENING(12_000, "18:00", "10:00"),
    LATE_EVENING(13_000, "19:00", "10:50"),

    // night
    EARLY_NIGHT(13_702, "19:42", "11:25"),
    NIGHT(17_843, "23:50", "14:52"),
    LATE_NIGHT(18_000, "00:00", "15:00"),

    // sunrise
    EARLY_SUNRISE(22_300, "04:18", "18:35"),
    SUNRISE(23_000, "05:00", "19:10"),
    LATE_SUNRISE(23_992, "05:59", "19:59"),
    ;

    private final long minecraftTicks;

    private final String minecraftTime, realTime;
}
