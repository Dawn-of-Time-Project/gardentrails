package org.dawnoftime.gardentrails.mixin;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.dawnoftime.gardentrails.GTCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.stream.Collectors;

// Adds the cross mod recipes built by GTDynamicRecipes to the recipes read from the data packs.
// The injector is additive and non-required: if it cannot apply, only these recipes are missing.
// Since 26.3 recipes are a reloadable registry, so the map is rebuilt from a registry copy at the end of the constructor.
@Mixin(RecipeManager.class)
public class DynamicRecipes {
    @Unique
    private static final Logger GT_LOGGER = LoggerFactory.getLogger("gardentrails/dynamic_recipes");

    @Shadow
    @Final
    @Mutable
    private RecipeMap recipes;

    @Shadow
    @Final
    @Mutable
    private Collection<RecipeHolder<?>> learnableRecipes;

    @Inject(method = "<init>(Lnet/minecraft/core/HolderLookup$Provider;)V", at = @At("TAIL"), require = 0)
    private void gt$addDynamicRecipes(HolderLookup.Provider registries, CallbackInfo ci) {
        if (GTCommon.dynamicRecipes == null || GTCommon.dynamicRecipes.isEmpty()) {
            return;
        }
        MappedRegistry<Recipe<?>> merged = new MappedRegistry<>(Registries.RECIPE, Lifecycle.stable());
        for (RecipeHolder<?> holder : this.recipes.values()) {
            merged.register(holder.id(), holder.value(), RegistrationInfo.BUILT_IN);
        }
        var ops = registries.createSerializationContext(JsonOps.INSTANCE);
        for (Pair<Identifier, JsonObject> pair : GTCommon.dynamicRecipes) {
            if (pair.getFirst() == null || pair.getSecond() == null) {
                continue;
            }
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, pair.getFirst());
            if (merged.containsKey(key)) {
                continue;
            }
            Recipe.DIRECT_CODEC.parse(ops, pair.getSecond())
                    .resultOrPartial(error -> GT_LOGGER.error("Could not read the dynamic recipe {}: {}", pair.getFirst(), error))
                    .ifPresent(recipe -> merged.register(key, recipe, RegistrationInfo.BUILT_IN));
        }
        this.recipes = RecipeMap.create(merged);
        this.learnableRecipes = this.recipes.values().stream().filter(r -> !r.value().isSpecial()).collect(Collectors.toUnmodifiableList());
    }
}
