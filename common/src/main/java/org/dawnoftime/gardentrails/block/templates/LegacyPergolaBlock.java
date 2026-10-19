package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties.ClimbingPlant;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The former blocks "metal_pergola_vine, _ivy and _grape", kept registered only so that existing worlds still load.
 * They have no item, no model and no loot table: each one is turned into the pergola block (same axes, same plant,
 * same age) when its chunk loads, see {@link #convertChunk}.
 */
public class LegacyPergolaBlock extends Block {
    private final Supplier<PergolaBlock> pergola;
    private final ClimbingPlant plant;

    public LegacyPergolaBlock(Properties properties, Supplier<PergolaBlock> pergola, ClimbingPlant plant) {
        super(properties);
        this.pergola = pergola;
        this.plant = plant;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(PergolaBlock.AXIS_X, false).setValue(PergolaBlock.AXIS_Y, false).setValue(PergolaBlock.AXIS_Z, false)
                .setValue(GTBlockStateProperties.AGE_6, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PergolaBlock.AXIS_X, PergolaBlock.AXIS_Y, PergolaBlock.AXIS_Z, GTBlockStateProperties.AGE_6);
    }

    public BlockState toPergola(BlockState legacyState) {
        return this.pergola.get().defaultBlockState()
                .setValue(PergolaBlock.AXIS_X, legacyState.getValue(PergolaBlock.AXIS_X))
                .setValue(PergolaBlock.AXIS_Y, legacyState.getValue(PergolaBlock.AXIS_Y))
                .setValue(PergolaBlock.AXIS_Z, legacyState.getValue(PergolaBlock.AXIS_Z))
                .setValue(PergolaBlock.CLIMBING_PLANT, this.plant)
                .setValue(PergolaBlock.AGE, legacyState.getValue(GTBlockStateProperties.AGE_6));
    }

    // Safety net: a legacy block that escaped the chunk conversion converts itself on a random tick
    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        level.setBlock(pos, this.toPergola(state), 2);
    }

    /** Replaces every legacy pergola of a freshly loaded chunk. Called by the chunk load event of each loader. */
    public static void convertChunk(ChunkAccess chunk) {
        LevelChunkSection[] sections = chunk.getSections();
        for (int index = 0; index < sections.length; index++) {
            LevelChunkSection section = sections[index];
            if (section.hasOnlyAir() || !section.maybeHas(state -> state.getBlock() instanceof LegacyPergolaBlock)) {
                continue;
            }
            int minY = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(index));
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state.getBlock() instanceof LegacyPergolaBlock legacy) {
                            BlockPos pos = new BlockPos(chunk.getPos().getMinBlockX() + x, minY + y, chunk.getPos().getMinBlockZ() + z);
                            chunk.setBlockState(pos, legacy.toPergola(state), 0);
                        }
                    }
                }
            }
        }
    }
}
