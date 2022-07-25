package net.pxstudios.minelib.world.rule;

import lombok.Getter;

@Getter
public class WorldGameRule {

    private final WorldGameRuleType type;

    public WorldGameRule(WorldGameRuleType type, String value) {
        this.type = type;
        this.set(value);
    }

    private String asString;

    private int asInt;

    private double asDouble;

    private boolean asBoolean;

    public WorldGameRule set(String value) {
        this.asString = value;
        this.asBoolean = Boolean.parseBoolean(value);

        this.asDouble = this.asInt = asBoolean ? 1 : 0;

        try {
            this.asInt = Integer.parseInt(value);
        }
        catch (NumberFormatException ignored) {
        }

        try {
            this.asDouble = Double.parseDouble(value);
        }
        catch (NumberFormatException ignored) {
        }

        return this;
    }


}
