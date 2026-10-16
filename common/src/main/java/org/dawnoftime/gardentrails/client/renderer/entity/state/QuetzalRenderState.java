package org.dawnoftime.gardentrails.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/** Frozen copy of the quetzal data read by the model. Also used by the shoulder layer with the shoulder fields set. */
public class QuetzalRenderState extends LivingEntityRenderState {
    /** Wing flap phase, same value as the vanilla parrot flap angle. */
    public float flapAngle;
    /** From 0 (on the ground) to 1 (flying). */
    public float flapSpeed;
    /** Beak opening from 0 (closed) to 1 (fully open). */
    public float beakOpen;
    public boolean onShoulder;
    public boolean leftShoulder;
}
