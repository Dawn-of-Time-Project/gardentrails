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
import org.dawnoftime.gardentrails.recipe.DryerRecipe;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * JEI category showing the drying tray recipes (input -> dried result + drying time).
 */
public class DryingRecipeCategory implements IRecipeCategory<DryerRecipe> {
    public static final RecipeType<DryerRecipe> RECIPE_TYPE = RecipeType.create(GTCommon.MOD_ID, "drying", DryerRecipe.class);

    private static final int WIDTH = 100;
    private static final int HEIGHT = 40;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final IDrawableAnimated arrow;
    private final Component title = Component.translatable("gardentrails.jei.category.drying");

    public DryingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(GTBlocksRegistry.INSTANCE.OAK_DRYING_TRAY.get()));
        this.slot = guiHelper.getSlotDrawable();
        this.arrow = guiHelper.createAnimatedRecipeArrow(100);
    }

    @Override
    public @NotNull RecipeType<DryerRecipe> getRecipeType() {
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull DryerRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 8)
                .addIngredients(recipe.getIngredients().get(0));

        final ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 8)
                .addItemStack(result);
    }

    @Override
    public void draw(@NotNull DryerRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.slot.draw(guiGraphics, 9, 7);
        this.slot.draw(guiGraphics, 73, 7);
        this.arrow.draw(guiGraphics, 40, 8);

        final Font font = Minecraft.getInstance().font;
        final Component time = Component.translatable("gardentrails.jei.drying.time", recipe.getDryingTime() / 20);
        final int textX = (WIDTH - font.width(time)) / 2;
        guiGraphics.drawString(font, time, textX, 30, 0xFF808080, false);
    }
}
