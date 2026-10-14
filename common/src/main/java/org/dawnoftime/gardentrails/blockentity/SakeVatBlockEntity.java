package org.dawnoftime.gardentrails.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.block.templates.SakeVatBlock;
import org.dawnoftime.gardentrails.recipe.FermentingRecipe;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.registry.GTRecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SakeVatBlockEntity extends BlockEntity {
    private final SimpleContainer ingredients = new SimpleContainer(FermentingRecipe.INGREDIENT_COUNT);
    private ItemStack result = ItemStack.EMPTY;
    private long finishTime = -1;

    public SakeVatBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntitiesRegistry.INSTANCE.SAKE_VAT.get(), pos, state);
    }

    public boolean isFermenting() {
        return !this.result.isEmpty() && !this.getBlockState().getValue(SakeVatBlock.READY);
    }

    public long getFinishTime() {
        return this.finishTime;
    }

    private List<ItemStack> currentItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < this.ingredients.getContainerSize(); i++) {
            if (!this.ingredients.getItem(i).isEmpty()) {
                items.add(this.ingredients.getItem(i));
            }
        }
        return items;
    }

    private List<FermentingRecipe> allRecipes() {
        if (this.level == null) {
            return List.of();
        }
        return this.level.getRecipeManager().getAllRecipesFor(GTRecipeTypesRegistry.INSTANCE.FERMENTING.get());
    }

    /** Pure check, same answer on both sides as the content is synchronized. */
    public boolean canAccept(ItemStack stack) {
        if (stack.isEmpty() || !this.result.isEmpty() || this.getBlockState().getValue(SakeVatBlock.LEVEL) >= FermentingRecipe.INGREDIENT_COUNT) {
            return false;
        }
        List<ItemStack> current = this.currentItems();
        for (FermentingRecipe recipe : this.allRecipes()) {
            if (recipe.canComplete(current, stack)) {
                return true;
            }
        }
        return false;
    }

    public void accept(ItemStack stack, Player player, InteractionHand hand) {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }
        List<ItemStack> current = this.currentItems();
        FermentingRecipe candidate = null;
        for (FermentingRecipe recipe : this.allRecipes()) {
            if (recipe.canComplete(current, stack)) {
                candidate = recipe;
                break;
            }
        }
        if (candidate == null) {
            return;
        }
        ItemStack remainder = candidate.getRemainderFor(stack);
        ItemStack single = stack.copyWithCount(1);
        for (int i = 0; i < this.ingredients.getContainerSize(); i++) {
            if (this.ingredients.getItem(i).isEmpty()) {
                this.ingredients.setItem(i, single);
                break;
            }
        }
        if (remainder.isEmpty()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        } else {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, remainder));
        }
        this.level.playSound(null, this.worldPosition, remainder.isEmpty() ? SoundEvents.COMPOSTER_FILL : SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

        BlockState state = this.getBlockState();
        int newLevel = state.getValue(SakeVatBlock.LEVEL) + 1;
        this.level.setBlock(this.worldPosition, state.setValue(SakeVatBlock.LEVEL, newLevel), Block.UPDATE_ALL);

        if (newLevel == FermentingRecipe.INGREDIENT_COUNT) {
            SimpleContainer full = new SimpleContainer(this.currentItems().toArray(new ItemStack[0]));
            for (FermentingRecipe recipe : this.allRecipes()) {
                if (recipe.matches(full, this.level)) {
                    this.result = recipe.getResultItem(this.level.registryAccess()).copy();
                    this.finishTime = this.level.getGameTime() + recipe.getFermentingTime();
                    this.ingredients.clearContent();
                    this.level.scheduleTick(this.worldPosition, state.getBlock(), recipe.getFermentingTime());
                    break;
                }
            }
        }
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, state, this.getBlockState(), Block.UPDATE_ALL);
    }

    public void extractBottle(Player player, InteractionHand hand) {
        if (this.level == null || this.level.isClientSide() || this.result.isEmpty()) {
            return;
        }
        ItemStack held = player.getItemInHand(hand);
        player.setItemInHand(hand, ItemUtils.createFilledResult(held, player, this.result.copy()));
        BlockState state = this.getBlockState();
        int newLevel = state.getValue(SakeVatBlock.LEVEL) - 1;
        if (newLevel <= 0) {
            this.result = ItemStack.EMPTY;
            this.finishTime = -1;
            this.level.setBlock(this.worldPosition, state.setValue(SakeVatBlock.LEVEL, 0).setValue(SakeVatBlock.READY, false), Block.UPDATE_ALL);
        } else {
            this.level.setBlock(this.worldPosition, state.setValue(SakeVatBlock.LEVEL, newLevel), Block.UPDATE_ALL);
        }
        this.level.playSound(null, this.worldPosition, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, state, this.getBlockState(), Block.UPDATE_ALL);
    }

    /** Gives the poured items back when the vat is broken before the fermentation starts. */
    public void dropContent(Level level, BlockPos pos) {
        if (this.result.isEmpty()) {
            for (int i = 0; i < this.ingredients.getContainerSize(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), this.ingredients.getItem(i));
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.writeContent(tag);
        return tag;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        this.writeContent(tag);
    }

    private void writeContent(CompoundTag tag) {
        for (int i = 0; i < this.ingredients.getContainerSize(); i++) {
            if (!this.ingredients.getItem(i).isEmpty()) {
                tag.put("ingredient_" + i, this.ingredients.getItem(i).save(new CompoundTag()));
            }
        }
        if (!this.result.isEmpty()) {
            tag.put("result", this.result.save(new CompoundTag()));
        }
        tag.putLong("finish_time", this.finishTime);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.ingredients.clearContent();
        for (int i = 0; i < this.ingredients.getContainerSize(); i++) {
            if (tag.contains("ingredient_" + i)) {
                this.ingredients.setItem(i, ItemStack.of(tag.getCompound("ingredient_" + i)));
            }
        }
        this.result = tag.contains("result") ? ItemStack.of(tag.getCompound("result")) : ItemStack.EMPTY;
        this.finishTime = tag.contains("finish_time") ? tag.getLong("finish_time") : -1;
    }
}
