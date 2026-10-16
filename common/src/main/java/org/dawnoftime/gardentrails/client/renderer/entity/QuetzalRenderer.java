package org.dawnoftime.gardentrails.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.dawnoftime.gardentrails.client.model.entity.QuetzalModel;
import org.dawnoftime.gardentrails.client.renderer.entity.state.QuetzalRenderState;
import org.dawnoftime.gardentrails.entity.QuetzalEntity;
import org.jetbrains.annotations.NotNull;

import static org.dawnoftime.gardentrails.GTCommon.MOD_ID;

public class QuetzalRenderer extends MobRenderer<QuetzalEntity, QuetzalRenderState, QuetzalModel> {
    public static final Identifier QUETZAL_TEXTURE = Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/quetzal.png");

    public QuetzalRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new QuetzalModel(ctx.bakeLayer(QuetzalModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(@NotNull QuetzalRenderState state) {
        return QUETZAL_TEXTURE;
    }

    @Override
    @NotNull
    public QuetzalRenderState createRenderState() {
        return new QuetzalRenderState();
    }

    @Override
    public void extractRenderState(@NotNull QuetzalEntity entity, @NotNull QuetzalRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.beakOpen = entity.getBeakOpen(partialTick);
        // Same value as the vanilla parrot renderer
        float flap = Mth.lerp(partialTick, entity.oFlap, entity.flap);
        state.flapSpeed = Mth.lerp(partialTick, entity.oFlapSpeed, entity.flapSpeed);
        state.flapAngle = (Mth.sin(flap) + 1.0F) * state.flapSpeed;
    }
}
