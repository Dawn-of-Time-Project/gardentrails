package org.dawnoftime.gardentrails.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.dawnoftime.gardentrails.entity.GTArrowEntity;
import org.dawnoftime.gardentrails.entity.QuetzalEntity;
import org.dawnoftime.gardentrails.entity.SilkmothEntity;

import java.util.function.Supplier;

public abstract class GTEntitiesRegistry {
    public static GTEntitiesRegistry INSTANCE;
    public final Supplier<EntityType<SilkmothEntity>> SILKMOTH_ENTITY = register("silkmoth", () -> EntityType.Builder.<SilkmothEntity>of((type, world) -> new SilkmothEntity(world), MobCategory.AMBIENT).sized(0.3F, 0.3F));

    public final Supplier<EntityType<QuetzalEntity>> QUETZAL_ENTITY = register("quetzal", () -> EntityType.Builder.<QuetzalEntity>of(QuetzalEntity::new, MobCategory.CREATURE).sized(0.5F, 0.9F));

    public final Supplier<EntityType<GTArrowEntity>> FEATHERED_ARROW_ENTITY = register("feathered_arrow", () -> EntityType.Builder.<GTArrowEntity>of(GTArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

    public abstract <T extends Entity> Supplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder);
}