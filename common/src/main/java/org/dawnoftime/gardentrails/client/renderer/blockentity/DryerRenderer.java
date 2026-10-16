package org.dawnoftime.gardentrails.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.dawnoftime.gardentrails.blockentity.DryerBlockEntity;
import org.jspecify.annotations.Nullable;

public class DryerRenderer implements BlockEntityRenderer<DryerBlockEntity, DryerRenderer.DryerRenderState> {
    private static final int COPIES = 4;

    private final ItemModelResolver itemModelResolver;

    public DryerRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    /** Render state: one item render state per copy of each of the two slots. */
    public static class DryerRenderState extends BlockEntityRenderState {
        public final ItemStackRenderState[][] slots = new ItemStackRenderState[2][COPIES];

        public DryerRenderState() {
            for (ItemStackRenderState[] slot : this.slots) {
                for (int i = 0; i < COPIES; i++) {
                    slot[i] = new ItemStackRenderState();
                }
            }
        }
    }

    @Override
    public DryerRenderState createRenderState() {
        return new DryerRenderState();
    }

    @Override
    public void extractRenderState(DryerBlockEntity blockEntity, DryerRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        final int seed = (int) blockEntity.getBlockPos().asLong();
        for (int slot = 0; slot < 2; slot++) {
            final ItemStack stack = blockEntity.itemHandler.getItem(slot);
            for (int i = 0; i < COPIES; i++) {
                final ItemStackRenderState state = renderState.slots[slot][i];
                state.clear();
                if (!stack.isEmpty()) {
                    this.itemModelResolver.updateForTopItem(state, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + i);
                }
            }
        }
    }

    @Override
    public void submit(DryerRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        for (int slot = 0; slot < 2; slot++) {
            for (int i = 0; i < COPIES; i++) {
                final ItemStackRenderState itemState = renderState.slots[slot][i];
                if (itemState.isEmpty()) {
                    continue;
                }
                poseStack.pushPose();
                poseStack.translate(0.0D, 0.5D * slot, 0.0D);
                poseStack.translate(0.35D, 0, 0.35D);
                poseStack.translate((i == 1 || i == 2) ? 0.3D : 0, 0.1D, i >= 2 ? 0.3D : 0);
                poseStack.mulPose(Axis.YN.rotationDegrees(90.0F * i));
                poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
                poseStack.scale(0.3F, 0.3F, 0.3F);
                itemState.submit(poseStack, nodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }
    }
}
