package org.dawnoftime.gardentrails.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.dawnoftime.gardentrails.registry.GTRecipeSerializersRegistry;
import org.dawnoftime.gardentrails.registry.GTRecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public record DryerRecipe(String group, Ingredient ingredient, ItemStack result, float experience,
                          int dryingTime, int ingredientCount) implements Recipe<SimpleContainerRecipeInput> {

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    public int getDryingTime() {
        return this.dryingTime;
    }

    @Override
    public boolean matches(SimpleContainerRecipeInput inv, @NotNull Level worldIn) {
        return this.ingredient.test(inv.getItem(0)) && inv.getItem(0).getCount() >= this.ingredientCount;
    }

    @Override
    public ItemStack assemble(SimpleContainerRecipeInput simpleContainer, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public @NotNull RecipeSerializer<DryerRecipe> getSerializer() {
        return GTRecipeSerializersRegistry.INSTANCE.DRYER.get();
    }

    @Override
    @Nonnull
    public RecipeType<DryerRecipe> getType() {
        return GTRecipeTypesRegistry.INSTANCE.DRYING.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
