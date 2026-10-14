package org.dawnoftime.gardentrails.registry;

import net.minecraft.world.effect.MobEffect;
import org.dawnoftime.gardentrails.effect.QuetzalTrustEffect;
import org.dawnoftime.gardentrails.effect.ReunitedBrothersEffect;

import java.util.function.Supplier;

public abstract class GTMobEffectsRegistry {
    public static GTMobEffectsRegistry INSTANCE;

    public final Supplier<MobEffect> QUETZAL_TRUST = register("quetzal_trust", QuetzalTrustEffect::new);
    public final Supplier<MobEffect> REUNITED_BROTHERS = register("reunited_brothers", ReunitedBrothersEffect::new);

    public abstract <T extends MobEffect> Supplier<T> register(String name, Supplier<T> effect);
}
