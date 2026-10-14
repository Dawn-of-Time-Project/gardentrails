package org.dawnoftime.gardentrails.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.dawnoftime.gardentrails.entity.GTArrowEntity;
import org.dawnoftime.gardentrails.entity.GTArrowVariant;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class GTArrowItem extends ArrowItem {
    private final GTArrowVariant variant;
    private final String loreKey;

    public GTArrowItem(Properties properties, GTArrowVariant variant, String loreKey) {
        super(properties);
        this.variant = variant;
        this.loreKey = loreKey;
    }

    // Called by the bow and the crossbow, Power and Punch are applied by them afterwards
    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        return new GTArrowEntity(level, shooter, this.variant);
    }

    public GTArrowVariant getVariant() {
        return this.variant;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltips, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltips, flag);
        TooltipHelper.lore(tooltips, this.loreKey);
    }
}
