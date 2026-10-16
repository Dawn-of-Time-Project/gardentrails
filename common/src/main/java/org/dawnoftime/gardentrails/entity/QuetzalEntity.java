package org.dawnoftime.gardentrails.entity;

import net.minecraft.core.BlockPos;
import org.dawnoftime.gardentrails.mixin.PlayerShoulderAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.dawnoftime.gardentrails.registry.GTTags;
import net.minecraft.server.level.ServerPlayer;
import org.dawnoftime.gardentrails.registry.GTItemsRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class QuetzalEntity extends ShoulderRidingEntity {

    // Same taming items as the vanilla parrot, each one has a 1 in 10 chance to tame
    // Only the quetzal offering can tame a quetzal, with a 1 in 3 chance
    private static final int TAME_CHANCE = 3;

    public float flap;
    public float oFlap;
    public float flapSpeed;
    public float oFlapSpeed;
    private float flapping = 1.0F;

    // Garden mode: the bird flies freely around an anchor point and speeds up the plants around it
    private static final int GARDEN_FLY_RADIUS = 8;
    private static final int BOOST_RADIUS_XZ = 12;
    private static final int BOOST_RADIUS_Y = 8;
    private static final int BOOST_PULSE_TICKS = 20;
    private static final int BOOST_PLANTS_PER_PULSE = 5;

    // Beak animation: the server sends this entity event when the bird makes a sound, the client then opens the beak for a few ticks
    private static final byte BEAK_EVENT = 100;
    private static final byte HURT_EVENT = 2;
    private static final int BEAK_ANIM_TICKS = 8;
    private int beakTicks;
    private int oBeakTicks;

    private boolean gardenMode;
    private BlockPos gardenAnchor;
    // Random initial offset so several birds do not scan on the same tick
    private int boostTimer = this.random.nextInt(BOOST_PULSE_TICKS);

    public QuetzalEntity(EntityType<? extends QuetzalEntity> type, Level level) {
        super(type, level);
        // Same movement setup as the vanilla parrot
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
        this.setPathfindingMalus(PathType.FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new GardenAwareFollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new GardenAwareLandOnShoulderGoal(this));
    }

    // In garden mode the bird neither follows its owner nor lands on the shoulder
    private static class GardenAwareFollowOwnerGoal extends FollowOwnerGoal {
        private final QuetzalEntity quetzal;

        GardenAwareFollowOwnerGoal(QuetzalEntity quetzal, double speed, float start, float stop, boolean canFly) {
            super(quetzal, speed, start, stop);
            this.quetzal = quetzal;
        }

        @Override
        public boolean canUse() {
            return !this.quetzal.gardenMode && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.quetzal.gardenMode && super.canContinueToUse();
        }
    }

    private static class GardenAwareLandOnShoulderGoal extends LandOnOwnersShoulderGoal {
        private final QuetzalEntity quetzal;

        GardenAwareLandOnShoulderGoal(QuetzalEntity quetzal) {
            super(quetzal);
            this.quetzal = quetzal;
        }

        @Override
        public boolean canUse() {
            return !this.quetzal.gardenMode && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.quetzal.gardenMode && super.canContinueToUse();
        }
    }

    private void setGardenMode(boolean enabled) {
        this.gardenMode = enabled;
        if (enabled) {
            this.gardenAnchor = this.blockPosition();
            this.setHomeTo(this.gardenAnchor, GARDEN_FLY_RADIUS);
        } else {
            this.gardenAnchor = null;
            this.clearHome();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("GardenMode", this.gardenMode);
        if (this.gardenMode && this.gardenAnchor != null) {
            output.putInt("GardenAnchorX", this.gardenAnchor.getX());
            output.putInt("GardenAnchorY", this.gardenAnchor.getY());
            output.putInt("GardenAnchorZ", this.gardenAnchor.getZ());
        }
    }

    // The restriction is not saved by vanilla, so it is restored from the saved anchor
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        if (input.getBooleanOr("GardenMode", false) && input.getInt("GardenAnchorX").isPresent()) {
            this.gardenMode = true;
            this.gardenAnchor = new BlockPos(input.getIntOr("GardenAnchorX", 0), input.getIntOr("GardenAnchorY", 0), input.getIntOr("GardenAnchorZ", 0));
            this.setHomeTo(this.gardenAnchor, GARDEN_FLY_RADIUS);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.isTame() && stack.is(GTItemsRegistry.INSTANCE.QUETZAL_OFFERING.get())) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (!this.isSilent()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
            if (!this.level().isClientSide()) {
                this.level().broadcastEntityEvent(this, BEAK_EVENT);
                if (this.random.nextInt(TAME_CHANCE) == 0) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.SUCCESS;
        }
        // A flying bird cannot be clicked, except in garden mode so the owner can bring it back
        if ((!this.isFlying() || this.gardenMode) && this.isTame() && this.isOwnedBy(player)) {
            if (!this.level().isClientSide()) {
                // Cycle: follow -> sit -> garden -> follow
                if (this.gardenMode) {
                    this.setGardenMode(false);
                } else if (this.isOrderedToSit()) {
                    this.setOrderedToSit(false);
                    this.setInSittingPose(false);
                    this.setGardenMode(true);
                    player.sendSystemMessage(Component.translatable("message.gardentrails.quetzal_garden"));
                } else {
                    this.setOrderedToSit(true);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isInvulnerableTo(level, source)) {
            return false;
        }
        this.setOrderedToSit(false);
        return super.hurtServer(level, source, amount);
    }

    // Vanilla always fills the left shoulder first. Here the right shoulder is preferred, the left one is used only if the right one is taken.
    // The left shoulder is blocked with a temporary placeholder so the vanilla method picks the right one and keeps its timers and checks.
    @Override
    public boolean setEntityOnShoulder(ServerPlayer player) {
        if (player.getShoulderEntityLeft().isEmpty() && player.getShoulderEntityRight().isEmpty()) {
            CompoundTag placeholder = new CompoundTag();
            placeholder.putBoolean("placeholder", true);
            ((PlayerShoulderAccessor) player).gt$setShoulderEntityLeft(placeholder);
            boolean success = super.setEntityOnShoulder(player);
            ((PlayerShoulderAccessor) player).gt$setShoulderEntityLeft(new CompoundTag());
            return success;
        }
        return super.setEntityOnShoulder(player);
    }

    // The quetzal cannot breed
    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canMate(Animal other) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }

    // Prefers air so the random flying goal picks positions in the open
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    // Same rule as the vanilla parrot: logs, leaves, grass or air below, and enough light
    public static boolean checkQuetzalSpawnRules(EntityType<QuetzalEntity> type, LevelAccessor level, EntitySpawnReason spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        return navigation;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();
        this.oBeakTicks = this.beakTicks;
        if (this.beakTicks > 0) {
            this.beakTicks--;
        }
    }

    @Override
    public void playAmbientSound() {
        super.playAmbientSound();
        if (!this.isSilent()) {
            this.level().broadcastEntityEvent(this, BEAK_EVENT);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == BEAK_EVENT || id == HURT_EVENT) {
            this.beakTicks = BEAK_ANIM_TICKS;
            this.oBeakTicks = BEAK_ANIM_TICKS;
        }
        super.handleEntityEvent(id);
    }

    /** Beak opening from 0 (closed) to 1 (fully open), a smooth open and close over the animation duration. */
    public float getBeakOpen(float partialTick) {
        if (this.beakTicks == 0 && this.oBeakTicks == 0) {
            return 0.0F;
        }
        float progress = 1.0F - Mth.lerp(partialTick, this.oBeakTicks, this.beakTicks) / BEAK_ANIM_TICKS;
        return Mth.sin(Mth.clamp(progress, 0.0F, 1.0F) * (float) Math.PI);
    }

    // Copied from the vanilla parrot: wing flap phase and speed, slowed fall while flying
    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (this.onGround() ? -1 : 4) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }
        this.flapping *= 0.9F;
        Vec3 motion = this.getDeltaMovement();
        if (!this.onGround() && motion.y < 0.0D) {
            this.setDeltaMovement(motion.multiply(1.0D, 0.6D, 1.0D));
        }
        this.flap += this.flapping * 2.0F;
    }

    // Like the silkmoth eggs, the quetzal sheds a random feather from time to time while flying (about once per minute)
    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        if (this.isFlying() && this.random.nextInt(1200) == 0) {
            Item feather = switch (this.random.nextInt(4)) {
                case 0 -> Items.FEATHER;
                case 1 -> GTItemsRegistry.INSTANCE.BLUE_FEATHER.get();
                case 2 -> GTItemsRegistry.INSTANCE.GREEN_FEATHER.get();
                default -> GTItemsRegistry.INSTANCE.RED_FEATHER.get();
            };
            this.spawnAtLocation(serverLevel, feather);
        }
        if (this.gardenMode && this.isTame() && --this.boostTimer <= 0) {
            this.boostTimer = BOOST_PULSE_TICKS;
            this.boostSurroundingPlants(serverLevel);
        }
    }

    // Picks uniformly BOOST_PLANTS_PER_PULSE boosted plants around the bird (reservoir sampling, no list kept)
    // and gives each one a regular random tick, so the natural growth rules stay in charge.
    private void boostSurroundingPlants(ServerLevel level) {
        BlockPos center = this.blockPosition();
        int minX = center.getX() - BOOST_RADIUS_XZ;
        int maxX = center.getX() + BOOST_RADIUS_XZ;
        int minZ = center.getZ() - BOOST_RADIUS_XZ;
        int maxZ = center.getZ() + BOOST_RADIUS_XZ;
        int minY = Math.max(center.getY() - BOOST_RADIUS_Y, level.getMinY());
        int maxY = Math.min(center.getY() + BOOST_RADIUS_Y, level.getMaxY());

        BlockPos[] picked = new BlockPos[BOOST_PLANTS_PER_PULSE];
        int seen = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++) {
            for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++) {
                // Never force a chunk to load
                LevelChunk chunk = level.getChunkSource().getChunkNow(chunkX, chunkZ);
                if (chunk == null) {
                    continue;
                }
                for (int index = chunk.getSectionIndex(minY); index <= chunk.getSectionIndex(maxY); index++) {
                    LevelChunkSection section = chunk.getSection(index);
                    if (section.hasOnlyAir() || !section.maybeHas(state -> state.is(GTTags.INSTANCE.QUETZAL_BOOSTED))) {
                        continue;
                    }
                    int sectionMinY = chunk.getSectionYFromSectionIndex(index) << 4;
                    for (int x = Math.max(minX, chunkX << 4); x <= Math.min(maxX, (chunkX << 4) + 15); x++) {
                        for (int z = Math.max(minZ, chunkZ << 4); z <= Math.min(maxZ, (chunkZ << 4) + 15); z++) {
                            for (int y = Math.max(minY, sectionMinY); y <= Math.min(maxY, sectionMinY + 15); y++) {
                                BlockState state = section.getBlockState(x & 15, y & 15, z & 15);
                                if (!state.is(GTTags.INSTANCE.QUETZAL_BOOSTED) || !state.isRandomlyTicking()) {
                                    continue;
                                }
                                if (seen < BOOST_PLANTS_PER_PULSE) {
                                    picked[seen] = cursor.set(x, y, z).immutable();
                                } else {
                                    int slot = this.random.nextInt(seen + 1);
                                    if (slot < BOOST_PLANTS_PER_PULSE) {
                                        picked[slot] = cursor.set(x, y, z).immutable();
                                    }
                                }
                                seen++;
                            }
                        }
                    }
                }
            }
        }
        // The world is only modified once the scan is over
        for (BlockPos pos : picked) {
            if (pos != null) {
                BlockState state = level.getBlockState(pos);
                if (state.isRandomlyTicking()) {
                    state.randomTick(level, pos, level.getRandom());
                }
            }
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    public static AttributeSupplier.Builder createAttributes() {
        // Same values as the vanilla parrot
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.FLYING_SPEED, 0.4D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }

    public boolean isFlying() {
        return !this.onGround();
    }
}
