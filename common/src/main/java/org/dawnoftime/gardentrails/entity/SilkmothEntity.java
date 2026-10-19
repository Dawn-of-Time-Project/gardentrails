package org.dawnoftime.gardentrails.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.entity.EntitySpawnReason;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.block.templates.DoubleGrowingBushBlock;
import org.dawnoftime.gardentrails.platform.Services;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.registry.GTItemsRegistry;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SilkmothEntity extends AmbientCreature {
    private static final int PITILESS_KILLS = 10;
    private static final Identifier PITILESS_ADVANCEMENT = Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "pitiless");
    private static final String PITILESS_CRITERION = "kill_silkmoths";
    private static final EntityDataAccessor<BlockPos> ROTATION_POS = SynchedEntityData.defineId(SilkmothEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> CLOCKWISE = SynchedEntityData.defineId(SilkmothEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DISTANCE = SynchedEntityData.defineId(SilkmothEntity.class, EntityDataSerializers.FLOAT);

    public SilkmothEntity(Level worldIn) {
        super(GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY.get(), worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D);
    }

    private float getNewRotationDistance() {
        return 0.5F + 2 * this.random.nextFloat();
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyInstance, EntitySpawnReason spawnReason, @Nullable SpawnGroupData data) {
        this.getEntityData().set(ROTATION_POS, this.blockPosition());
        return super.finalizeSpawn(world, difficultyInstance, spawnReason, data);
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);

        if(this.tickCount >= 24000){
            //The silkmoth dies from oldness.
            if(!this.hasCustomName())
                this.hurtServer(serverLevel, this.damageSources().starve(), 20.0F);
        }

        if(this.random.nextInt(400) == 0){
            //Randomly changes the rotation pos.
            this.changeRotationPos();
        }

        // ~3 silk worm egg drops over a full lifetime (24000 ticks)
        if(this.random.nextInt(8000) == 0){
            this.spawnAtLocation(serverLevel, GTItemsRegistry.INSTANCE.SILK_WORM_EGGS.get());
        }

        BlockPos pos = this.getEntityData().get(ROTATION_POS);
        double distance = this.getEntityData().get(DISTANCE);
        double x = this.getX() - (pos.getX() + 0.5D);
        double z = this.getZ() - (pos.getZ() + 0.5D);

        double alpha;
        if(z == 0)
            alpha = (x > 0) ? 0 : Math.PI;
        else{
            alpha = Math.atan(z / x);
            if(x > 0)
                alpha += Math.PI;
        }
        double d = Math.sqrt(x * x + z * z);
        d = - distance * 2 / (d + distance) + 1;
        alpha += (this.getEntityData().get(CLOCKWISE) ? d - 1 : 1 - d) * Math.PI / 2;

        Vec3 motionVector = this.getDeltaMovement();
        this.setDeltaMovement(motionVector.x * 0.5D + Math.cos(alpha) * 0.15D, Math.sin(this.tickCount / 20.0D) * 0.05D, motionVector.z * 0.5D + Math.sin(alpha) * 0.15D);
        float rot = (float) Mth.wrapDegrees(180.0D * alpha / Math.PI - 90.0D);
        this.setYHeadRot(rot);
        this.setYRot(rot);
    }

    private void changeRotationPos(){
        int horizontalRange = 5;
        int verticalRange = 2;

        int x = (int) this.level().getDefaultClockTime() % 23999;
        boolean isNight = x > 12000 && x < 23000;
        x = (int) Math.floor(this.getX()) - horizontalRange;
        int y = (int) Math.floor(this.getY()) - verticalRange;
        int z = (int) Math.floor(this.getZ()) - horizontalRange;

        List<BlockPos> listMulberry = new ArrayList<>();
        List<BlockPos> listLight = new ArrayList<>();
        BlockState state;

        for(int searchX = 0; searchX < 2 * horizontalRange + 1; searchX++){
            for(int searchZ = 0; searchZ < 2 * horizontalRange + 1; searchZ++){
                for(int searchY = 0; searchY < 2 * verticalRange + 1; searchY++){

                    BlockPos pos = new BlockPos(x + searchX, y + searchY, z + searchZ);
                    state = this.level().getBlockState(pos);

                    if(state.getBlock() == GTBlocksRegistry.INSTANCE.MULBERRY.get()){
                        if(!((DoubleGrowingBushBlock) state.getBlock()).isBottomHalf(state)) listMulberry.add(pos);
                    }else if(isNight){
                        if(state.getLightEmission() >= 14) listLight.add(pos);
                    }

                }
            }
        }

        if(!listLight.isEmpty()){
            this.getEntityData().set(ROTATION_POS, listLight.get(random.nextInt(listLight.size())));
        }else if(!listMulberry.isEmpty()){
            this.getEntityData().set(ROTATION_POS, listMulberry.get(random.nextInt(listMulberry.size())));
        }else this.getEntityData().set(ROTATION_POS, new BlockPos(x + this.random.nextInt(2 * horizontalRange + 1), y + this.random.nextInt(2 * verticalRange + 1), z + this.random.nextInt(2 * horizontalRange + 1)));

        this.getEntityData().set(DISTANCE, this.getNewRotationDistance());
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.9F;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return this.random.nextInt(4) == 0 ? SoundEvents.PARROT_FLY : null;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        // The vanilla kill statistic is already incremented by super.die(), so it is persistent per player
        if (!this.level().isClientSide() && source.getEntity() instanceof ServerPlayer player
                && player.getStats().getValue(Stats.ENTITY_KILLED.get(this.getType())) >= PITILESS_KILLS) {
            AdvancementHolder advancement = player.level().getServer().getAdvancements().get(PITILESS_ADVANCEMENT);
            if (advancement != null) {
                player.getAdvancements().award(advancement, PITILESS_CRITERION);
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PARROT_FLY;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_FLY;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ROTATION_POS, this.blockPosition());
        builder.define(CLOCKWISE, this.random.nextBoolean());
        builder.define(DISTANCE, this.getNewRotationDistance());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.getEntityData().set(ROTATION_POS, new BlockPos(input.getIntOr("RotationX", 0), input.getIntOr("RotationY", 0), input.getIntOr("RotationZ", 0)));
        this.getEntityData().set(CLOCKWISE, input.getBooleanOr("RotationClockwise", false));
        this.getEntityData().set(DISTANCE, input.getFloatOr("RotationDistance", this.getNewRotationDistance()));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("RotationX", this.getEntityData().get(ROTATION_POS).getX());
        output.putInt("RotationY", this.getEntityData().get(ROTATION_POS).getY());
        output.putInt("RotationZ", this.getEntityData().get(ROTATION_POS).getZ());
        output.putBoolean("RotationClockwise", this.getEntityData().get(CLOCKWISE));
        output.putFloat("RotationDistance", this.getEntityData().get(DISTANCE));
    }
}