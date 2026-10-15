package org.dawnoftime.gardentrails.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.recipe.FermentingRecipe;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * JEI category showing the sake vat recipes (3 ingredients -> result + fermenting time).
 */
public class FermentingRecipeCategory implements IRecipeCategory<FermentingRecipe> {
    public static final RecipeType<FermentingRecipe> RECIPE_TYPE = RecipeType.create(GTCommon.MOD_ID, "fermenting", FermentingRecipe.class);

    private static final int WIDTH = 130;
    private static final int HEIGHT = 40;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;
    private final Component title = Component.translatable("gardentrails.jei.category.fermenting");

    public FermentingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(GTBlocksRegistry.INSTANCE.SAKE_VAT.get()));
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.createAnimatedRecipeArrow(100);
    }

    @Override
    public @NotNull RecipeType<FermentingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return this.title;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull FermentingRecipe recipe, @NotNull IFocusGroup focuses) {
        for (int i = 0; i < recipe.getFermentingIngredients().size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 10 + i * 20, 8)
                    .addItemStacks(recipe.getFermentingIngredients().get(i).getDisplayStacks());
        }
        final ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 8)
                .addItemStack(result);
    }

    @Override
    public void draw(@NotNull FermentingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        for (int i = 0; i < recipe.getFermentingIngredients().size(); i++) {
            this.slot.draw(guiGraphics, 9 + i * 20, 7);
        }
        this.slot.draw(guiGraphics, 103, 7);
        this.arrow.draw(guiGraphics, 72, 8);

        final Font font = Minecraft.getInstance().font;
        final Component time = Component.translatable("gardentrails.jei.fermenting.time", recipe.getFermentingTime() / 20);
        final int textX = (WIDTH - font.width(time)) / 2;
        guiGraphics.drawString(font, time, textX, 30, 0xFF808080, false);
    }
}
