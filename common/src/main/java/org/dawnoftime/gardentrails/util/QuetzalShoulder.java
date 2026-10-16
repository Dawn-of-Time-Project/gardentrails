package org.dawnoftime.gardentrails.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.dawnoftime.gardentrails.GTCommon;

/**
 * Detects a quetzal sitting on one of the two player shoulders.
 * On the server the shoulder NBT of the player is the only source of truth. The clients cannot read it, so it is
 * copied into a synced attachment (see QuetzalShoulderSync) once per tick by {@link #sync}, never while the player data is loading.
 */
public class QuetzalShoulder {
    private static final String QUETZAL_ID = GTCommon.MOD_ID + ":quetzal";
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    /** Client side display: reads the synced copy. */
    public static boolean isOnLeft(Player player) {
        return (copiedMask(player) & LEFT) != 0;
    }

    /** Client side display: reads the synced copy. */
    public static boolean isOnRight(Player player) {
        return (copiedMask(player) & RIGHT) != 0;
    }

    /** Server side logic: reads the shoulder NBT. */
    public static boolean isOnShoulder(ServerPlayer player) {
        return maskFromNbt(player) != 0;
    }

    /** Updates the synced copy if the shoulders changed. Call it only on a connected server player (a tick). */
    public static void sync(ServerPlayer player) {
        if (QuetzalShoulderSync.INSTANCE == null) {
            return;
        }
        int wanted = maskFromNbt(player);
        if (wanted != copiedMask(player)) {
            QuetzalShoulderSync.INSTANCE.setMask(player, wanted);
        }
    }

    public static boolean isQuetzal(CompoundTag tag) {
        return !tag.isEmpty() && tag.getString("id").map(QUETZAL_ID::equals).orElse(false);
    }

    private static int maskFromNbt(ServerPlayer player) {
        return (isQuetzal(player.getShoulderEntityLeft()) ? LEFT : 0) | (isQuetzal(player.getShoulderEntityRight()) ? RIGHT : 0);
    }

    private static int copiedMask(Player player) {
        return QuetzalShoulderSync.INSTANCE == null ? 0 : QuetzalShoulderSync.INSTANCE.getMask(player);
    }
}
