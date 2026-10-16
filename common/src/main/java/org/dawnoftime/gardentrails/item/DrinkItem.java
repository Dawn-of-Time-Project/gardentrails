package org.dawnoftime.gardentrails.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import org.dawnoftime.gardentrails.item.templates.ItemGT;
import org.jetbrains.annotations.NotNull;

public class DrinkItem extends ItemGT {

    public DrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }
}
