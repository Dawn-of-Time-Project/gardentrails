package org.dawnoftime.gardentrails.block.templates;

import org.dawnoftime.gardentrails.util.RegistrationContext;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;

import javax.annotation.Nullable;

/**
 * A plant inside a vanilla flower pot. The vanilla constructor registers this block as the potted version of the plant
 * (on Forge it calls addPlant on the empty pot), so placing the plant item on an empty pot, taking it back with an empty
 * hand and the pot loot table all work without any custom interaction code.
 */
public class PottedPlantBlock extends FlowerPotBlock implements IBiomeColoredBlock {
    @Nullable
    private final ColorType colorType;

    public PottedPlantBlock(Block plant, @Nullable ColorType colorType) {
        super(plant, RegistrationContext.copy(Blocks.FLOWER_POT));
        this.colorType = colorType;
    }

    @Nullable
    @Override
    public ColorType getColorType() {
        return this.colorType;
    }
}
