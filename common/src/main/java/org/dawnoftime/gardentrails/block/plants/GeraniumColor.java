package org.dawnoftime.gardentrails.block.plants;

import java.util.Locale;

/** Every geranium color. Blocks, pots, planters, models and textures are all derived from this list. */
public enum GeraniumColor {
    PINK,
    ORANGE,
    PURPLE,
    PURPLEISH,
    RED,
    WHITE;

    /** Lower case color name, used in the ids and the texture names. */
    public String id() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    /** Registry id of the geranium block of this color. */
    public String blockId() {
        return "geranium_" + this.id();
    }
}
