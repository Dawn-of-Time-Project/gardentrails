package org.dawnoftime.gardentrails.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.registry.GTRecipeTypesRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * JEI entry point. Only loaded when JEI is installed (Forge scans the annotation, Fabric uses the jei_mod_plugin entrypoint).
 */
@JeiPlugin
public class GTJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new DryingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new FermentingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final var level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        registration.addRecipes(DryingRecipeCategory.RECIPE_TYPE,
                level.getRecipeManager().getAllRecipesFor(GTRecipeTypesRegistry.INSTANCE.DRYING.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(FermentingRecipeCategory.RECIPE_TYPE,
                level.getRecipeManager().getAllRecipesFor(GTRecipeTypesRegistry.INSTANCE.FERMENTING.get()).stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(GTBlocksRegistry.INSTANCE.SAKE_VAT.get()), FermentingRecipeCategory.RECIPE_TYPE);
        final Collection<Supplier<Block>> trays = GTBlocksRegistry.INSTANCE.DRYING_TRAYS.values();
        for (Supplier<Block> tray : trays) {
            registration.addRecipeCatalyst(new ItemStack(tray.get()), DryingRecipeCategory.RECIPE_TYPE);
        }
    }
}
