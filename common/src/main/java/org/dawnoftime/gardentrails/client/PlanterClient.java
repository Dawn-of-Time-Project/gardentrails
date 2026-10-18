package org.dawnoftime.gardentrails.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;

import javax.annotation.Nullable;

/** Client side helpers of the flower planter, kept out of the block entity so the server never loads them. */
public final class PlanterClient {
    private PlanterClient() {}

    /** Rebuilds the chunk section that holds the planter, its plant is part of the chunk mesh. */
    public static void refresh(BlockPos pos) {
        Minecraft.getInstance().levelRenderer.setSectionDirty(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ()));
    }

    /** Block tint of the planter: the tint the potted block of its plant would get at this position. */
    public static int blockColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
        if (level != null && pos != null && level.getBlockEntity(pos) instanceof FlowerPlanterBlockEntity planter && planter.getPotted() != null) {
            return Minecraft.getInstance().getBlockColors().getColor(planter.getPotted().defaultBlockState(), level, pos, tintIndex);
        }
        return -1;
    }
}
