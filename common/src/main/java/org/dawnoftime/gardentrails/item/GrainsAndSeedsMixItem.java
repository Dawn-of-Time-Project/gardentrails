package org.dawnoftime.gardentrails.item;

import org.dawnoftime.gardentrails.util.TooltipHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.dawnoftime.gardentrails.GTCommon;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Works exactly like bone meal on plants, and doubles the vanilla parrot taming chance (1/5 instead of 1/10)
 * on every tameable entity listed in the gardentrails:tameable_birds entity type tag.
 */
public class GrainsAndSeedsMixItem extends BoneMealItem {
    public static final TagKey<EntityType<?>> TAMEABLE_BIRDS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(GTCommon.MOD_ID, "tameable_birds"));
    private static final int TAME_CHANCE = 5;

    public GrainsAndSeedsMixItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if (target instanceof TamableAnimal animal && animal.getType().is(TAMEABLE_BIRDS) && !animal.isTame()) {
            Level level = animal.level();
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.playSound(null, animal.getX(), animal.getY(), animal.getZ(), SoundEvents.PARROT_EAT, animal.getSoundSource(), 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F);
            if (!level.isClientSide) {
                if (level.random.nextInt(TAME_CHANCE) == 0) {
                    animal.tame(player);
                    level.broadcastEntityEvent(animal, (byte) 7);
                } else {
                    level.broadcastEntityEvent(animal, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltips, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltips, flag);
        TooltipHelper.lore(tooltips, "lore.gardentrails.grains_and_seeds_mix");
    }
}
