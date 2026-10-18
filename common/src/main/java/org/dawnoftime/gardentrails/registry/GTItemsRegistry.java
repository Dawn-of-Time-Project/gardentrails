package org.dawnoftime.gardentrails.registry;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.dawnoftime.gardentrails.entity.GTArrowVariant;
import org.dawnoftime.gardentrails.item.DrinkItem;
import org.dawnoftime.gardentrails.item.GTArrowItem;
import org.dawnoftime.gardentrails.item.GrainsAndSeedsMixItem;
import org.dawnoftime.gardentrails.item.SakeItem;
import org.dawnoftime.gardentrails.item.templates.ItemGT;
import org.dawnoftime.gardentrails.item.templates.PotItem;
import org.dawnoftime.gardentrails.util.Foods;

import java.util.function.Supplier;

@SuppressWarnings({"unused"})
public abstract class GTItemsRegistry {
    public static GTItemsRegistry INSTANCE;

    public Supplier<Item> GARDEN_TRAILS;
    public Supplier<Item> SILK_WORMS;
    public Supplier<Item> SILK_WORMS_HATCHERY;
    public Supplier<Item> SILK_WORM_EGGS;
    public Supplier<Item> SILK_COCOONS;
    public Supplier<Item> SILK;
    public Supplier<Item> MOLD;
    public Supplier<Item> BLUE_FEATHER;
    public Supplier<Item> GREEN_FEATHER;
    public Supplier<Item> RED_FEATHER;
    public Supplier<Item> RED_ARROW;
    public Supplier<Item> GREEN_ARROW;
    public Supplier<Item> BLUE_ARROW;
    public Supplier<Item> ROTTEN_ARROW;
    public Supplier<Item> QUETZAL_ARROW;
    public Supplier<Item> TEA_LEAVES;
    public Supplier<Item> CAMELLIA_LEAVES;
    public Supplier<Item> GRAINS_AND_SEEDS_MIX;
    public Supplier<Item> BLUE_PETALS;
    public Supplier<Item> QUETZAL_OFFERING;
    public Supplier<Item> MULBERRY_LEAVES;
    public Supplier<Item> GRAPE;
    public Supplier<Item> DRIED_MAIZE;
    public Supplier<Item> DRIED_GRAPE;
    public Supplier<Item> COOKED_RICE;
    public Supplier<Item> WASHED_RICE;
    public Supplier<Item> MOLDY_RICE;
    public Supplier<Item> FERMENTED_RICE;
    public Supplier<Item> CRUSHED_MAIZE;
    public Supplier<Item> CRUSHED_GRAPE;
    public Supplier<Item> MULBERRY_JUICE;
    public Supplier<Item> FERMENTED_MULBERRY_JUICE;
    public Supplier<Item> SAKE;
    public Supplier<Item> MULBERRY_FLAVORED_SAKE;
    public Supplier<Item> GRAPE_FLAVORED_SAKE;
    public Supplier<Item> MAIZE_FLAVORED_SAKE;
    public Supplier<Item> TEA_FLAVORED_SAKE;
    public Supplier<Item> GRAPE_SEEDS;
    public Supplier<Item> SILKMOTH_SPAWN_EGG;
    public Supplier<Item> QUETZAL_SPAWN_EGG;

    // General
    // Arrows: damage per variant is defined in GTArrowVariant
    // Sake: 60 s boost + 15 s debuff, UNCOMMON rarity (yellow name), returns an empty glass bottle

    private static Item.Properties sakeProperties() {
        return new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(16);
    }

