package org.dawnoftime.gardentrails.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.dawnoftime.gardentrails.mixin.accessor.CropBlockAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.dawnoftime.gardentrails.FlowerPlanterBlockEntityNeoForge;
import org.dawnoftime.gardentrails.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public FlowerPlanterBlockEntity createFlowerPlanterBlockEntity(BlockPos pos, BlockState state) {
        return new FlowerPlanterBlockEntityNeoForge(pos, state);
    }

    @Override
    public float getGrowthSpeed(BlockState state, BlockGetter level, BlockPos pos) {
        return CropBlockAccessor.callGetGrowthSpeed(state, level, pos);
    }
}
