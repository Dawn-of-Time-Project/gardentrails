package org.dawnoftime.gardentrails.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.dawnoftime.gardentrails.entity.GTArrowEntity;
import org.dawnoftime.gardentrails.entity.GTArrowVariant;
import org.dawnoftime.gardentrails.registry.GTItemsRegistry;
import org.jetbrains.annotations.NotNull;

public class GTDispenserBehaviors {

    public static void init() {
        // Same behavior as the vanilla bone meal dispenser
        DispenserBlock.registerBehavior(GTItemsRegistry.INSTANCE.GRAINS_AND_SEEDS_MIX.get(), new OptionalDispenseItemBehavior() {
            @Override
            protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
                this.setSuccess(true);
                Level level = source.getLevel();
                BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                if (!BoneMealItem.growCrop(stack, level, pos) && !BoneMealItem.growWaterPlant(stack, level, pos, null)) {
                    this.setSuccess(false);
                } else if (!level.isClientSide) {
                    level.levelEvent(1505, pos, 0);
                }
                return stack;
            }
        });

        // Same behavior as the vanilla arrow dispenser, one per variant
        for (GTArrowVariant variant : GTArrowVariant.values()) {
            DispenserBlock.registerBehavior(variant.getItem(), new AbstractProjectileDispenseBehavior() {
                @Override
                protected @NotNull Projectile getProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack stack) {
                    GTArrowEntity arrow = new GTArrowEntity(level, position.x(), position.y(), position.z(), variant);
                    arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                    return arrow;
                }
            });
        }
    }
}
