package org.dawnoftime.gardentrails.block.templates;

import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.block.IBlockClimbingPlant;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties.ClimbingPlant;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static org.dawnoftime.gardentrails.util.VoxelShapes.PERGOLA_COLLISION_SHAPES;
import static org.dawnoftime.gardentrails.util.VoxelShapes.PERGOLA_SHAPES;

/**
 * One block per metal. The climbing plant (vine, ivy, grape) is a state property, like the lattice.
 * The former blocks "metal_pergola_vine, _ivy and _grape" are aliased to this block by the loaders.
 */
public class PergolaBlock extends BlockGT implements IBlockClimbingPlant {
    public static final BooleanProperty AXIS_X = GTBlockStateProperties.AXIS_X;
    public static final BooleanProperty AXIS_Y = GTBlockStateProperties.AXIS_Y;
    public static final BooleanProperty AXIS_Z = GTBlockStateProperties.AXIS_Z;
    public static final EnumProperty<ClimbingPlant> CLIMBING_PLANT = GTBlockStateProperties.CLIMBING_PLANT;
    public static final IntegerProperty AGE = GTBlockStateProperties.AGE_6;

    public PergolaBlock(Properties properties) {
        super(properties, PERGOLA_SHAPES);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(AXIS_Y, false).setValue(AXIS_X, false).setValue(AXIS_Z, false)
                .setValue(CLIMBING_PLANT, ClimbingPlant.NONE).setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS_Y, AXIS_X, AXIS_Z, CLIMBING_PLANT, AGE);
    }

    @Override
    public int getShapeIndex(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if(state.getValue(AXIS_Y)) {
            int index = 3;
            if(state.getValue(AXIS_X))
                index += 1;
            if(state.getValue(AXIS_Z))
                index += 2;
            return index;
        } else {
            int index = state.getValue(AXIS_Z) ? 1 : 0;
            return state.getValue(AXIS_X) ? index * 2 : index;
        }
    }

    /** A pergola holding a plant has a smaller selection box than its collision box. */
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(CLIMBING_PLANT).hasNoPlant()) {
            return super.getShape(state, level, pos, context);
        }
        if (state.getValue(AXIS_X) || state.getValue(AXIS_Z)) {
            return state.getValue(AXIS_Y) ? PERGOLA_COLLISION_SHAPES[2] : PERGOLA_COLLISION_SHAPES[1];
        }
        return PERGOLA_COLLISION_SHAPES[0];
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if(state.getBlock() != this) {
            state = super.getStateForPlacement(context);
            if (state == null) {
                return null;
            }
        }
        return switch (context.getClickedFace().getAxis()) {
            case X -> state.setValue(AXIS_X, true);
            case Z -> state.setValue(AXIS_Z, true);
            default -> state.setValue(AXIS_Y, true);
        };
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext useContext) {
        ItemStack itemstack = useContext.getItemInHand();
        if(useContext.getPlayer() != null && useContext.getPlayer().isCrouching())
            return false;
        if(itemstack.getItem() == this.asItem()) {
            return switch (useContext.getClickedFace().getAxis()) {
                case X -> !state.getValue(AXIS_X);
                case Y -> !state.getValue(AXIS_Y);
                case Z -> !state.getValue(AXIS_Z);
            };
        }
        return false;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        boolean x = state.getValue(AXIS_X);
        boolean z = state.getValue(AXIS_Z);
        return switch (rotation) {
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> state.setValue(AXIS_X, z).setValue(AXIS_Z, x);
            default -> state;
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
    protected ItemInteractionResult useItemOn(ItemStack usedStack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (this.tryPlacingPlant(state, level, pos, player, hand)) {
            return ItemInteractionResult.SUCCESS;
        }
        return this.harvestPlant(state, level, pos, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.TooltipContext worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        TooltipHelper.label(tooltip, "lore.gardentrails.pergola.label");
        TooltipHelper.lore(tooltip, "lore.gardentrails.pergola.accepts", TooltipHelper.names(
                "item.gardentrails.grape_seeds", "block.gardentrails.ivy", "block.minecraft.vine"));
        TooltipHelper.lore(tooltip, "lore.gardentrails.pergola.usage");
    }

    public static BlockPos randomSpread(BlockPos pos, RandomSource random) {
        return switch (random.nextInt(5)) {
            case 0 -> pos.north();
            case 1 -> pos.east();
            case 2 -> pos.south();
            case 3 -> pos.west();
            default -> pos.above();
        };
    }

    @Override
    protected boolean blocksLandPathing(final BlockState state) {
        return true;
    }
}
