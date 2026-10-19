package org.dawnoftime.gardentrails.mixin;

import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.client.gui.SocialButton;
import org.dawnoftime.gardentrails.registry.GTCreativeModeTabsRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    @Unique
    private static final String gardentrails$SOCIAL_LINK = "https://linktr.ee/TheGoldenWorld";
    @Unique
    private Button gardentrails$socialButton;
    @Unique
    private boolean gardentrails$tabSelected;

    protected CreativeInventoryMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void gardentrails$init(CallbackInfo ci) {
        this.addRenderableWidget(this.gardentrails$socialButton = new SocialButton(this.leftPos + 156, this.topPos + 4,
                button -> Util.getPlatform().openUri(gardentrails$SOCIAL_LINK),
                Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "textures/gui/discord.png")));
        this.gardentrails$socialButton.setTooltip(Tooltip.create(Component.translatable("gui." + GTCommon.MOD_ID + ".discord")));
        this.gardentrails$socialButton.visible = this.gardentrails$tabSelected;
    }

    @Inject(method = "extractRenderState", at = @At(value = "HEAD"))
    public void gardentrails$render(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.gardentrails$socialButton != null) {
            this.gardentrails$socialButton.visible = this.gardentrails$tabSelected;
        }
    }

    @Inject(method = "selectTab", at = @At(value = "HEAD"))
    public void gardentrails$selectTab(CreativeModeTab tab, CallbackInfo ci) {
        this.gardentrails$tabSelected = tab == GTCreativeModeTabsRegistry.INSTANCE.GT_TAB.get();
    }
}
