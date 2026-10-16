package org.dawnoftime.gardentrails.mixin;

import net.minecraft.world.effect.MobEffect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.dawnoftime.gardentrails.registry.GTMobEffectsRegistry;
import org.dawnoftime.gardentrails.registry.GTTags;
import org.dawnoftime.gardentrails.util.QuetzalShoulder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Quetzal shoulder bonus. All injectors are additive (no redirect) and non-required, so other mods touching
// the same methods cannot conflict with them or crash the game.
@Mixin(Player.class)
public class PlayerMixin {
    // Duration of the quetzal effects (5 s) and the remaining time (1 s) at which they are renewed
    @Unique
    private static final int GT_EFFECT_TICKS = 100;
    @Unique
    private static final int GT_EFFECT_RENEW_TICKS = 20;

    @Inject(method = "aiStep", at = @At("RETURN"), require = 0)
    private void gt$aiStepTail(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        // The player is connected here, so the synced copy of the shoulders can be updated safely
        QuetzalShoulder.sync(serverPlayer);
        if (!QuetzalShoulder.isOnShoulder(serverPlayer)) {
            // The bird left the shoulder, the bonus stops right away instead of running out
            gt$removeQuetzalEffects(player, null);
            return;
        }
        gt$applyQuetzalBonus(player);
        // Crouching makes the birds come down (vanilla keeps its 1 second guard after the bird lands on the shoulder)
        if (player.isShiftKeyDown()) {
            ((PlayerShoulderAccessor) player).gt$removeEntitiesOnShoulder();
        }
    }

    // Quetzal trust, or reunited brothers (speed + 20 percent less damage, see LivingEntityMixin) when the chestplate of the quetzal bond tag is worn.
    // The effect lasts 5 seconds and is renewed when 1 second is left, so the timer loops from 5 to 1 and never drops to 0.
    @Unique
    private static void gt$applyQuetzalBonus(Player player) {
        boolean bonded = player.getItemBySlot(EquipmentSlot.CHEST).is(GTTags.INSTANCE.QUETZAL_BOND_CHEST);
        Holder<MobEffect> wanted = bonded ? GTMobEffectsRegistry.INSTANCE.REUNITED_BROTHERS.get() : GTMobEffectsRegistry.INSTANCE.QUETZAL_TRUST.get();
        gt$removeQuetzalEffects(player, wanted);
        MobEffectInstance current = player.getEffect(wanted);
        if (current == null || current.getDuration() <= GT_EFFECT_RENEW_TICKS) {
            player.addEffect(new MobEffectInstance(wanted, GT_EFFECT_TICKS, 0, true, false, true));
        }
    }

    // Removes the quetzal effects except the kept one (null keeps none)
    @Unique
    private static void gt$removeQuetzalEffects(Player player, Holder<MobEffect> kept) {
        Holder<MobEffect> trust = GTMobEffectsRegistry.INSTANCE.QUETZAL_TRUST.get();
        Holder<MobEffect> brothers = GTMobEffectsRegistry.INSTANCE.REUNITED_BROTHERS.get();
        if (kept != trust && player.hasEffect(trust)) {
            player.removeEffect(trust);
        }
        if (kept != brothers && player.hasEffect(brothers)) {
            player.removeEffect(brothers);
        }
    }
}
