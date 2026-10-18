package org.dawnoftime.gardentrails;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.dawnoftime.gardentrails.blockentity.FlowerPlanterBlockEntity;
import org.jetbrains.annotations.NotNull;

/** Forge flavour of the flower planter block entity: gives the plant to the baked model through the model data. */
public class FlowerPlanterBlockEntityForge extends FlowerPlanterBlockEntity {
    public FlowerPlanterBlockEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @NotNull ModelData getModelData() {
        Block potted = this.getPotted();
        return potted == null ? ModelData.EMPTY : ModelData.builder().with(PlanterModelProperties.POTTED, potted).build();
    }

    @Override
    protected void onContentChangedOnClient() {
        this.requestModelDataUpdate();
        super.onContentChangedOnClient();
    }
}
