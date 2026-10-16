package org.dawnoftime.gardentrails.registry;

import org.dawnoftime.gardentrails.util.RegistrationContext;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.dawnoftime.gardentrails.entity.GTArrowVariant;
import org.dawnoftime.gardentrails.item.DrinkItem;
import org.dawnoftime.gardentrails.item.GTArrowItem;
import org.dawnoftime.gardentrails.item.GrainsAndSeedsMixItem;
import org.dawnoftime.gardentrails.item.SakeItem;
import org.dawnoftime.gardentrails.item.IHasFlowerPot;
import org.dawnoftime.gardentrails.item.templates.ItemGT;
import org.dawnoftime.gardentrails.item.templates.PotItem;
import org.dawnoftime.gardentrails.util.Foods;

import java.util.function.Supplier;

@SuppressWarnings({"unused"})
public abstract class GTItemsRegistry {
    public static GTItemsRegistry INSTANCE;

    // General
    public final Supplier<Item> GARDEN_TRAILS = register("garden_trails", ItemGT::new);
    public final Supplier<Item> SILK_WORMS = register("silk_worms", () -> new ItemGT(RegistrationContext.item(), "lore.gardentrails.silk_worms", "block.gardentrails.stick_bundle"));
    public final Supplier<Item> SILK_WORMS_HATCHERY = register("silk_worm_hatchery", () -> new ItemGT(RegistrationContext.item(), "lore.gardentrails.silk_worm_hatchery", "lore.gardentrails.name.drying_tray"));
    public final Supplier<Item> SILK_WORM_EGGS = register("silk_worm_eggs", () -> new ItemGT(RegistrationContext.item(), "lore.gardentrails.silk_worm_eggs", "item.gardentrails.mulberry_leaves"));
    public final Supplier<Item> SILK_COCOONS = register("silk_cocoons", () -> new ItemGT(RegistrationContext.item(), "lore.gardentrails.silk_cocoons", "item.gardentrails.silk"));
    public final Supplier<Item> SILK = register("silk", () -> new ItemGT(RegistrationContext.item().rarity(Rarity.EPIC), "lore.gardentrails.silk", null));
    public final Supplier<Item> MOLD = register("mold", () -> ItemGT.sakeProcess(RegistrationContext.item(), "lore.gardentrails.mold", "item.minecraft.brush", "block.gardentrails.rotten_oak_log", "item.gardentrails.cooked_rice", "item.gardentrails.moldy_rice"));
    public final Supplier<Item> BLUE_FEATHER = register("blue_feather", ItemGT::new);
    public final Supplier<Item> GREEN_FEATHER = register("green_feather", ItemGT::new);
    public final Supplier<Item> RED_FEATHER = register("red_feather", ItemGT::new);
    // Arrows: damage per variant is defined in GTArrowVariant
    public final Supplier<Item> RED_ARROW = register("red_arrow", () -> new GTArrowItem(RegistrationContext.item(), GTArrowVariant.RED, "lore.gardentrails.feathered_arrow"));
    public final Supplier<Item> GREEN_ARROW = register("green_arrow", () -> new GTArrowItem(RegistrationContext.item(), GTArrowVariant.GREEN, "lore.gardentrails.feathered_arrow"));
    public final Supplier<Item> BLUE_ARROW = register("blue_arrow", () -> new GTArrowItem(RegistrationContext.item(), GTArrowVariant.BLUE, "lore.gardentrails.feathered_arrow"));
    public final Supplier<Item> QUETZAL_ARROW = register("quetzal_arrow", () -> new GTArrowItem(RegistrationContext.item().rarity(Rarity.UNCOMMON), GTArrowVariant.QUETZAL, "lore.gardentrails.quetzal_arrow"));
    public final Supplier<Item> TEA_LEAVES = register("tea_leaves", ItemGT::new);
    public final Supplier<Item> CAMELLIA_LEAVES = register("camellia_leaves", ItemGT::new);
    public final Supplier<Item> GRAINS_AND_SEEDS_MIX = register("grains_and_seeds_mix", () -> new GrainsAndSeedsMixItem(RegistrationContext.item()));
    public final Supplier<Item> BLUE_PETALS = register("blue_petals", ItemGT::new);
    public final Supplier<Item> QUETZAL_OFFERING = register("quetzal_offering", () -> new ItemGT(RegistrationContext.item().rarity(Rarity.UNCOMMON), new String[]{"lore.gardentrails.quetzal_offering", "lore.gardentrails.quetzal_offering.bond"}));
    public final Supplier<Item> MULBERRY_LEAVES =register("mulberry_leaves", () -> new ItemGT(RegistrationContext.item(), "lore.gardentrails.mulberry_leaves", "item.gardentrails.silk_worm_eggs"));
    public final Supplier<Item> GRAPE = register("grape", () -> new ItemGT(Foods.GRAPE.applyTo(RegistrationContext.item())));
    public final Supplier<Item> DRIED_MAIZE = register("dried_maize", () -> new ItemGT(Foods.DRIED_MAIZE.applyTo(RegistrationContext.item())));
    public final Supplier<Item> DRIED_GRAPE = register("dried_grape", () -> new ItemGT(Foods.DRIED_GRAPE.applyTo(RegistrationContext.item())));
    public final Supplier<Item> COOKED_RICE = register("cooked_rice", () -> ItemGT.sakeProcess(Foods.COOKED_RICE.applyTo(RegistrationContext.item()), "lore.gardentrails.cooked_rice", "item.gardentrails.mold", "item.gardentrails.moldy_rice", "block.gardentrails.sake_vat"));
    public final Supplier<Item> WASHED_RICE = register("washed_rice", () -> ItemGT.sakeProcess(RegistrationContext.item(), "lore.gardentrails.washed_rice", "block.minecraft.furnace", "item.gardentrails.cooked_rice"));
    public final Supplier<Item> MOLDY_RICE = register("moldy_rice", () -> ItemGT.sakeProcess(RegistrationContext.item(), "lore.gardentrails.moldy_rice", "lore.gardentrails.name.drying_tray", "item.gardentrails.fermented_rice"));
    public final Supplier<Item> FERMENTED_RICE = register("fermented_rice", () -> ItemGT.sakeProcess(Foods.FERMENTED_RICE.applyTo(RegistrationContext.item()), "lore.gardentrails.fermented_rice", "block.gardentrails.sake_vat"));
    public final Supplier<Item> CRUSHED_MAIZE = register("crushed_maize", ItemGT::new);
    public final Supplier<Item> CRUSHED_GRAPE = register("crushed_grape", ItemGT::new);
    public final Supplier<Item> MULBERRY_JUICE = register("mulberry_juice", () -> new DrinkItem(Foods.MULBERRY_JUICE.applyTo(RegistrationContext.item())));
    public final Supplier<Item> FERMENTED_MULBERRY_JUICE = register("fermented_mulberry_juice", () -> new DrinkItem(Foods.FERMENTED_MULBERRY_JUICE.applyTo(RegistrationContext.item())));
    // Sake: 60 s boost + 15 s debuff, UNCOMMON rarity (yellow name), returns an empty glass bottle
    public final Supplier<Item> SAKE = register("sake", () -> new SakeItem(sakeProperties(), MobEffects.SATURATION, 1200, MobEffects.SLOWNESS, 300, "lore.gardentrails.sake"));
    public final Supplier<Item> MULBERRY_FLAVORED_SAKE = register("mulberry_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.REGENERATION, 1200, MobEffects.MINING_FATIGUE, 300, "lore.gardentrails.mulberry_flavored_sake"));
    public final Supplier<Item> GRAPE_FLAVORED_SAKE = register("grape_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.SPEED, 1200, MobEffects.HUNGER, 300, "lore.gardentrails.grape_flavored_sake"));
    public final Supplier<Item> MAIZE_FLAVORED_SAKE = register("maize_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.STRENGTH, 1200, MobEffects.INSTANT_DAMAGE, 0, "lore.gardentrails.maize_flavored_sake"));
    public final Supplier<Item> TEA_FLAVORED_SAKE = register("tea_flavored_sake", () -> new SakeItem(sakeProperties(), MobEffects.HASTE, 1200, MobEffects.WEAKNESS, 300, "lore.gardentrails.tea_flavored_sake"));
    public Supplier<Item> GRAPE_SEEDS;

    private static Item.Properties sakeProperties() {
        return RegistrationContext.item().rarity(Rarity.UNCOMMON).stacksTo(16);
    }

    public void postRegister() {
        GRAPE_SEEDS = registerWithFlowerPot("grape_seeds", () -> new PotItem(Foods.GRAPE_SEEDS.applyTo(RegistrationContext.item()), "lore.gardentrails.grape_seeds"));
    }

    public abstract <T extends Item> Supplier<Item> register(final String name, final Supplier<T> itemSupplier);
    public abstract <T extends Item & IHasFlowerPot> Supplier<Item> registerWithFlowerPot(final String name, final Supplier<T> itemSupplier);
    public abstract <T extends Item & IHasFlowerPot> Supplier<Item> registerWithFlowerPot(final String plantName, final String seedName, final Supplier<T> itemSupplier);

}
