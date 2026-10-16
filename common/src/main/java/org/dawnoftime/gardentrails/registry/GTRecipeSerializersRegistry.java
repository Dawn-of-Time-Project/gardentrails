package org.dawnoftime.gardentrails.registry;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.dawnoftime.gardentrails.recipe.DryerRecipe;
import org.dawnoftime.gardentrails.recipe.DryerRecipeSerializer;
import org.dawnoftime.gardentrails.recipe.FermentingRecipe;
import org.dawnoftime.gardentrails.recipe.FermentingRecipeSerializer;

import java.util.function.Supplier;

public abstract class GTRecipeSerializersRegistry {
    public static GTRecipeSerializersRegistry INSTANCE;

    public final Supplier<RecipeSerializer<DryerRecipe>> DRYER = register("dryer", () -> DryerRecipeSerializer.SERIALIZER);
    public final Supplier<RecipeSerializer<FermentingRecipe>> FERMENTING = register("fermenting", () -> FermentingRecipeSerializer.SERIALIZER);

    public abstract <T extends RecipeSerializer<? extends Recipe<?>>> Supplier<T> register(String name, Supplier<T> recipeSerializer);
}