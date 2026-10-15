package org.dawnoftime.gardentrails.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.dawnoftime.gardentrails.GTCommon;

// Detects a quetzal sitting on one of the two player shoulders
public class QuetzalShoulder {
    private static final String QUETZAL_ID = GTCommon.MOD_ID + ":quetzal";

    public static boolean isOnShoulder(Player player) {
        return isQuetzal(player.getShoulderEntityLeft()) || isQuetzal(player.getShoulderEntityRight());
    }

    private static boolean isQuetzal(CompoundTag tag) {
        return !tag.isEmpty() && QUETZAL_ID.equals(tag.getString("id"));
    }
}
