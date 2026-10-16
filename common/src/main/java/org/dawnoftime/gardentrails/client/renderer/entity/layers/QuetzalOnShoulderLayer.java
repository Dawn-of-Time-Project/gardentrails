package org.dawnoftime.gardentrails.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.dawnoftime.gardentrails.client.model.entity.QuetzalModel;
import org.dawnoftime.gardentrails.client.renderer.entity.QuetzalRenderer;
import org.dawnoftime.gardentrails.client.renderer.entity.state.QuetzalRenderState;
import org.dawnoftime.gardentrails.util.QuetzalShoulder;
import org.jetbrains.annotations.NotNull;

/**
 * Renders a quetzal sitting on a player shoulder. The vanilla parrot layer only knows the parrot variants,
 * so the shoulders of the quetzal are read from the synced attachment of the player entity (the render state has no room for them).
 */
public class QuetzalOnShoulderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final QuetzalModel model;

    public QuetzalOnShoulderLayer(RenderLayerParent<AvatarRenderState, PlayerModel> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new QuetzalModel(modelSet.bakeLayer(QuetzalModel.LAYER_LOCATION));
    }

    @Override
    public void submit(@NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int packedLight, @NotNull AvatarRenderState state, float yRot, float xRot) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(state.id);
        if (!(entity instanceof Player player)) {
            return;
        }
        if (QuetzalShoulder.isOnLeft(player)) {
            this.submitOnShoulder(poseStack, collector, packedLight, state, yRot, xRot, true);
        }
        if (QuetzalShoulder.isOnRight(player)) {
            this.submitOnShoulder(poseStack, collector, packedLight, state, yRot, xRot, false);
        }
    }

    private void submitOnShoulder(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, AvatarRenderState state, float yRot, float xRot, boolean leftShoulder) {
        poseStack.pushPose();
        poseStack.translate(leftShoulder ? 0.4F : -0.4F, state.isCrouching ? -1.3F : -1.5F, 0.0F);
        QuetzalRenderState quetzalState = new QuetzalRenderState();
        quetzalState.onShoulder = true;
        quetzalState.leftShoulder = leftShoulder;
        quetzalState.ageInTicks = state.ageInTicks;
        quetzalState.yRot = yRot;
        quetzalState.xRot = xRot;
        collector.submitModel(this.model, quetzalState, poseStack, this.model.renderType(QuetzalRenderer.QUETZAL_TEXTURE),
                packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
    }
}
