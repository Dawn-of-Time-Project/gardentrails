package org.dawnoftime.gardentrails.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;

public interface IPlatformHelper {


    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);


    /** Creates the flower planter block entity, Forge returns a subclass that hands its content to the baked model. */
    FlowerPlanterBlockEntity createFlowerPlanterBlockEntity(BlockPos pos, BlockState state);
}