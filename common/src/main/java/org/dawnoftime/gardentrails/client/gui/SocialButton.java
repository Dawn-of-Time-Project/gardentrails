package org.dawnoftime.gardentrails.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/** 12x12 clickable icon that draws one texture and darkens it on hover. */
public class SocialButton extends Button {
    private final ResourceLocation buttonTexture;

    public SocialButton(int x, int y, OnPress pressable, ResourceLocation texture) {
        super(x, y, 12, 12, Component.empty(), pressable, DEFAULT_NARRATION);
        this.buttonTexture = texture;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        PoseStack ps = guiGraphics.pose();

        if (this.isHovered() && this.active) {
            guiGraphics.setColor(0.7F, 0.7F, 0.7F, 1.0F);
        }

        ps.pushPose();
        RenderSystem.enableBlend();
        guiGraphics.blit(buttonTexture, this.getX() - 1, this.getY(), 0, 0, 12, 12, 12, 12);
        RenderSystem.disableBlend();
        ps.popPose();

        // Reset the tint so the widgets drawn afterwards are not darkened
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
