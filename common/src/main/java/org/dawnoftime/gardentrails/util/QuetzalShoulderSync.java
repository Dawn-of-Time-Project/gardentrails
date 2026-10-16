package org.dawnoftime.gardentrails.util;

import net.minecraft.world.entity.player.Player;

/** Loader side storage of the shoulder state: a synced data attachment on the player (bit 1 left, bit 2 right). */
public abstract class QuetzalShoulderSync {
    public static QuetzalShoulderSync INSTANCE;

    public abstract int getMask(Player player);

    public abstract void setMask(Player player, int mask);
}
