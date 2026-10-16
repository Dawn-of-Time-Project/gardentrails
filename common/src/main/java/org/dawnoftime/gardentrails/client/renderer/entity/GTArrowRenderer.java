package org.dawnoftime.gardentrails.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import org.dawnoftime.gardentrails.entity.GTArrowEntity;
import org.jetbrains.annotations.NotNull;

public class GTArrowRenderer extends ArrowRenderer<GTArrowEntity, GTArrowRenderer.GTArrowRenderState> {

    /** Render state carrying the texture of the arrow variant. */
    public static class GTArrowRenderState extends ArrowRenderState {
        public Identifier texture;
    }

    public GTArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull GTArrowRenderState createRenderState() {
        return new GTArrowRenderState();
    }

    @Override
    public void extractRenderState(@NotNull GTArrowEntity entity, @NotNull GTArrowRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.texture = entity.getVariant().getEntityTexture();
    }

    @Override
    protected @NotNull Identifier getTextureLocation(@NotNull GTArrowRenderState renderState) {
        return renderState.texture;
    }
}
