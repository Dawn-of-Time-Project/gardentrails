package org.dawnoftime.gardentrails.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

// Drinkable sake bottle: grants one boost and one debuff, returns an empty glass bottle.
// Drinking again while the boost is active extends the boost and escalates the penalties:
// 2nd drink adds nausea, 3rd adds blindness, 4th and beyond add Instant Damage II.
// The drink count is derived from the remaining boost time, so it fades as the player sobers up.
public class SakeItem extends Item {
    private static final int USE_DURATION = 32;
    private static final int NAUSEA_TICKS = 300;
    private static final int BLINDNESS_TICKS = 200;

    private final Holder<MobEffect> boost;
    private final int boostTicks;
    private final Holder<MobEffect> debuff;
    private final int debuffTicks;
    private final String loreKey;

    // Debuff ticks are ignored when the debuff is an instant effect (applied once, no duration)
    public SakeItem(Properties properties, Holder<MobEffect> boost, int boostTicks, Holder<MobEffect> debuff, int debuffTicks, String loreKey) {
        super(properties);
        this.boost = boost;
        this.boostTicks = boostTicks;
        this.debuff = debuff;
        this.debuffTicks = debuffTicks;
        this.loreKey = loreKey;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        Player player = entity instanceof Player p ? p : null;
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }
        if (!level.isClientSide()) {
            // Drink count of this sake: 1 + number of full boost durations still remaining
            MobEffectInstance active = entity.getEffect(this.boost);
            int drinkCount = 1;
            int duration = this.boostTicks;
            if (active != null && active.getAmplifier() == 0) {
                drinkCount += (active.getDuration() + this.boostTicks - 1) / this.boostTicks;
                duration += active.getDuration();
            }
            entity.addEffect(new MobEffectInstance(this.boost, duration, 0));
            if (this.debuff.value().isInstantaneous()) {
                this.debuff.value().applyInstantaneousEffect((ServerLevel) level, null, null, entity, 0, 1.0D);
            } else {
                entity.addEffect(new MobEffectInstance(this.debuff, this.debuffTicks, 0));
            }
            if (drinkCount >= 2) {
                entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, NAUSEA_TICKS, 0));
            }
            if (drinkCount >= 3) {
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, BLINDNESS_TICKS, 0));
            }
            if (drinkCount >= 4) {
                MobEffects.INSTANT_DAMAGE.value().applyInstantaneousEffect((ServerLevel) level, null, null, entity, 1, 1.0D);
            }
        }
        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        if (player == null || !player.getAbilities().instabuild) {
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (stack.isEmpty()) {
                return bottle;
            }
            if (player != null && !player.getInventory().add(bottle)) {
                player.drop(bottle, false, net.minecraft.util.Prediction.SERVER_ONLY);
            }
        }
        return stack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> adder, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, adder, flag);
        List<Component> tooltips = new ArrayList<>();
        TooltipHelper.lore(tooltips, this.loreKey);
        TooltipHelper.lore(tooltips, "lore.gardentrails.sake_stacking");
        tooltips.forEach(adder);
    }
}
