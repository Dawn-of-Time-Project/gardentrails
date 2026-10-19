package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.dawnoftime.gardentrails.block.templates.BlockGT;

/**
 * Common base of the maple trunk and leaves: light corrections shared by the whole tree.
 */
public abstract class MapleMultiblockBlock extends BlockGT {
    protected MapleMultiblockBlock(final Properties properties) {
        super(properties.pushReaction(PushReaction.DESTROY));
    }

    @Override
    public boolean useShapeForLightOcclusion(final BlockState state) {
        return false;
    }

    @Override
    public float getShadeBrightness(final BlockState state, final BlockGetter level, final BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(final BlockState state) {
        return true;
    }
}
