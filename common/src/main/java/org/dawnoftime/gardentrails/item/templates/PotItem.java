package org.dawnoftime.gardentrails.item.templates;

import net.minecraft.world.item.Item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * Item without a block that can still be put in an empty vanilla flower pot (grape seeds).
 * Vanilla pots only take block items, so the swap to the pot block is done here.
 */
public class PotItem extends ItemGT {
    private final Supplier<Block> potBlock;

    public PotItem(Properties properties, String loreKey, Supplier<Block> potBlock) {
        super(properties, new String[]{loreKey, PottablePlantItem.POT_LORE_KEY});
        this.potBlock = potBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.FLOWER_POT)) {
            if (!world.isClientSide()) {
                Player player = context.getPlayer();
                if (player == null || !player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
                world.setBlock(pos, this.potBlock.get().defaultBlockState(), 2);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
