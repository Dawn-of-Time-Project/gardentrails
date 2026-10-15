package org.dawnoftime.gardentrails.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GTArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(GTArrowEntity.class, EntityDataSerializers.INT);
    private static final String VARIANT_TAG = "Variant";

    // Used by the entity type factory (client spawn and NBT loading)
    public GTArrowEntity(EntityType<? extends GTArrowEntity> type, Level level) {
        super(type, level);
    }

    // Shot by a living entity (bow, crossbow)
    public GTArrowEntity(Level level, LivingEntity shooter, GTArrowVariant variant, ItemStack pickupStack, @Nullable ItemStack firedFrom) {
        super(GTEntitiesRegistry.INSTANCE.FEATHERED_ARROW_ENTITY.get(), shooter, level, pickupStack, firedFrom);
        this.setVariant(variant);
    }

    // Shot by a dispenser
    public GTArrowEntity(Level level, double x, double y, double z, GTArrowVariant variant, ItemStack pickupStack, @Nullable ItemStack firedFrom) {
        super(GTEntitiesRegistry.INSTANCE.FEATHERED_ARROW_ENTITY.get(), x, y, z, level, pickupStack, firedFrom);
        this.setVariant(variant);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, GTArrowVariant.RED.ordinal());
    }

    public GTArrowVariant getVariant() {
        return GTArrowVariant.byOrdinal(this.entityData.get(DATA_VARIANT));
    }

    // Also sets the base damage: only call it when the arrow is created, Power is added on top afterwards by the bow
    private void setVariant(GTArrowVariant variant) {
        this.entityData.set(DATA_VARIANT, variant.ordinal());
        this.setBaseDamage(variant.getBaseDamage());
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(this.getVariant().getItem());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString(VARIANT_TAG, this.getVariant().getId());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(VARIANT_TAG)) {
            this.entityData.set(DATA_VARIANT, GTArrowVariant.byId(compound.getString(VARIANT_TAG)).ordinal());
        }
    }
}
