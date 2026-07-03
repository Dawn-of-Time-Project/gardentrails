package org.dawnoftime.gardentrails.block.japanese;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record MapleVariant(String id, Supplier<Block> trunk, Supplier<Block> leaves, Supplier<Block> sapling) {
}
