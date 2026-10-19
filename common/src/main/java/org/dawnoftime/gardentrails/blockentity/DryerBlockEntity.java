package org.dawnoftime.gardentrails.blockentity;

import net.minecraft.world.item.Item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.platform.Services;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.dawnoftime.gardentrails.recipe.DryerRecipe;
import org.dawnoftime.gardentrails.recipe.SimpleContainerRecipeInput;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.registry.GTRecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class DryerBlockEntity extends BlockEntity {
    public final SimpleContainer itemHandler = new SimpleContainer(2);
    private final int[] remainingTicks = new int[2];
    private boolean isInOperation;

    public DryerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(GTBlockEntitiesRegistry.INSTANCE.DRYER.get(), pPos, pBlockState);
    }

    public void tick() {
        if (this.getLevel() != null && this.isInOperation) {
            int finish = 0;
            boolean success = false;
            for (int slotIndex = 0; slotIndex < this.remainingTicks.length; slotIndex++) {
                this.remainingTicks[slotIndex]--;

                if (this.remainingTicks[slotIndex] <= 0) {
                    this.remainingTicks[slotIndex] = 0;
                    // Item dried, we replace it with the recipe result, and clear the recipe cached.
                    final DryerRecipe recipe = this.getDryerRecipe(new SimpleContainer(this.itemHandler.getItem(slotIndex)));

                    if (recipe != null) {
                        this.itemHandler.setItem(slotIndex, recipe.getResultItem(this.getLevel().registryAccess()).copy());
                        success = true;
                    }
                    finish++;
                }
            }

            if (success) {
                this.setChanged();
                BlockState state = level.getBlockState(worldPosition);
                this.getLevel().sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
            }

            if (finish >= 2) {
                this.isInOperation = false;
            }
        }
    }

    public InteractionResult tryInsertItemStack(final ItemStack itemStack, final boolean simple, final Level worldIn, final BlockPos pos, final Player player) {

        //Try to put the itemStack in an empty dryer
        if (this.putItemStackInFreeSpace(itemStack, simple, player)) {
            return InteractionResult.SUCCESS;
        }
        //No empty dryer, let's see if we could replace a dried item with ours
        if (simple) {
            if (this.itemIsDried(0)) {
                this.dropItemIndex(0, worldIn, pos);
                this.putItemStackInIndex(0, itemStack, player);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        final int index = this.dropOneDriedItem(worldIn, pos);
        if (index < 0) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        this.putItemStackInIndex(index, itemStack, player);
        return InteractionResult.SUCCESS;
    }

    public InteractionResult dropOneItem(final Level worldIn, final BlockPos pos) {
        if (this.dropOneDriedItem(worldIn, pos) > -1) {
            return InteractionResult.SUCCESS;
        }
        if (!this.itemHandler.getItem(0).isEmpty()) {
            this.dropItemIndex(0, worldIn, pos);
            return InteractionResult.SUCCESS;
        }
        if (!this.itemHandler.getItem(1).isEmpty()) {
            this.dropItemIndex(1, worldIn, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    public int dropOneDriedItem(final Level worldIn, final BlockPos pos) {
        if (this.itemIsDried(0)) {
            this.dropItemIndex(0, worldIn, pos);
            return 0;
        }
        if (this.itemIsDried(1)) {
            this.dropItemIndex(1, worldIn, pos);
            return 1;
        }
        return -1;
    }

    private boolean itemIsDried(final int index) {
        if (this.itemHandler.getItem(index).isEmpty()) {
            return false;
        }
        return this.remainingTicks[index] <= 0;
    }

    private boolean putItemStackInFreeSpace(final ItemStack itemStack, final boolean simple, final Player player) {

        if (this.itemHandler.getItem(0).isEmpty() && this.putItemStackInIndex(0, itemStack, player)) {
            this.isInOperation = true;
            return true;
        }
        if (!simple && this.itemHandler.getItem(1).isEmpty() && this.putItemStackInIndex(1, itemStack, player)) {
            this.isInOperation = true;
            return true;
        }
        return false;
    }

    @Nullable
    private DryerRecipe getDryerRecipe(final SimpleContainer ingredientInventory) {
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().getRecipeFor(GTRecipeTypesRegistry.INSTANCE.DRYING.get(), new SimpleContainerRecipeInput(ingredientInventory), serverLevel).map(RecipeHolder::value).orElse(null);
        }
        return null;
    }

    private boolean putItemStackInIndex(final int index, final ItemStack itemStack, final Player player) {
        //Tries to put the itemStack in a dryer : first we check if there is a corresponding recipe, then we set the variables.
        if (this.getLevel() != null) {
            final SimpleContainer invInHand = new SimpleContainer(itemStack);
            final DryerRecipe recipe = this.getDryerRecipe(invInHand);
            if (recipe != null && recipe.matches(new SimpleContainerRecipeInput(invInHand), this.getLevel())) {
                this.itemHandler.setItem(index, new ItemStack(recipe.ingredient().items().findFirst().orElseThrow(), recipe.ingredientCount()));
                if (!player.isCreative()) {
                    itemStack.shrink(recipe.ingredientCount());
                }
                final float timeVariation = new Random().nextFloat() * 2.0F - 1.0F;
                final int range = 30;
                this.remainingTicks[index] = (int) (recipe.getDryingTime() * (100 + timeVariation * range) / 100);

                this.setChanged();
                BlockState state = level.getBlockState(worldPosition);
                this.getLevel().sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);

                return true;
            }
        }
        return false;
    }

    private void dropItemIndex(final int index, final Level worldIn, final BlockPos pos) {
        Block.popResource(worldIn, pos, this.itemHandler.removeItem(index, 64));
        this.remainingTicks[index] = 0;
        if (this.getLevel() != null) {
            final BlockState state = this.getLevel().getBlockState(pos);
            this.setChanged();
            this.getLevel().sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!itemHandler.getItem(0).isEmpty()) {
            output.store("slot_0", ItemStack.CODEC, itemHandler.getItem(0));
        }
        if (!itemHandler.getItem(1).isEmpty()) {
            output.store("slot_1", ItemStack.CODEC, itemHandler.getItem(1));
        }
        for (int index = 0; index < 2; index++) {
            output.putInt("remainingTime" + index, this.remainingTicks[index]);
        }
        output.putBoolean("isInOperation", this.isInOperation);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        itemHandler.clearContent();
        input.read("slot_0", ItemStack.CODEC).ifPresent(itemStack -> itemHandler.setItem(0, itemStack));
        input.read("slot_1", ItemStack.CODEC).ifPresent(itemStack -> itemHandler.setItem(1, itemStack));
        for (int index = 0; index < 2; index++) {
            this.remainingTicks[index] = input.getIntOr("remainingTime" + index, 0);
        }
        this.isInOperation = input.getBooleanOr("isInOperation", false);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.level != null) {
            this.itemHandler.removeAllItems().forEach(stack -> Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), stack));
        }
    }
}
