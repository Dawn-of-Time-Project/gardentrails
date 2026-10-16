package org.dawnoftime.gardentrails.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// The shoulder entity data only exists on the server player. Gives access to the protected shoulder setter, used by the quetzal to choose its shoulder
@Mixin(ServerPlayer.class)
public interface PlayerShoulderAccessor {
    @Invoker("setShoulderEntityLeft")
    void gt$setShoulderEntityLeft(CompoundTag tag);

    @Invoker("removeEntitiesOnShoulder")
    void gt$removeEntitiesOnShoulder();
}
