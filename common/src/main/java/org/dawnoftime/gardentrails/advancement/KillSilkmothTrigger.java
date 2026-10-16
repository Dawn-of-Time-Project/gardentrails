package org.dawnoftime.gardentrails.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.dawnoftime.gardentrails.GTCommon;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class KillSilkmothTrigger extends SimpleCriterionTrigger<KillSilkmothTrigger.TriggerInstance> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "kill_silkmoth");

    // Per-player kill counts for this session. Resets on server restart which is acceptable.
    private static final Map<UUID, Integer> KILL_COUNTS = new HashMap<>();

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * Called when a player kills a silkmoth. Increments the kill count and fires
     * any criteria whose required count has now been reached.
     */
    public void trigger(ServerPlayer player) {
        int newCount = KILL_COUNTS.merge(player.getUUID(), 1, Integer::sum);
        this.trigger(player, instance -> instance.matches(newCount));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, int count) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.optionalFieldOf("count", 1).forGetter(TriggerInstance::count)
        ).apply(instance, TriggerInstance::new));

        public boolean matches(int currentCount) {
            return currentCount >= this.count;
        }
    }
}
