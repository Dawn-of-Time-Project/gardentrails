package org.dawnoftime.gardentrails.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

// Replaces the quetzal trust while a quetzal sits on the shoulder of a player who wears a chestplate of the quetzal bond tag.
// Same speed bonus as the quetzal trust, the 20 percent damage reduction (like Resistance I) is applied by LivingEntityMixin because Resistance is not an attribute.
public class ReunitedBrothersEffect extends MobEffect {
    public ReunitedBrothersEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xD4A017);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "a3f5c8d1-7b2e-4e69-8c14-2d9f0b6a7e35", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
