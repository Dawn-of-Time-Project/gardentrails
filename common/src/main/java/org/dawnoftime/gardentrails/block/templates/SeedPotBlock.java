package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.dawnoftime.gardentrails.block.IBlockSpecialDisplay;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.Blocks.FLOWER_POT;
import static org.dawnoftime.gardentrails.util.VoxelShapes.FLOWER_POT_SHAPE;

/**
 * Pot holding an item that has no block (grape seeds). Vanilla pots only accept block items,
 * so this one keeps its own interaction: an empty hand gives the seeds back and empties the pot.
 */
public class SeedPotBlock extends BlockGT implements IBlockSpecialDisplay {
    private final Supplier<Item> seeds;

    public SeedPotBlock(Supplier<Item> seeds) {
        super(Properties.copy(FLOWER_POT), FLOWER_POT_SHAPE);
        this.seeds = seeds;
    }

    public Item getSeeds() {
        return this.seeds.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult ray) {
        if (!world.isClientSide() && entity.getItemInHand(hand).isEmpty()) {
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.seeds.get()));
            world.setBlock(pos, Blocks.FLOWER_POT.defaultBlockState(), 2);
        }
        return super.use(state, world, pos, entity, hand, ray);
    }

    @Override
    public float getDisplayScale() {
        return 0.667F;
    }

    @Override
    protected boolean blocksLandPathing(final BlockState state) {
        return true;
    }
}
