package org.dawnoftime.gardentrails.block.templates;
import org.dawnoftime.gardentrails.block.IBlockTooltip;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.cauldron.CauldronInteractions;

import net.minecraft.world.item.Item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.dawnoftime.gardentrails.blockentity.SakeVatBlockEntity;
import org.dawnoftime.gardentrails.registry.GTBlockEntitiesRegistry;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class SakeVatBlock extends AbstractCauldronBlock implements EntityBlock, IBlockTooltip {
    public static final IntegerProperty LEVEL = GTBlockStateProperties.LEVEL_0_3;
    public static final BooleanProperty READY = GTBlockStateProperties.READY;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    /** Liquid surface height in pixels for each level, keep identical to the sake_vat_liquid models. */
    private static final int[] LIQUID_HEIGHTS = {0, 6, 10, 14};

    private static final VoxelShape VAT_SHAPE = Shapes.join(
            Shapes.or(
                    Block.box(0, 1, 0, 16, 16, 16),
                    Block.box(0, 0, 0, 3, 1, 3),
                    Block.box(13, 0, 0, 16, 1, 3),
                    Block.box(0, 0, 13, 3, 1, 16),
                    Block.box(13, 0, 13, 16, 1, 16)),
            Block.box(2, 2, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);

    /** The lid is shut for the whole fermentation, so the top is solid. */
    private static final VoxelShape CLOSED_SHAPE = Shapes.or(
            Block.box(0, 1, 0, 16, 16, 16),
            Block.box(0, 0, 0, 3, 1, 3),
            Block.box(13, 0, 0, 16, 1, 3),
            Block.box(0, 0, 13, 3, 1, 16),
            Block.box(13, 0, 13, 16, 1, 16));

    private static VoxelShape shapeFor(BlockState state) {
        return isClosed(state) ? CLOSED_SHAPE : VAT_SHAPE;
    }

    /** Closed while the 3 slots are filled and the fermentation runs. */
    public static boolean isClosed(BlockState state) {
        return state.getValue(LEVEL) == 3 && !state.getValue(READY);
    }

    public SakeVatBlock(Properties properties) {
        super(properties, CauldronInteractions.EMPTY);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0).setValue(READY, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, READY, FACING);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return shapeFor(state);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return shapeFor(state);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return shapeFor(state);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean isFull(@NotNull BlockState state) {
        return state.getValue(LEVEL) == 3;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return state.getValue(LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return GTBlockEntitiesRegistry.INSTANCE.SAKE_VAT.get().create(pos, state);
    }

    @Override
    public void appendTooltip(List<Component> tooltip) {
        TooltipHelper.label(tooltip, "lore.gardentrails.sake_process");
        TooltipHelper.lore(tooltip, "lore.gardentrails.sake_vat.base", TooltipHelper.name("item.minecraft.potion.effect.water"), TooltipHelper.name("item.gardentrails.fermented_rice"));
        TooltipHelper.lore(tooltip, "lore.gardentrails.sake_vat.ingredients", TooltipHelper.names("item.gardentrails.cooked_rice", "item.gardentrails.fermented_mulberry_juice", "item.gardentrails.crushed_grape", "item.gardentrails.crushed_maize", "item.gardentrails.tea_leaves"));
        TooltipHelper.lore(tooltip, "lore.gardentrails.sake_vat.collect", TooltipHelper.name("item.minecraft.glass_bottle"));
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND || !(level.getBlockEntity(pos) instanceof SakeVatBlockEntity vat)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        if (state.getValue(READY) && stack.is(Items.GLASS_BOTTLE)) {
            if (!level.isClientSide()) {
                vat.extractBottle(player, hand);
            }
            return InteractionResult.SUCCESS;
        }
        if (vat.canAccept(stack)) {
            if (!level.isClientSide()) {
                vat.accept(stack, player, hand);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.tick(state, level, pos, random);
        if (state.getValue(LEVEL) == 3 && !state.getValue(READY) && level.getBlockEntity(pos) instanceof SakeVatBlockEntity vat && vat.isFermenting()) {
            long remaining = vat.getFinishTime() - level.getGameTime();
            if (remaining <= 0) {
                level.setBlock(pos, state.setValue(READY, true), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
            } else {
                level.scheduleTick(pos, this, (int) remaining);
            }
        }
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        int liquidLevel = state.getValue(LEVEL);
        if (state.getValue(READY) && liquidLevel > 0) {
            double x = pos.getX() + 0.2 + random.nextDouble() * 0.6;
            double y = pos.getY() + LIQUID_HEIGHTS[liquidLevel] / 16.0 + 0.02;
            double z = pos.getZ() + 0.2 + random.nextDouble() * 0.6;
            level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isPathfindable(final BlockState state, final PathComputationType type) {
        return type != PathComputationType.LAND && super.isPathfindable(state, type);
    }
}
