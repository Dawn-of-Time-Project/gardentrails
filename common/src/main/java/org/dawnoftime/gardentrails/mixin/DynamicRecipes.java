package org.dawnoftime.gardentrails.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.registry.GTDynamicRecipes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public class DynamicRecipes {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    // Since 1.21.2 the recipes are parsed in prepare() into a RecipeMap, so the replacements are added to that map
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;", at = @At("RETURN"), cancellable = true, require = 0)
    public void gt$injectDynamicRecipes(final ResourceManager resourceManager, final ProfilerFiller profiler, final CallbackInfoReturnable<RecipeMap> cir) {
        final RegistryOps<JsonElement> ops = this.registries.createSerializationContext(JsonOps.INSTANCE);
        final Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> replacements = new LinkedHashMap<>();
        for (final GTDynamicRecipes.Entry entry : GTCommon.dynamicRecipes) {
            final Optional<Resource> resource = resourceManager.getResource(entry.file());
            if (resource.isEmpty()) {
                GTCommon.LOG.error("Dynamic recipe file {} not found, the recipe {} is left unchanged", entry.file(), entry.target());
                continue;
            }
            try (Reader reader = resource.get().openAsReader()) {
                final DataResult<Recipe<?>> parsed = Recipe.CODEC.parse(ops, JsonParser.parseReader(reader));
                final Optional<Recipe<?>> recipe = parsed.resultOrPartial(error -> GTCommon.LOG.error("Could not parse the dynamic recipe file {}: {}", entry.file(), error));
                if (recipe.isPresent()) {
                    final ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, entry.target());
                    // put replaces the original recipe of the other mod if it exists
                    replacements.put(key, new RecipeHolder<>(key, recipe.get()));
                }
            } catch (Exception e) {
                GTCommon.LOG.error("Could not read the dynamic recipe file {}, the recipe {} is left unchanged", entry.file(), entry.target(), e);
            }
        }
        if (replacements.isEmpty()) {
            return;
        }
        final List<RecipeHolder<?>> all = new ArrayList<>();
        for (final RecipeHolder<?> holder : cir.getReturnValue().values()) {
            if (!replacements.containsKey(holder.id())) {
                all.add(holder);
            }
        }
        all.addAll(replacements.values());
        cir.setReturnValue(RecipeMap.create(all));
    }
}
