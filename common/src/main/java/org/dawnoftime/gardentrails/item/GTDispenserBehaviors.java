package org.dawnoftime.gardentrails.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
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
                ServerLevel level = source.level();
                BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                if (!BoneMealItem.growCrop(stack, level, pos) && !BoneMealItem.growWaterPlant(stack, level, pos, null)) {
                    this.setSuccess(false);
                } else if (!level.isClientSide()) {
                    level.levelEvent(1505, pos, 0);
                }
                return stack;
            }
        });

        // Same behavior as the vanilla arrow dispenser, the projectile comes from GTArrowItem#asProjectile
        for (GTArrowVariant variant : GTArrowVariant.values()) {
            DispenserBlock.registerBehavior(variant.getItem(), new ProjectileDispenseBehavior(variant.getItem()));
        }
    }
}
