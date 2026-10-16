package org.dawnoftime.gardentrails.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class FermentingRecipeSerializer {
    public static final MapCodec<FermentingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FermentingIngredient.CODEC.listOf(FermentingRecipe.INGREDIENT_COUNT, FermentingRecipe.INGREDIENT_COUNT).fieldOf("ingredients").forGetter(FermentingRecipe::ingredients),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(FermentingRecipe::result),
            Codec.INT.optionalFieldOf("fermentingTime", 1200).forGetter(FermentingRecipe::fermentingTime)
    ).apply(instance, FermentingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FermentingRecipe> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    public static final RecipeSerializer<FermentingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private FermentingRecipeSerializer() {
    }
}