    /**
     * Registers every item. Each loader calls it right after assigning {@link #INSTANCE}.
     */
    public final void registerAll() {
        GARDEN_TRAILS = register("garden_trails", ItemGT::new);
        SILK_WORMS = register("silk_worms", () -> new ItemGT(new Item.Properties(), "lore.gardentrails.silk_worms", "block.gardentrails.stick_bundle"));
        SILK_WORMS_HATCHERY = register("silk_worm_hatchery", () -> new ItemGT(new Item.Properties(), "lore.gardentrails.silk_worm_hatchery", "lore.gardentrails.name.drying_tray"));
        SILK_WORM_EGGS = register("silk_worm_eggs", () -> new ItemGT(new Item.Properties(), "lore.gardentrails.silk_worm_eggs", "item.gardentrails.mulberry_leaves"));
        SILK_COCOONS = register("silk_cocoons", () -> new ItemGT(new Item.Properties(), "lore.gardentrails.silk_cocoons", "item.gardentrails.silk"));
        SILK = register("silk", () -> new ItemGT(new Item.Properties().rarity(Rarity.EPIC), "lore.gardentrails.silk", null));
        MOLD = register("mold", () -> ItemGT.sakeProcess(new Item.Properties(), "lore.gardentrails.mold", "item.minecraft.brush", "block.gardentrails.rotten_oak_log", "item.gardentrails.cooked_rice", "item.gardentrails.moldy_rice"));
        BLUE_FEATHER = register("blue_feather", ItemGT::new);
        GREEN_FEATHER = register("green_feather", ItemGT::new);
        RED_FEATHER = register("red_feather", ItemGT::new);
        RED_ARROW = register("red_arrow", () -> new GTArrowItem(new Item.Properties(), GTArrowVariant.RED, "lore.gardentrails.feathered_arrow"));
        GREEN_ARROW = register("green_arrow", () -> new GTArrowItem(new Item.Properties(), GTArrowVariant.GREEN, "lore.gardentrails.feathered_arrow"));
        BLUE_ARROW = register("blue_arrow", () -> new GTArrowItem(new Item.Properties(), GTArrowVariant.BLUE, "lore.gardentrails.feathered_arrow"));
        ROTTEN_ARROW = register("rotten_arrow", () -> new GTArrowItem(new Item.Properties(), GTArrowVariant.ROTTEN, "lore.gardentrails.rotten_arrow"));
        QUETZAL_ARROW = register("quetzal_arrow", () -> new GTArrowItem(new Item.Properties().rarity(Rarity.UNCOMMON), GTArrowVariant.QUETZAL, "lore.gardentrails.quetzal_arrow"));
        TEA_LEAVES = register("tea_leaves", ItemGT::new);
        CAMELLIA_LEAVES = register("camellia_leaves", ItemGT::new);
        GRAINS_AND_SEEDS_MIX = register("grains_and_seeds_mix", () -> new GrainsAndSeedsMixItem(new Item.Properties()));
        BLUE_PETALS = register("blue_petals", ItemGT::new);
        QUETZAL_OFFERING = register("quetzal_offering", () -> new ItemGT(new Item.Properties().rarity(Rarity.UNCOMMON), new String[]{"lore.gardentrails.quetzal_offering", "lore.gardentrails.quetzal_offering.bond"}));
        MULBERRY_LEAVES = register("mulberry_leaves", () -> new ItemGT(new Item.Properties(), "lore.gardentrails.mulberry_leaves", "item.gardentrails.silk_worm_eggs"));
        GRAPE = register("grape", () -> new ItemGT(new Item.Properties().food(Foods.GRAPE)));
        DRIED_MAIZE = register("dried_maize", () -> new ItemGT(new Item.Properties().food(Foods.DRIED_MAIZE)));
        DRIED_GRAPE = register("dried_grape", () -> new ItemGT(new Item.Properties().food(Foods.DRIED_GRAPE)));
        COOKED_RICE = register("cooked_rice", () -> ItemGT.sakeProcess(new Item.Properties().food(Foods.COOKED_RICE), "lore.gardentrails.cooked_rice", "item.gardentrails.mold", "item.gardentrails.moldy_rice", "block.gardentrails.sake_vat"));
        WASHED_RICE = register("washed_rice", () -> ItemGT.sakeProcess(new Item.Properties(), "lore.gardentrails.washed_rice", "block.minecraft.furnace", "item.gardentrails.cooked_rice"));
        MOLDY_RICE = register("moldy_rice", () -> ItemGT.sakeProcess(new Item.Properties(), "lore.gardentrails.moldy_rice", "lore.gardentrails.name.drying_tray", "item.gardentrails.fermented_rice"));
        FERMENTED_RICE = register("fermented_rice", () -> ItemGT.sakeProcess(new Item.Properties().food(Foods.FERMENTED_RICE), "lore.gardentrails.fermented_rice", "block.gardentrails.sake_vat"));
        CRUSHED_MAIZE = register("crushed_maize", ItemGT::new);
        CRUSHED_GRAPE = register("crushed_grape", ItemGT::new);
        MULBERRY_JUICE = register("mulberry_juice", () -> new DrinkItem(new Item.Properties().food(Foods.MULBERRY_JUICE)));
        FERMENTED_MULBERRY_JUICE = register("fermented_mulberry_juice", () -> new DrinkItem(new Item.Properties().food(Foods.FERMENTED_MULBERRY_JUICE)));
        SAKE = register("sake", () -> new SakeItem(sakeProperties(), MobEffects.SATURATION, 1200, MobEffects.MOVEMENT_SLOWDOWN, 300, "lore.gardentrails.sake"));
        MULBERRY_FLAVORED_SAKE = register("mulberry_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.REGENERATION, 1200, MobEffects.DIG_SLOWDOWN, 300, "lore.gardentrails.mulberry_flavored_sake"));
        GRAPE_FLAVORED_SAKE = register("grape_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.MOVEMENT_SPEED, 1200, MobEffects.HUNGER, 300, "lore.gardentrails.grape_flavored_sake"));
        MAIZE_FLAVORED_SAKE = register("maize_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.DAMAGE_BOOST, 1200, MobEffects.HARM, 0, "lore.gardentrails.maize_flavored_sake"));
        TEA_FLAVORED_SAKE = register("tea_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.DIG_SPEED, 1200, MobEffects.WEAKNESS, 300, "lore.gardentrails.tea_flavored_sake"));
        GRAPE_SEEDS = register("grape_seeds", () -> new PotItem(new Item.Properties().food(Foods.GRAPE_SEEDS), "lore.gardentrails.grape_seeds", () -> GTBlocksRegistry.INSTANCE.GRAPE_SEEDS_POT.get()));
        SILKMOTH_SPAWN_EGG = register("silkmoth_spawn_egg", () -> createSpawnEgg(GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY, 0xDBD8BD, 0xFEFEFC, new Item.Properties()));
        QUETZAL_SPAWN_EGG = register("quetzal_spawn_egg", () -> createSpawnEgg(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY, 0xFFFFFF, 0xFFFFFF, new Item.Properties().rarity(Rarity.UNCOMMON)));
    }

    /** Spawn eggs are built by the loader, Forge has its own spawn egg class. */
    protected abstract Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);
    public abstract <T extends Item> Supplier<Item> register(final String name, final Supplier<T> itemSupplier);

}
