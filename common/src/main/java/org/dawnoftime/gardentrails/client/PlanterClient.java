package org.dawnoftime.gardentrails.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;

import java.util.ArrayList;
import java.util.List;

/** Client side helpers of the flower planter, kept out of the block entity so the server never loads them. */
public final class PlanterClient {
    private static final int TINT_INDEX_COUNT = 3;

    private PlanterClient() {}

    /** Rebuilds the chunk section that holds the planter, its plant is part of the chunk mesh. */
    public static void refresh(BlockPos pos) {
        Minecraft.getInstance().levelRenderer.setSectionDirty(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getY()), SectionPos.blockToSectionCoord(pos.getZ()));
    }

    /** Block tints of the planter: the tint the potted block of its plant would get at this position, per tint index. */
    public static List<BlockTintSource> tintSources() {
        List<BlockTintSource> sources = new ArrayList<>();
        for (int tintIndex = 0; tintIndex < TINT_INDEX_COUNT; tintIndex++) {
            final int index = tintIndex;
            sources.add(new BlockTintSource() {
                @Override
                public int color(BlockState state) {
                    return -1;
                }

                @Override
                public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                    if (level.getBlockEntity(pos) instanceof FlowerPlanterBlockEntity planter && planter.getPotted() != null) {
                        BlockState potted = planter.getPotted().defaultBlockState();
                        BlockTintSource source = Minecraft.getInstance().getBlockColors().getTintSource(potted, index);
                        if (source != null) {
                            return source.colorInWorld(potted, level, pos);
                        }
                    }
                    return -1;
                }
            });
        }
        return sources;
    }
}