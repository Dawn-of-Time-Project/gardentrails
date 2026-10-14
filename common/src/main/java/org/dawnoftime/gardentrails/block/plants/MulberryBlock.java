package org.dawnoftime.gardentrails.block.plants;

import org.dawnoftime.gardentrails.util.TooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.properties.Half;
import org.dawnoftime.gardentrails.block.templates.DoubleGrowingBushBlock;
import org.dawnoftime.gardentrails.platform.Services;
import org.dawnoftime.gardentrails.registry.GTEntitiesRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class MulberryBlock extends DoubleGrowingBushBlock {
    public MulberryBlock(PlantType plantType, int cutAge, int growingAge) {
        super(plantType, cutAge, growingAge);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        TooltipHelper.label(tooltip, "lore.gardentrails.silk_process");
        TooltipHelper.lore(tooltip, "lore.gardentrails.mulberry", TooltipHelper.name("item.gardentrails.mulberry_leaves"));
        TooltipHelper.lore(tooltip, "lore.gardentrails.mulberry.silkmoths", TooltipHelper.name("item.gardentrails.silk_worm_eggs"));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        super.randomTick(state, worldIn, pos, random);
        if(state.getValue(HALF) == Half.TOP) {
            if(random.nextInt(400) == 0) {
                GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY.get().spawn(worldIn, (ItemStack) null, null, pos, MobSpawnType.SPAWNER, false, true);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    @Override
    public boolean isPathfindable(final BlockState state, final BlockGetter level, final BlockPos pos, final PathComputationType type) {
        return type != PathComputationType.LAND && super.isPathfindable(state, level, pos, type);
    }
}
