package org.dawnoftime.gardentrails.client.renderer.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.dawnoftime.gardentrails.entity.GTArrowEntity;
import org.jetbrains.annotations.NotNull;

public class GTArrowRenderer extends ArrowRenderer<GTArrowEntity> {

    public GTArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull GTArrowEntity entity) {
        return entity.getVariant().getEntityTexture();
    }
}
