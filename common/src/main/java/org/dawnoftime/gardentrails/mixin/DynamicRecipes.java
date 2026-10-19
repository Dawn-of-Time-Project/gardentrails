package org.dawnoftime.gardentrails.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.registry.GTDynamicRecipes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Replaces recipes of other mods by the Garden Trails versions stored in data/gardentrails/dynamic_recipes.
 * Since 26.3 recipes are a reloadable registry, so the recipe map is rebuilt from a registry copy at the end of the
 * constructor. The injector is non required: if it cannot apply, only these recipes are left unchanged.
 */
@Mixin(RecipeManager.class)
public class DynamicRecipes {
    @Shadow
    @Final
    @Mutable
    private RecipeMap recipes;

    @Shadow
    @Final
    @Mutable
    private Collection<RecipeHolder<?>> learnableRecipes;

    @Inject(method = "<init>(Lnet/minecraft/core/HolderLookup$Provider;)V", at = @At("TAIL"), require = 0)
    private void gt$injectDynamicRecipes(final HolderLookup.Provider registries, final CallbackInfo ci) {
        if (GTCommon.dynamicRecipes == null || GTCommon.dynamicRecipes.isEmpty()) {
            return;
        }
        final var ops = registries.createSerializationContext(JsonOps.INSTANCE);
        final Map<ResourceKey<Recipe<?>>, Recipe<?>> replacements = new LinkedHashMap<>();
        for (final GTDynamicRecipes.Entry entry : GTCommon.dynamicRecipes) {
            final String path = "/data/" + entry.file().getNamespace() + "/" + entry.file().getPath();
            try (InputStream stream = DynamicRecipes.class.getResourceAsStream(path)) {
                if (stream == null) {
                    GTCommon.LOG.error("Dynamic recipe file {} not found, the recipe {} is left unchanged", path, entry.target());
                    continue;
                }
                try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    final JsonElement json = JsonParser.parseReader(reader);
                    Recipe.DIRECT_CODEC.parse(ops, json)
                            .resultOrPartial(error -> GTCommon.LOG.error("Could not parse the dynamic recipe file {}: {}", path, error))
                            .ifPresent(recipe -> replacements.put(ResourceKey.create(Registries.RECIPE, entry.target()), recipe));
                }
            } catch (Exception e) {
                GTCommon.LOG.error("Could not read the dynamic recipe file {}, the recipe {} is left unchanged", path, entry.target(), e);
            }
        }
        if (replacements.isEmpty()) {
            return;
        }
        final MappedRegistry<Recipe<?>> merged = new MappedRegistry<>(Registries.RECIPE, Lifecycle.stable());
        for (final RecipeHolder<?> holder : this.recipes.values()) {
            if (!replacements.containsKey(holder.id())) {
                merged.register(holder.id(), holder.value(), RegistrationInfo.BUILT_IN);
            }
        }
        replacements.forEach((key, recipe) -> merged.register(key, recipe, RegistrationInfo.BUILT_IN));
        this.recipes = RecipeMap.create(merged);
        this.learnableRecipes = this.recipes.values().stream().filter(holder -> !holder.value().isSpecial()).collect(Collectors.toUnmodifiableList());
    }
}
