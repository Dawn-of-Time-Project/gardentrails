package org.dawnoftime.gardentrails.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.dawnoftime.gardentrails.client.model.entity.SilkmothModel;
import org.dawnoftime.gardentrails.entity.SilkmothEntity;
import org.jetbrains.annotations.NotNull;

import static org.dawnoftime.gardentrails.GTCommon.MOD_ID;

public class SilkmothRenderer extends MobRenderer<SilkmothEntity, LivingEntityRenderState, SilkmothModel> {
	private static final Identifier SILKMOTH_TEXTURE = Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/silkmoth.png");

	public SilkmothRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SilkmothModel(ctx.bakeLayer(SilkmothModel.LAYER_LOCATION)), 0.1F);
	}

	@Override
	public @NotNull LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public @NotNull Identifier getTextureLocation(@NotNull LivingEntityRenderState renderState) {
		return SILKMOTH_TEXTURE;
	}
}
