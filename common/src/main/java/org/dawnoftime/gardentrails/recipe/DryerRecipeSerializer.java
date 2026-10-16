package org.dawnoftime.gardentrails.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class DryerRecipeSerializer {
    public static final MapCodec<DryerRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(DryerRecipe::group),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(DryerRecipe::ingredient),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(DryerRecipe::result),
                    Codec.FLOAT.optionalFieldOf("experience", 0f).forGetter(DryerRecipe::experience),
                    Codec.INT.optionalFieldOf("dryingTime", 1200).forGetter(DryerRecipe::dryingTime),
                    Codec.INT.optionalFieldOf("ingredientCount", 1).forGetter(DryerRecipe::ingredientCount)
            ).apply(instance, DryerRecipe::new)
    );

    private static final StreamCodec<ByteBuf, Ingredient> INGREDIENT_STREAM_CODEC = ByteBufCodecs.fromCodec(Ingredient.CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, DryerRecipe> STREAM_CODEC = StreamCodec.of(DryerRecipeSerializer::write, DryerRecipeSerializer::read);

    public static final RecipeSerializer<DryerRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private DryerRecipeSerializer() {
    }

    public static DryerRecipe read(RegistryFriendlyByteBuf buf) {
        String group = buf.readUtf(32767);
        Ingredient ingredient = INGREDIENT_STREAM_CODEC.decode(buf);
        ItemStackTemplate itemStackResult = ItemStackTemplate.STREAM_CODEC.decode(buf);
        float experience = buf.readFloat();
        int dryingTime = buf.readVarInt();
        int ingredientCount = buf.readVarInt();
        return new DryerRecipe(group, ingredient, itemStackResult, experience, dryingTime, ingredientCount);
    }

    public static void write(RegistryFriendlyByteBuf buf, DryerRecipe recipe) {
        buf.writeUtf(recipe.group(), 32767);
        INGREDIENT_STREAM_CODEC.encode(buf, recipe.ingredient());
        ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.result());
        buf.writeFloat(recipe.experience());
        buf.writeVarInt(recipe.dryingTime());
        buf.writeVarInt(recipe.ingredientCount());
    }
}
