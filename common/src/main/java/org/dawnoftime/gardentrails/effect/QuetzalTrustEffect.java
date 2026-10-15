package org.dawnoftime.gardentrails.effect;

import net.minecraft.world.effect.MobEffect;
import org.dawnoftime.gardentrails.GTCommon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

// Applied while a quetzal sits on the player shoulder, same speed bonus as Speed I
public class QuetzalTrustEffect extends MobEffect {
    public QuetzalTrustEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x2FB57A);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, "effect.quetzal_trust"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
