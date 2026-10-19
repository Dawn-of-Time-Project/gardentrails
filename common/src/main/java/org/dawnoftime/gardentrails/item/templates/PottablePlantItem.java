package org.dawnoftime.gardentrails.item.templates;

import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;

import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/** Plant item that can also be put in a flower pot, which it tells in its tooltip. */
public class PottablePlantItem extends BlockItem {
    public static final String POT_LORE_KEY = "lore.gardentrails.can_be_potted";

    public PottablePlantItem(Block block, Properties properties) {
        super(block, properties);
    }

    /** Adds the pot line when a potted version of the block was registered. */
    public static void appendPotLore(Block plant, List<Component> tooltips) {
        if (GTBlocksRegistry.POTTABLE_PLANTS.contains(plant)) {
            TooltipHelper.lore(tooltips, POT_LORE_KEY);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> adder, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, adder, flag);
        List<Component> tooltips = new ArrayList<>();
        appendPotLore(this.getBlock(), tooltips);
        tooltips.forEach(adder);
    }
}
