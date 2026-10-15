package org.dawnoftime.gardentrails.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.dawnoftime.gardentrails.client.model.entity.QuetzalModel;
import org.dawnoftime.gardentrails.entity.QuetzalEntity;
import org.jetbrains.annotations.NotNull;

import static org.dawnoftime.gardentrails.GTCommon.MOD_ID;

public class QuetzalRenderer extends MobRenderer<QuetzalEntity, QuetzalModel<QuetzalEntity>> {
    public static final ResourceLocation QUETZAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/entity/quetzal.png");

    public QuetzalRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new QuetzalModel<>(ctx.bakeLayer(QuetzalModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull QuetzalEntity entity) {
        return QUETZAL_TEXTURE;
    }

    @Override
    public void render(@NotNull QuetzalEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model.setBeakOpen(entity.getBeakOpen(partialTick));
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    // Same value as the vanilla parrot renderer, passed to the model as ageInTicks
    @Override
    protected float getBob(@NotNull QuetzalEntity entity, float partialTick) {
        float flap = Mth.lerp(partialTick, entity.oFlap, entity.flap);
        float flapSpeed = Mth.lerp(partialTick, entity.oFlapSpeed, entity.flapSpeed);
        return (Mth.sin(flap) + 1.0F) * flapSpeed;
    }
}
