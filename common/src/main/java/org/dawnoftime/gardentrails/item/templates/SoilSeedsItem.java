package org.dawnoftime.gardentrails.item.templates;

import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;

import org.dawnoftime.gardentrails.util.RegistrationContext;
import org.dawnoftime.gardentrails.util.Foods;

import net.minecraft.world.item.Item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.block.templates.SoilCropsBlock;
import net.minecraft.network.chat.Component;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import javax.annotation.Nullable;

public class SoilSeedsItem extends BlockItem {
    // Optional sake process description, null for seeds outside the sake chain
    @Nullable private final String sakeDescKey;
    private final String[] sakeArgKeys;

    public <T extends SoilCropsBlock> SoilSeedsItem(T crops, @Nullable Foods.Food food) {
        this(crops, food, null);
    }

    public <T extends SoilCropsBlock> SoilSeedsItem(T crops, @Nullable Foods.Food food, @Nullable String sakeDescKey, String... sakeArgKeys) {
        super(crops, food != null ? food.applyTo(RegistrationContext.item()) : RegistrationContext.item());
        this.sakeDescKey = sakeDescKey;
        this.sakeArgKeys = sakeArgKeys;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> adder, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, adder, flag);
        List<Component> tooltips = new ArrayList<>();
        if (this.sakeDescKey != null) {
            TooltipHelper.process(tooltips, "lore.gardentrails.sake_process", this.sakeDescKey, this.sakeArgKeys);
        }
        PottablePlantItem.appendPotLore(this.getBlock(), tooltips);
        tooltips.forEach(adder);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (context.getClickedFace() != Direction.UP)
            return InteractionResult.FAIL;

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!this.getBlock().defaultBlockState().canSurvive(world, pos))
            return InteractionResult.FAIL;
        if (!world.getBlockState(pos).canBeReplaced(context))
            return InteractionResult.FAIL;
        BlockPlaceContext blockitemusecontext = this.updatePlacementContext(context);
        if (blockitemusecontext == null) {
            return InteractionResult.FAIL;
        } else {
            BlockState madeState = this.getPlacementState(blockitemusecontext);
            if (madeState == null) {
                return InteractionResult.FAIL;
            } else if (!world.setBlock(pos, madeState, 11)) {
                return InteractionResult.FAIL;
            } else {
                Player playerentity = blockitemusecontext.getPlayer();
                ItemStack itemstack = blockitemusecontext.getItemInHand();
                BlockState newState = world.getBlockState(pos);
                Block block = newState.getBlock();
                if (block == madeState.getBlock()) {
                    this.updateCustomBlockEntityTag(pos, world, playerentity, itemstack, newState);
                    block.setPlacedBy(world, pos, newState, playerentity, itemstack);
                    if (playerentity instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerentity, pos, itemstack);
                    }
                }
                SoundType soundtype = newState.getSoundType();
                world.playSound(playerentity, pos, this.getPlaceSound(newState), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
    }
}