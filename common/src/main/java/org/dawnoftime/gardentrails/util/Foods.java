package org.dawnoftime.gardentrails.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class Foods {
    /**
     * Since 1.21.2 the eating behaviour (effects, duration, animation) is a Consumable component
     * and no longer part of FoodProperties, this record keeps both together.
     */
    public record Food(FoodProperties properties, Consumable consumable) {
        public Item.Properties applyTo(Item.Properties itemProperties) {
            return itemProperties.food(this.properties, this.consumable);
        }
    }

    private static final float FAST_SECONDS = 0.8F;

    private static Food food(int nutrition, float saturation, boolean fast, MobEffectInstance effect) {
        Consumable.Builder consumable = Consumable.builder();
        if (fast) {
            consumable.consumeSeconds(FAST_SECONDS);
        }
        if (effect != null) {
            consumable.onConsume(new ApplyStatusEffectsConsumeEffect(effect, 1.0f));
        }
        return new Food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build(), consumable.build());
    }

    private static Food drink(int nutrition, float saturation, MobEffectInstance effect) {
        Consumable consumable = Consumable.builder()
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEvents.GENERIC_DRINK)
                .hasConsumeParticles(false)
                .onConsume(new ApplyStatusEffectsConsumeEffect(effect, 1.0f))
                .build();
        return new Food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build(), consumable);
    }

    public static final Food GRAPE = food(3, 0.6f, false, new MobEffectInstance(MobEffects.SPEED, 100, 0));
    public static final Food MAIZE = food(3, 0.6f, false, new MobEffectInstance(MobEffects.STRENGTH, 100, 0));
    public static final Food MULBERRY = food(1, 0.5f, true, new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
    public static final Food MULBERRY_JUICE = drink(2, 0.4f, new MobEffectInstance(MobEffects.REGENERATION, 500, 0));
    public static final Food RICE = food(4, 0.6f, false, new MobEffectInstance(MobEffects.SLOWNESS, 100, 0));
    public static final Food GRAPE_SEEDS = food(1, 0.5f, true, null);
    public static final Food DRIED_MAIZE = food(6, 0.8f, false, new MobEffectInstance(MobEffects.STRENGTH, 900, 0));
    public static final Food DRIED_GRAPE = food(3, 0.6f, false, new MobEffectInstance(MobEffects.SPEED, 900, 0));
    public static final Food COOKED_RICE = food(7, 1.0f, false, null);
    public static final Food FERMENTED_RICE = food(9, 1.5f, false, new MobEffectInstance(MobEffects.SLOWNESS, 500, 0));
    public static final Food FERMENTED_MULBERRY_JUICE = drink(3, 0.6f, new MobEffectInstance(MobEffects.REGENERATION, 800, 0));
}
