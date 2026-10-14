package org.dawnoftime.gardentrails.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

// Applied while a quetzal sits on the player shoulder, same speed bonus as Speed I
public class QuetzalTrustEffect extends MobEffect {
    public QuetzalTrustEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x2FB57A);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "6b0c2a5e-3f1d-4c8e-9a47-5d1e8b7c2f90", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
