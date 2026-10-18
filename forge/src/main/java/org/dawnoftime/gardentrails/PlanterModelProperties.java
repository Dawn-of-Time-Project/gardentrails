package org.dawnoftime.gardentrails;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.data.ModelProperty;

/** Model data handed from the flower planter block entity to its baked model. */
public final class PlanterModelProperties {
    /** The potted block whose plant is drawn in the planter. */
    public static final ModelProperty<Block> POTTED = new ModelProperty<>();

    private PlanterModelProperties() {}
}
