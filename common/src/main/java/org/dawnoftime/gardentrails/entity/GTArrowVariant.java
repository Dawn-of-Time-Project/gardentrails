package org.dawnoftime.gardentrails.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.dawnoftime.gardentrails.GTCommon;

public enum GTArrowVariant {
    // Vanilla arrow base damage is 2.0, see docs/feathered-arrows-implementation.md for the full table
    RED("red_arrow", 2.25D),
    GREEN("green_arrow", 2.25D),
    BLUE("blue_arrow", 2.25D),
    QUETZAL("quetzal_arrow", 3.0D);

    private final String id;
    private final double baseDamage;
    private final ResourceLocation entityTexture;

    GTArrowVariant(String id, double baseDamage) {
        this.id = id;
        this.baseDamage = baseDamage;
        this.entityTexture = ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, "textures/entity/projectiles/" + id + ".png");
    }

    public String getId() {
        return this.id;
    }

    public double getBaseDamage() {
        return this.baseDamage;
    }

    public ResourceLocation getEntityTexture() {
        return this.entityTexture;
    }

    // Looked up lazily in the item registry, safe to call once registries are filled
    public Item getItem() {
        return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, this.id));
    }

    public static GTArrowVariant byId(String id) {
        for (GTArrowVariant variant : values()) {
            if (variant.id.equals(id)) {
                return variant;
            }
        }
        return RED;
    }

    public static GTArrowVariant byOrdinal(int ordinal) {
        GTArrowVariant[] values = values();
        return values[Math.floorMod(ordinal, values.length)];
    }
}
