package org.dawnoftime.gardentrails.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.registry.GTDynamicRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Reader;
import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public class DynamicRecipes {
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"), require = 0)
    public void gt$interceptRecipesApply(final Map<ResourceLocation, JsonElement> map, final ResourceManager resourceManager, final ProfilerFiller profiler, final CallbackInfo ci) {
        for (final GTDynamicRecipes.Entry entry : GTCommon.dynamicRecipes) {
            final Optional<Resource> resource = resourceManager.getResource(entry.file());
            if (resource.isEmpty()) {
                GTCommon.LOG.error("Dynamic recipe file {} not found, the recipe {} is left unchanged", entry.file(), entry.target());
                continue;
            }
            try (Reader reader = resource.get().openAsReader()) {
                // put replaces the original recipe of the other mod if it exists
                map.put(entry.target(), JsonParser.parseReader(reader));
            } catch (Exception e) {
                GTCommon.LOG.error("Could not read the dynamic recipe file {}, the recipe {} is left unchanged", entry.file(), entry.target(), e);
            }
        }
    }
}
