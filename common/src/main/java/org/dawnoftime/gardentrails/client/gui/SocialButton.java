package org.dawnoftime.gardentrails.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/** 12x12 clickable icon that draws one texture and darkens it on hover. */
public class SocialButton extends Button {
    private static final int NORMAL_COLOR = 0xFFFFFFFF;
    private static final int HOVER_COLOR = 0xFFB3B3B3;

    private final Identifier buttonTexture;

    public SocialButton(int x, int y, OnPress pressable, Identifier texture) {
        super(x, y, 12, 12, Component.empty(), pressable, DEFAULT_NARRATION);
        this.buttonTexture = texture;
    }

    @Override
    protected void extractContents(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int color = this.isHovered() && this.active ? HOVER_COLOR : NORMAL_COLOR;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.buttonTexture, this.getX() - 1, this.getY(), 0, 0, 12, 12, 12, 12, color);
    }
}
