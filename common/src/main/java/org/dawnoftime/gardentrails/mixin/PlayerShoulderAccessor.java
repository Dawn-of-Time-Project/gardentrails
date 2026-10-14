package org.dawnoftime.gardentrails.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Gives access to the protected shoulder setter, used by the quetzal to choose its shoulder
@Mixin(Player.class)
public interface PlayerShoulderAccessor {
    @Invoker("setShoulderEntityLeft")
    void gt$setShoulderEntityLeft(CompoundTag tag);

    @Invoker("removeEntitiesOnShoulder")
    void gt$removeEntitiesOnShoulder();
}
