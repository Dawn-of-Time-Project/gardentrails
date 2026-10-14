package org.dawnoftime.gardentrails.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.dawnoftime.gardentrails.registry.GTMobEffectsRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Damage reduction of the reunited brothers effect, same value and same place in the damage calculation as Resistance I
// (after armor, together with the other effects). It is done here and not with the vanilla Resistance effect so no second effect icon shows up.
// The injector is additive and non-required, so other mods touching the same method cannot conflict with it.
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    private static final float GT_REUNITED_BROTHERS_DAMAGE_FACTOR = 0.8F;

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true, require = 0)
    private void gt$reunitedBrothersResistance(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player && !source.is(DamageTypeTags.BYPASSES_RESISTANCE) && !source.is(DamageTypeTags.BYPASSES_EFFECTS)
                && entity.hasEffect(GTMobEffectsRegistry.INSTANCE.REUNITED_BROTHERS.get())) {
            cir.setReturnValue(cir.getReturnValue() * GT_REUNITED_BROTHERS_DAMAGE_FACTOR);
        }
    }
}
