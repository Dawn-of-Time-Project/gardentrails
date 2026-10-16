package org.dawnoftime.gardentrails.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.dawnoftime.gardentrails.util.QuetzalShoulder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Shoulder entities only exist on the server player. Additive injector only, non-required.
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    // Highest fall distance (blocks) at which the quetzal stays on the shoulder, vanilla fall damage starts above it
    @Unique
    private static final float GT_MAX_FALL_DISTANCE = 3.0F;
    @Unique
    private boolean gt$inShoulderTick;

    @Inject(method = "handleShoulderEntities", at = @At("HEAD"), require = 0)
    private void gt$shoulderTickStart(CallbackInfo ci) {
        this.gt$inShoulderTick = true;
    }

    @Inject(method = "handleShoulderEntities", at = @At("RETURN"), require = 0)
    private void gt$shoulderTickEnd(CallbackInfo ci) {
        this.gt$inShoulderTick = false;
    }

    // Vanilla drops the shoulder birds from handleShoulderEntities after a fall of 0.5 block. Only that call is cancelled,
    // hits, death and the other callers still make the bird fall.
    @Inject(method = "removeEntitiesOnShoulder", at = @At("HEAD"), cancellable = true, require = 0)
    private void gt$keepQuetzalOnShoulder(CallbackInfo ci) {
        if (!this.gt$inShoulderTick) {
            return;
        }
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (QuetzalShoulder.isOnShoulder(player) && !player.isInWater() && !player.getAbilities().flying
                && !player.isSleeping() && !player.isInPowderSnow && player.fallDistance <= GT_MAX_FALL_DISTANCE) {
            ci.cancel();
        }
    }
}
