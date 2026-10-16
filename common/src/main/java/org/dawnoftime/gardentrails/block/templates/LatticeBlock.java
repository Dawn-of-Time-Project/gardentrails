package org.dawnoftime.gardentrails.block.templates;

import org.dawnoftime.gardentrails.block.IBlockTooltip;
import net.minecraft.world.item.Item;

import org.dawnoftime.gardentrails.util.TooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.dawnoftime.gardentrails.block.IBlockClimbingPlant;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties.ClimbingPlant;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static org.dawnoftime.gardentrails.util.VoxelShapes.LATTICE_SHAPES;

public class LatticeBlock extends WaterloggedBlock implements IBlockClimbingPlant, BonemealableBlock, IBlockTooltip {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final EnumProperty<ClimbingPlant> CLIMBING_PLANT = GTBlockStateProperties.CLIMBING_PLANT;
    public static final IntegerProperty AGE = GTBlockStateProperties.AGE_6;

    public LatticeBlock(Properties properties) {
        super(properties, LATTICE_SHAPES);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(CLIMBING_PLANT, ClimbingPlant.NONE)
                .setValue(AGE, 0)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, CLIMBING_PLANT, AGE);
    }

    /**
     * Binary index: SOUTH = 1, WEST = 2, NORTH = 4, EAST = 8. The index 15 (four sides) uses the shape 0.
     */
    @Override
    public int getShapeIndex(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int index = 0;
        if (state.getValue(SOUTH)) index += 1;
        if (state.getValue(WEST)) index += 2;
        if (state.getValue(NORTH)) index += 4;
        if (state.getValue(EAST)) index += 8;
        return index > 14 ? 0 : index;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.getBlock() != this) {
            state = super.getStateForPlacement(context);
            if (state == null) {
                return null;
            }
        }
        return switch (context.getHorizontalDirection()) {
            case WEST -> state.setValue(WEST, true);
            case NORTH -> state.setValue(NORTH, true);
            case EAST -> state.setValue(EAST, true);
            default -> state.setValue(SOUTH, true);
        };
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext useContext) {
        if (useContext.getPlayer() != null && useContext.getPlayer().isCrouching()) {
            return false;
        }
        if (useContext.getItemInHand().getItem() == this.asItem()) {
            return switch (useContext.getHorizontalDirection()) {
                case WEST -> !state.getValue(WEST);
                case NORTH -> !state.getValue(NORTH);
                case EAST -> !state.getValue(EAST);
                default -> !state.getValue(SOUTH);
            };
        }
        return false;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rotation) {
        return state.setValue(NORTH, state.getValue(sideProperty(rotation.rotate(Direction.NORTH))))
                .setValue(EAST, state.getValue(sideProperty(rotation.rotate(Direction.EAST))))
                .setValue(SOUTH, state.getValue(sideProperty(rotation.rotate(Direction.SOUTH))))
                .setValue(WEST, state.getValue(sideProperty(rotation.rotate(Direction.WEST))));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation(Direction.NORTH));
    }

    private static BooleanProperty sideProperty(Direction direction) {
        return switch (direction) {
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return !state.getValue(CLIMBING_PLANT).hasNoPlant();
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.tickPlant(state, level, pos, random);
    }

    @Override
    public void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull ItemStack stack, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        // Climbing plants do not drop from the block loot table but from their own loot table (plant name and age)
        this.dropPlant(state, level, pos, stack);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.tryPlacingPlant(state, level, pos, player, hand)) {
            return InteractionResult.SUCCESS;
        }
        return this.harvestPlant(state, level, pos, player, hand);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, BonemealSource source) {
        ClimbingPlant plant = state.getValue(CLIMBING_PLANT);
        return !plant.hasNoPlant() && state.getValue(AGE) < plant.maxAge();
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state, BonemealSource source) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state, BonemealSource source) {
        // Bonemeal bypasses the light and moon cycle conditions of the random tick
        int age = state.getValue(AGE);
        level.setBlock(pos, state.setValue(AGE, age < 2 ? age + 1 : 2 + ((age - 1) % 5)), 2);
    }

    @Override
    public void appendTooltip(List<Component> tooltip) {
        TooltipHelper.label(tooltip, "lore.gardentrails.pergola.label");
        TooltipHelper.lore(tooltip, "lore.gardentrails.pergola.accepts", TooltipHelper.names(
                "item.gardentrails.grape_seeds", "block.gardentrails.ivy", "block.minecraft.vine"));
        TooltipHelper.lore(tooltip, "lore.gardentrails.pergola.usage");
    }

    @Override
    protected boolean blocksLandPathing(final BlockState state) {
        return true;
    }
}
