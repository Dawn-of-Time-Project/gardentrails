package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.platform.Services;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties;
import org.dawnoftime.gardentrails.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

import static org.dawnoftime.gardentrails.util.VoxelShapes.PERGOLA_COLLISION_SHAPES;

public class PergolaCropBlock extends PergolaBlock implements BonemealableBlock {
    private static final IntegerProperty AGE_6 = GTBlockStateProperties.AGE_6;
    private final int[] moonPhasePerAge;

    // The bare pergola this crop variant belongs to (used when reverting on harvest)
    private final Supplier<PergolaBlock> barePergola;

    public PergolaCropBlock(Properties properties, final int age2, final int age3, final int age4, final int age5, final int age6,
                            Supplier<PergolaBlock> barePergola) {
        // Crop variants don't need plant suppliers themselves — pass null
        super(properties, null, null, null);
        this.moonPhasePerAge = new int[]{age2, age3, age4, age5, age6};
        this.barePergola = barePergola;
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS_Y, false).setValue(AXIS_X, false).setValue(AXIS_Z, false).setValue(AGE_6, 0));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(AXIS_X) || state.getValue(AXIS_Z)) {
            return state.getValue(AXIS_Y) ? PERGOLA_COLLISION_SHAPES[2] : PERGOLA_COLLISION_SHAPES[1];
        }
        return PERGOLA_COLLISION_SHAPES[0];
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE_6);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    private boolean canGrowMoonCycle(final Level worldIn, final BlockPos pos, int currentAge) {
        if (currentAge < 2) {
            return false;
        }
        currentAge -= 2;
        final int currentPhase = worldIn.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos).index();
        return (currentPhase - this.moonPhasePerAge[currentAge] + 8) % 8 < 4;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if(!level.isClientSide()) {
            if(!level.isLoaded(pos))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light

            if(level.getRawBrightness(pos, 0) >= 8) {
                int age = state.getValue(AGE_6);
                if(age < 2){
                    if (random.nextInt(16) == 0) { //Probability "can grow"
                        level.setBlock(pos, state.setValue(AGE_6, age + 1), 2);
                    }
                } else {
                    if (this.canGrowMoonCycle(level, pos, age)) {
                        if (random.nextInt(16) == 0) { //Probability "can grow")
                            level.setBlock(pos, state.setValue(AGE_6, 2 + ((age - 1) % 5)), 2);
                        }
                    }
                    if (random.nextInt(5) == 0) { //Probability "can spread"
                        BlockPos adjacentPos = randomSpread(pos, random);
                        BlockState adjacentState = level.getBlockState(adjacentPos);
                        if(adjacentState.getBlock() == barePergola.get()) {
                            level.setBlock(adjacentPos, this.copyShapeToPergola(adjacentState, this), 2);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(AGE_6) > 2) {
            if (dropPlant(state, level, pos, player.getItemInHand(hand))) {
                level.setBlock(pos, state.setValue(AGE_6, 2), 2);
                level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        if (player.isCrouching()) {
            dropPlant(state, level, pos, player.getItemInHand(hand));
            level.setBlock(pos, this.copyShapeToPergola(state, barePergola.get()), 2);
            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    private boolean dropPlant(BlockState state, Level level, BlockPos pos, ItemStack heldItemStack) {
        if (level instanceof ServerLevel serverLevel) {
            Identifier blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            List<ItemStack> drops = Utils.getLootList(serverLevel, state, heldItemStack, blockId.getPath() + "_" + state.getValue(AGE_6));
            return Utils.dropLootFromList(level, pos, drops, 1.0F);
        } else {
            return false;
        }
    }

    @Override
    public void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull ItemStack stack, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        // The pergola comes from the block loot table, the plant from its own loot table (plant name and age)
        this.dropPlant(state, level, pos, stack);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return state.getValue(AGE_6) < 6;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        // Bonemeal bypasses the light and moon cycle conditions of the random tick
        int age = state.getValue(AGE_6);
        level.setBlock(pos, state.setValue(AGE_6, age < 2 ? age + 1 : 2 + ((age - 1) % 5)), 2);
    }
}
