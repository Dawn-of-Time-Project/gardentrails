package org.dawnoftime.gardentrails.mixin;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Tuple;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.dawnoftime.gardentrails.GTCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.Map;

// Adds the cross mod recipes built by GTDynamicRecipes to the recipes read from the data packs.
// The injector is additive and non-required: if it cannot apply, only these recipes are missing.
@Mixin(RecipeManager.class)
public class DynamicRecipes {
    @Unique
    private static final Logger GT_LOGGER = LoggerFactory.getLogger("gardentrails/dynamic_recipes");

    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
            at = @At("RETURN"), cancellable = true, require = 0)
    private void gt$addDynamicRecipes(ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfoReturnable<RecipeMap> cir) {
        if (GTCommon.dynamicRecipes == null || GTCommon.dynamicRecipes.isEmpty()) {
            return;
        }
        Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> merged = new LinkedHashMap<>();
        for (RecipeHolder<?> holder : cir.getReturnValue().values()) {
            merged.put(holder.id(), holder);
        }
        var ops = this.registries.createSerializationContext(JsonOps.INSTANCE);
        for (Tuple<Identifier, JsonObject> pair : GTCommon.dynamicRecipes) {
            if (pair.getA() == null || pair.getB() == null) {
                continue;
            }
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, pair.getA());
            Recipe.CODEC.parse(ops, pair.getB())
                    .resultOrPartial(error -> GT_LOGGER.error("Could not read the dynamic recipe {}: {}", pair.getA(), error))
                    .ifPresent(recipe -> merged.put(key, new RecipeHolder<>(key, recipe)));
        }
        cir.setReturnValue(RecipeMap.create(merged.values()));
    }
}
