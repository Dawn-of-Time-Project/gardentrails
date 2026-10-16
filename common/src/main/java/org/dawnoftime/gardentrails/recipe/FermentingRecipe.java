package org.dawnoftime.gardentrails.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.dawnoftime.gardentrails.registry.GTRecipeSerializersRegistry;
import org.dawnoftime.gardentrails.registry.GTRecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record FermentingRecipe(List<FermentingIngredient> ingredients, ItemStackTemplate result, int fermentingTime) implements Recipe<SimpleContainerRecipeInput> {
    public static final int INGREDIENT_COUNT = 3;

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result.create();
    }

    public int getFermentingTime() {
        return this.fermentingTime;
    }

    public List<FermentingIngredient> getFermentingIngredients() {
        return this.ingredients;
    }
    /** True if the 3 items of the container can be assigned to the 3 ingredients, in any order. */
    @Override
    public boolean matches(SimpleContainerRecipeInput input,  @NotNull Level level) {
        SimpleContainer inv = input.container();
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            items.add(inv.getItem(i));
        }
        return items.size() == INGREDIENT_COUNT && this.canAssign(items);
    }

    /** True if the current items plus the candidate can still be assigned to distinct ingredients. */
    public boolean canComplete(List<ItemStack> current, ItemStack candidate) {
        List<ItemStack> items = new ArrayList<>(current);
        items.add(candidate);
        return items.size() <= INGREDIENT_COUNT && this.canAssign(items);
    }

    /** Returns the remainder of the ingredient matching the stack, or an empty stack. */
    public ItemStack getRemainderFor(ItemStack stack) {
        for (FermentingIngredient ingredient : this.ingredients) {
            if (ingredient.test(stack)) {
                return ingredient.getRemainder();
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean canAssign(List<ItemStack> items) {
        return this.assign(items, 0, new boolean[this.ingredients.size()]);
    }

    private boolean assign(List<ItemStack> items, int index, boolean[] used) {
        if (index >= items.size()) {
            return true;
        }
        for (int i = 0; i < this.ingredients.size(); i++) {
            if (!used[i] && this.ingredients.get(i).test(items.get(index))) {
                used[i] = true;
                if (this.assign(items, index + 1, used)) {
                    return true;
                }
                used[i] = false;
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainerRecipeInput input) {
        return this.result.create();
    }

    @Override
    public @NotNull RecipeSerializer<FermentingRecipe> getSerializer() {
        return GTRecipeSerializersRegistry.INSTANCE.FERMENTING.get();
    }

    @Override
    public @NotNull RecipeType<FermentingRecipe> getType() {
        return GTRecipeTypesRegistry.INSTANCE.FERMENTING.get();
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
    public @NotNull String group() {
        return "";
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
