package org.dawnoftime.gardentrails.compat.jei;
import org.dawnoftime.gardentrails.recipe.DryerRecipe;
import org.dawnoftime.gardentrails.recipe.FermentingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
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
    private static final Identifier UID = Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "jei_plugin");

    @Override
    public @NotNull Identifier getPluginUid() {
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
        // Custom recipes are not synchronized to clients since 1.21.2, only the integrated server is readable for now
        final var server = Minecraft.getInstance().getSingleplayerServer();
        if (server == null) {
            return;
        }
        final var holders = server.getRecipeManager().getRecipes();
        registration.addRecipes(DryingRecipeCategory.RECIPE_TYPE,
                holders.stream().map(RecipeHolder::value).filter(DryerRecipe.class::isInstance).map(DryerRecipe.class::cast).toList());
        registration.addRecipes(FermentingRecipeCategory.RECIPE_TYPE,
                holders.stream().map(RecipeHolder::value).filter(FermentingRecipe.class::isInstance).map(FermentingRecipe.class::cast).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(GTBlocksRegistry.INSTANCE.SAKE_VAT.get()), FermentingRecipeCategory.RECIPE_TYPE);
        final GTBlocksRegistry blocks = GTBlocksRegistry.INSTANCE;
        final Collection<Supplier<Block>> trays = blocks.DRYING_TRAYS.values();
        for (Supplier<Block> tray : trays) {
            registration.addRecipeCatalyst(new ItemStack(tray.get()), DryingRecipeCategory.RECIPE_TYPE);
        }
    }
}
