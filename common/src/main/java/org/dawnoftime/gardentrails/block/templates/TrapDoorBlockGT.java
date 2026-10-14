package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

/**
 * Exposes the protected vanilla trapdoor constructor so trapdoors can be registered from the block registry
 */
public class TrapDoorBlockGT extends TrapDoorBlock {
    public TrapDoorBlockGT(Properties properties, BlockSetType type) {
        super(properties, type);
    }
}
