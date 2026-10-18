package org.dawnoftime.gardentrails.block.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Geometry of a maple tree: one trunk block topped by a 3x2x3 block of leaves.
 * The only place that knows the shape of the tree.
 */
public final class MapleTree {
    private MapleTree() {
    }

    /**
     * All leaf positions of the tree rooted at the given trunk.
     */
    public static Iterable<BlockPos> leafArea(final BlockPos trunkPos) {
        return BlockPos.betweenClosed(trunkPos.offset(-1, 1, -1), trunkPos.offset(1, 2, 1));
    }

    /**
     * Trunk position deduced from the multiblock indexes stored in a leaf state.
     */
    public static BlockPos trunkFromLeaf(final BlockPos leafPos, final BlockState leafState) {
        return leafPos.offset(
                1 - leafState.getValue(MapleLeavesBlock.MULTIBLOCK_X),
                -1 - leafState.getValue(MapleLeavesBlock.MULTIBLOCK_Y),
                1 - leafState.getValue(MapleLeavesBlock.MULTIBLOCK_Z));
    }

    /**
     * Leaf state for a given position of the tree, with its multiblock indexes relative to the trunk.
     */
    public static BlockState leafState(final Block leaves, final Direction facing, final BlockPos trunkPos, final BlockPos leafPos) {
        return leaves.defaultBlockState()
                .setValue(MapleTrunkBlock.FACING, facing)
                .setValue(MapleLeavesBlock.MULTIBLOCK_X, leafPos.getX() - trunkPos.getX() + 1)
                .setValue(MapleLeavesBlock.MULTIBLOCK_Y, leafPos.getY() - trunkPos.getY() - 1)
                .setValue(MapleLeavesBlock.MULTIBLOCK_Z, leafPos.getZ() - trunkPos.getZ() + 1);
    }
}
