package org.dawnoftime.gardentrails.util;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.registry.GTItemsRegistry;

public class GTBlockStateProperties {
    public static final BooleanProperty CUT = BooleanProperty.create("cut");
    public static final BooleanProperty AXIS_X = BooleanProperty.create("axis_x");
    public static final BooleanProperty AXIS_Y = BooleanProperty.create("axis_y");
    public static final BooleanProperty AXIS_Z = BooleanProperty.create("axis_z");
    public static final IntegerProperty MULTIBLOCK_3X = IntegerProperty.create("multiblock_3x", 0, 2);
    public static final IntegerProperty MULTIBLOCK_2Y = IntegerProperty.create("multiblock_2y", 0, 1);
    public static final IntegerProperty MULTIBLOCK_3Z = IntegerProperty.create("multiblock_3z", 0, 2);
    public static final IntegerProperty SIZE_0_2 = IntegerProperty.create("size", 0, 2);
    public static final IntegerProperty SIZE_0_5 = IntegerProperty.create("size", 0, 5);
    public static final IntegerProperty AGE_6 = IntegerProperty.create("age", 0, 6);
    public static final IntegerProperty LEVEL_0_3 = IntegerProperty.create("level", 0, 3);
    public static final BooleanProperty READY = BooleanProperty.create("ready");

    public static final EnumProperty<ClimbingPlant> CLIMBING_PLANT = EnumProperty.create("climbing_plant", ClimbingPlant.class);

    public enum ClimbingPlant implements StringRepresentable {
        NONE("none"),
        VINE("vine"),
        IVY("ivy"),
        GRAPE("grape", true, 4, 6, 0, 2, 2);

        private final String name;
        private final boolean cycle;
        private final int[] moonPhasePerAge;

        /**
         * These plants grow from age 0 to 2 (or 6 if they follow a cycle).
         * Ages 0 and 1 are the childhood. A cyclic plant then goes from 2 to 6 and comes back to 2.
         * The growth after age 2 is only allowed if the moon phase is between the required phase and 3 phases later.
         * Vanilla moon phases: 0 is full moon and 4 is new moon.
         */
        ClimbingPlant(String name, boolean cycle, int age2, int age3, int age4, int age5, int age6) {
            this.name = name;
            this.cycle = cycle;
            this.moonPhasePerAge = new int[]{age2, age3, age4, age5, age6};
        }

        ClimbingPlant(String name) {
            this(name, false, 0, 0, 0, 0, 0);
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public int maxAge() {
            return this.cycle ? 6 : 2;
        }

        public boolean canGrow(Level level, int currentAge) {
            if (!this.cycle || currentAge < 2 || currentAge > 6) {
                return false;
            }
            int phase = level.dimensionType().moonPhase(level.getDayTime());
            return (phase - this.moonPhasePerAge[currentAge - 2] + 8) % 8 < 4;
        }

        public boolean hasNoPlant() {
            return this == NONE;
        }

        public static ClimbingPlant getFromItem(Item item) {
            if (item == GTItemsRegistry.INSTANCE.GRAPE_SEEDS.get()) {
                return GRAPE;
            }
            if (item == Blocks.VINE.asItem()) {
                return VINE;
            }
            if (item == GTBlocksRegistry.INSTANCE.IVY.get().asItem()) {
                return IVY;
            }
            return NONE;
        }
    }
}
