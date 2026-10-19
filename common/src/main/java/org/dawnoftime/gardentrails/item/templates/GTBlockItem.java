package org.dawnoftime.gardentrails.item.templates;

import org.dawnoftime.gardentrails.block.IBlockTooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Default block item, forwards the tooltip of blocks implementing IBlockTooltip.
 */
public class GTBlockItem extends BlockItem {
    public GTBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> adder, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, adder, flag);
        TooltipHelper.appendBlockTooltip(this.getBlock(), adder);
    }
}
