package org.dawnoftime.gardentrails.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.dawnoftime.gardentrails.client.model.entity.QuetzalModel;
import org.dawnoftime.gardentrails.client.renderer.entity.QuetzalRenderer;
import org.dawnoftime.gardentrails.entity.QuetzalEntity;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * Renders a quetzal sitting on a player shoulder. The vanilla parrot layer only accepts the parrot entity type,
 * so this one does the same for the quetzal.
 */
public class QuetzalOnShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final QuetzalModel<QuetzalEntity> model;

    public QuetzalOnShoulderLayer(RenderLayerParent<T, PlayerModel<T>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new QuetzalModel<>(modelSet.bakeLayer(QuetzalModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        this.render(poseStack, buffer, packedLight, player, netHeadYaw, headPitch, true);
        this.render(poseStack, buffer, packedLight, player, netHeadYaw, headPitch, false);
    }

    private void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T player, float netHeadYaw, float headPitch, boolean leftShoulder) {
        CompoundTag tag = leftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        if (EntityType.byString(tag.getString("id")).filter(type -> type == GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get()).isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(leftShoulder ? 0.4F : -0.4F, player.isCrouching() ? -1.3F : -1.5F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(this.model.renderType(QuetzalRenderer.QUETZAL_TEXTURE));
        this.model.renderOnShoulder(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, netHeadYaw, headPitch, leftShoulder);
        poseStack.popPose();
    }
}
