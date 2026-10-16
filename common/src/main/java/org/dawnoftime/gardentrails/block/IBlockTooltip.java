package org.dawnoftime.gardentrails.block;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Blocks no longer own a tooltip since 1.21.5, the tooltip belongs to the item.
 * A block implementing this interface provides the lines shown by its block item.
 */
public interface IBlockTooltip {
    void appendTooltip(List<Component> tooltip);
}
