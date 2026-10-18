package org.dawnoftime.gardentrails.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.dawnoftime.gardentrails.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public FlowerPlanterBlockEntity createFlowerPlanterBlockEntity(BlockPos pos, BlockState state) {
        return new FlowerPlanterBlockEntity(pos, state);
    }
}
