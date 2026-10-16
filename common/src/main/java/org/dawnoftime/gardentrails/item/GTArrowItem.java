package org.dawnoftime.gardentrails.item;

import net.minecraft.world.item.component.TooltipDisplay;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
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
    public @NotNull AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack stack, @NotNull LivingEntity shooter, @Nullable ItemStack weapon) {
        return new GTArrowEntity(level, shooter, this.variant, stack.copyWithCount(1), weapon);
    }

    // Called by the dispenser
    @Override
    public @NotNull Projectile asProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack stack, @NotNull Direction direction) {
        GTArrowEntity arrow = new GTArrowEntity(level, position.x(), position.y(), position.z(), this.variant, stack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    public GTArrowVariant getVariant() {
        return this.variant;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> adder, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, adder, flag);
        List<Component> tooltips = new ArrayList<>();
        TooltipHelper.lore(tooltips, this.loreKey);
        tooltips.forEach(adder);
    }
}
