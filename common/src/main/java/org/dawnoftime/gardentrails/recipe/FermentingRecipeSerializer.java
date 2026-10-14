package org.dawnoftime.gardentrails.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FermentingRecipeSerializer implements RecipeSerializer<FermentingRecipe> {
    @Override
    public @NotNull FermentingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonArray array = GsonHelper.getAsJsonArray(json, "ingredients");
        if (array.size() != FermentingRecipe.INGREDIENT_COUNT) {
            throw new JsonSyntaxException("'ingredients' must contain exactly " + FermentingRecipe.INGREDIENT_COUNT + " entries.");
        }
        List<FermentingIngredient> ingredients = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            ingredients.add(FermentingIngredient.fromJson(GsonHelper.convertToJsonObject(array.get(i), "ingredients[" + i + "]")));
        }
        JsonObject resultObj = GsonHelper.getAsJsonObject(json, "result");
        ItemStack result = new ItemStack(ShapedRecipe.itemFromJson(resultObj), GsonHelper.getAsInt(resultObj, "count", 1));
        int fermentingTime = GsonHelper.getAsInt(json, "fermentingTime", 1200);
        return new FermentingRecipe(recipeId, ingredients, result, fermentingTime);
    }

    @Override
    public @NotNull FermentingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        List<FermentingIngredient> ingredients = new ArrayList<>();
        for (int i = 0; i < FermentingRecipe.INGREDIENT_COUNT; i++) {
            ingredients.add(FermentingIngredient.fromNetwork(buffer));
        }
        ItemStack result = buffer.readItem();
        int fermentingTime = buffer.readVarInt();
        return new FermentingRecipe(recipeId, ingredients, result, fermentingTime);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull FermentingRecipe recipe) {
        recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buffer));
        buffer.writeItem(recipe.result);
        buffer.writeVarInt(recipe.fermentingTime);
    }
}
