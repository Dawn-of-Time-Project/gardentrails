package org.dawnoftime.gardentrails.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.dawnoftime.gardentrails.FlowerPlanterBlockEntityForge;
import org.dawnoftime.gardentrails.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public FlowerPlanterBlockEntity createFlowerPlanterBlockEntity(BlockPos pos, BlockState state) {
        return new FlowerPlanterBlockEntityForge(pos, state);
    }
}
