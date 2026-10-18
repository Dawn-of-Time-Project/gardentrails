package org.dawnoftime.gardentrails.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.dawnoftime.gardentrails.client.PlanterClient;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.util.PlanterContents;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Remembers the potted block shown in a flower planter. The planter is a single block: the baked model of the loaders
 * reads this value to add the plant to the chunk mesh, so every plant of every mod works without any model file.
 */
public class FlowerPlanterBlockEntity extends BlockEntity {
    private static final String POTTED_TAG = "Potted";

    // Replaced as a whole and never modified, the chunk builder threads read it
    @Nullable
    private volatile Block potted;

    public FlowerPlanterBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntitiesRegistry.INSTANCE.FLOWER_PLANTER.get(), pos, state);
    }

    @Nullable
    public Block getPotted() {
        return this.potted;
    }

    public boolean isEmpty() {
        return this.potted == null;
    }

    /** Server side change of the content, saved and sent to the clients. */
    public void setPotted(@Nullable Block potted) {
        this.potted = potted;
        this.setChanged();
        Level level = this.getLevel();
        if (level != null) {
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    /** Called on the client when the content changed, the chunk mesh has to be rebuilt. Forge also refreshes its model data. */
    protected void onContentChangedOnClient() {
        PlanterClient.refresh(this.worldPosition);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        // An empty planter writes an empty id: an empty update tag is not delivered to the clients
        Block current = this.potted;
        tag.putString(POTTED_TAG, current == null ? "" : BuiltInRegistries.BLOCK.getKey(current).toString());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        Block loaded = null;
        if (!tag.getString(POTTED_TAG).isEmpty()) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString(POTTED_TAG));
            // A block of a mod that was removed or a potted block without content leaves an empty planter
            if (id != null && BuiltInRegistries.BLOCK.containsKey(id)) {
                Block block = BuiltInRegistries.BLOCK.get(id);
                loaded = PlanterContents.itemOf(block) != null ? block : null;
            }
        }
        boolean changed = loaded != this.potted;
        this.potted = loaded;
        if (changed && this.level != null && this.level.isClientSide()) {
            this.onContentChangedOnClient();
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
