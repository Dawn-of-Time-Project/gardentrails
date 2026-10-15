package org.dawnoftime.gardentrails.registry;

import net.minecraft.world.effect.MobEffect;

import net.minecraft.core.Holder;
import org.dawnoftime.gardentrails.effect.QuetzalTrustEffect;
import org.dawnoftime.gardentrails.effect.ReunitedBrothersEffect;

import java.util.function.Supplier;

public abstract class GTMobEffectsRegistry {
    public static GTMobEffectsRegistry INSTANCE;

    public final Supplier<Holder<MobEffect>> QUETZAL_TRUST = register("quetzal_trust", QuetzalTrustEffect::new);
    public final Supplier<Holder<MobEffect>> REUNITED_BROTHERS = register("reunited_brothers", ReunitedBrothersEffect::new);

    public abstract Supplier<Holder<MobEffect>> register(String name, Supplier<MobEffect> effect);
}
