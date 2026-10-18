package org.dawnoftime.gardentrails.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.DryerBlockEntity;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.dawnoftime.gardentrails.platform.Services;
import org.dawnoftime.gardentrails.blockentity.SakeVatBlockEntity;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class GTBlockEntitiesRegistry {
    public static GTBlockEntitiesRegistry INSTANCE;

    public final Supplier<BlockEntityType<DryerBlockEntity>> DRYER = register("dryer",
            DryerBlockEntity::new, () -> GTBlocksRegistry.INSTANCE.DRYING_TRAYS.values().stream().map(Supplier::get).toArray(Block[]::new));

    public final Supplier<BlockEntityType<SakeVatBlockEntity>> SAKE_VAT = register("sake_vat",
            SakeVatBlockEntity::new, () -> new Block[] { GTBlocksRegistry.INSTANCE.SAKE_VAT.get() });

    public final Supplier<BlockEntityType<FlowerPlanterBlockEntity>> FLOWER_PLANTER = register("flower_planter",
            Services.PLATFORM::createFlowerPlanterBlockEntity, () -> new Block[] { GTBlocksRegistry.INSTANCE.FLOWER_PLANTER.get() });

    public abstract <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BiFunction<BlockPos, BlockState, T> factoryIn, Supplier<Block[]> validBlocksSupplier);

}