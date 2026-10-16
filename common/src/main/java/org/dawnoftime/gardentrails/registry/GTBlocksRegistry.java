package org.dawnoftime.gardentrails.registry;

import org.dawnoftime.gardentrails.util.RegistrationContext;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.block.plants.*;
import org.dawnoftime.gardentrails.block.trees.*;
import org.dawnoftime.gardentrails.block.templates.*;
import org.dawnoftime.gardentrails.item.IHasFlowerPot;
import org.dawnoftime.gardentrails.item.templates.GTBlockItem;
import org.dawnoftime.gardentrails.item.templates.PotAndBlockItem;
import org.dawnoftime.gardentrails.item.templates.SoilSeedsItem;
import org.dawnoftime.gardentrails.util.Foods;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static org.dawnoftime.gardentrails.util.VoxelShapes.*;

@SuppressWarnings({"unused", "unchecked"})
public abstract class GTBlocksRegistry {
    public static GTBlocksRegistry INSTANCE;

    public static Map<TagKey<Block>, Set<Supplier<Block>>> blockTagsMap = new HashMap<>();
    public static final HashMap<String, Block> POT_BLOCKS = new HashMap<>();

    public Supplier<Block> CYPRESS;
    public final Supplier<Block> BOXWOOD_BUSH = register("boxwood_bush", () -> new BiomeColoredBushBlock(RegistrationContext.copy(Blocks.SPRUCE_LEAVES), BUSH_SHAPES, IBiomeColoredBlock.ColorType.FOLIAGE), BlockTags.SWORD_EFFICIENT);
    public final Supplier<Block> BOXWOOD_TALL_HEDGE = register("boxwood_tall_hedge", () -> new BiomeColoredPlateBlock(RegistrationContext.copy(Blocks.SPRUCE_LEAVES), IBiomeColoredBlock.ColorType.FOLIAGE), BlockTags.SWORD_EFFICIENT);
    public final Supplier<Block> BOXWOOD_SMALL_HEDGE = register("boxwood_small_hedge", () -> new BiomeColoredEdgeBlock(RegistrationContext.copy(Blocks.SPRUCE_LEAVES), IBiomeColoredBlock.ColorType.FOLIAGE), BlockTags.SWORD_EFFICIENT);
    public Supplier<Block> IVY;
    public Supplier<Block> GERANIUM_PINK;
    public Supplier<Block> GERANIUM_ORANGE;
    public Supplier<Block> GERANIUM_PURPLE;
    public Supplier<Block> GERANIUM_PURPLEISH;
    public Supplier<Block> GERANIUM_RED;
    public Supplier<Block> GERANIUM_WHITE;
    public final Supplier<Block> PLANTER_GERANIUM_PINK = register("planter_geranium_pink", () -> new PlanterBlock(RegistrationContext.copy(Blocks.CLAY).strength(0.6F).noOcclusion()));
    public final Supplier<Block> PLANTER_GERANIUM_ORANGE = register("planter_geranium_orange", () -> new PlanterBlock(RegistrationContext.copy(Blocks.CLAY).strength(0.6F).noOcclusion()));
    public final Supplier<Block> PLANTER_GERANIUM_PURPLE = register("planter_geranium_purple", () -> new PlanterBlock(RegistrationContext.copy(Blocks.CLAY).strength(0.6F).noOcclusion()));
    public final Supplier<Block> PLANTER_GERANIUM_PURPLEISH = register("planter_geranium_purpleish", () -> new PlanterBlock(RegistrationContext.copy(Blocks.CLAY).strength(0.6F).noOcclusion()));
    public final Supplier<Block> PLANTER_GERANIUM_RED = register("planter_geranium_red", () -> new PlanterBlock(RegistrationContext.copy(Blocks.CLAY).strength(0.6F).noOcclusion()));
    public final Supplier<Block> PLANTER_GERANIUM_WHITE = register("planter_geranium_white", () -> new PlanterBlock(RegistrationContext.copy(Blocks.CLAY).strength(0.6F).noOcclusion()));
    public final Supplier<Block> WILD_GRAPE = register("wild_grape", () -> new WildPlantBlock(RegistrationContext.copy(Blocks.DANDELION)), BlockTags.SWORD_EFFICIENT);
    public final Supplier<Block> BAMBOO_DRYING_TRAY = register("bamboo_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.OAK_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> OAK_DRYING_TRAY = register("oak_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.OAK_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> BIRCH_DRYING_TRAY = register("birch_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.BIRCH_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ACACIA_DRYING_TRAY = register("acacia_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.ACACIA_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> MANGROVE_DRYING_TRAY = register("mangrove_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.MANGROVE_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> JUNGLE_DRYING_TRAY = register("jungle_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.JUNGLE_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> CHERRY_DRYING_TRAY = register("cherry_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.CHERRY_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> DARK_OAK_DRYING_TRAY = register("dark_oak_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.DARK_OAK_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> SPRUCE_DRYING_TRAY = register("spruce_drying_tray", () -> new DryerBlock(RegistrationContext.copy(Blocks.SPRUCE_PLANKS).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> SAKE_VAT = register("sake_vat", () -> new SakeVatBlock(RegistrationContext.copy(Blocks.SPRUCE_PLANKS).noOcclusion()), BlockTags.MINEABLE_WITH_AXE);
    public Supplier<ColoredGrowingBushBlock> CAMELLIA;
    public Supplier<MulberryBlock> MULBERRY;
    public final Supplier<WaterDoubleCropsBlock> RICE = registerWithItem("rice", () -> new WaterDoubleCropsBlock(2), (block) -> new SoilSeedsItem(block, Foods.RICE, "lore.gardentrails.rice", "block.minecraft.cauldron"), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> STICK_BUNDLE = register("stick_bundle", () -> new StickBundleBlock(RegistrationContext.copy(Blocks.OAK_WOOD).strength(2.0F, 3.0F).sound(SoundType.GRASS).noOcclusion()).setBurnable(), BlockTags.MINEABLE_WITH_AXE);
    // Declared before the rotten log: the rotten log turns into it once brushed
    // Properties come from the planks: the vanilla log properties read an axis state these blocks do not have
    public final Supplier<Block> SOAKED_OAK_LOG = register("soaked_oak_log", () -> new SoakedLogBlock(RegistrationContext.copy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_LOG = register("rotten_oak_log", () -> new MoldyLogBlock(SOAKED_OAK_LOG.get(), RegistrationContext.copy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_PLANKS = register("rotten_oak_planks", () -> new Block(RegistrationContext.copy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_STAIRS = register("rotten_oak_stairs", () -> new StairBlockGT(ROTTEN_OAK_PLANKS.get().defaultBlockState(), RegistrationContext.copy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_SLAB = register("rotten_oak_slab", () -> new SlabBlock(RegistrationContext.copy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_EDGE = register("rotten_oak_edge", () -> new EdgeBlock(RegistrationContext.copy(Blocks.OAK_PLANKS).noOcclusion()), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_PLATE = register("rotten_oak_plate", () -> new PlateBlock(RegistrationContext.copy(Blocks.OAK_PLANKS).noOcclusion()), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_FENCE = register("rotten_oak_fence", () -> new FenceBlock(RegistrationContext.copy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_FENCE_GATE = register("rotten_oak_fence_gate", () -> new FenceGateBlock(WoodType.OAK, RegistrationContext.copy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
    public final Supplier<Block> ROTTEN_OAK_TRAPDOOR = register("rotten_oak_trapdoor", () -> new TrapDoorBlockGT(RegistrationContext.copy(Blocks.OAK_PLANKS).noOcclusion(), BlockSetType.OAK), BlockTags.MINEABLE_WITH_AXE);
    public static final List<String> MAPLE_VARIANT_IDS = List.of("red", "orange", "green");
    public final Map<String, MapleVariant> MAPLE_VARIANTS = new LinkedHashMap<>();
    public Supplier<SoilCropsBlock> COMMELINA;
    public final Supplier<Block> WILD_MAIZE = register("wild_maize", () -> new WildMaizeBlock(RegistrationContext.copy(Blocks.DANDELION)), BlockTags.SWORD_EFFICIENT);
    public Supplier<DoubleCropsBlock> MAIZE;
    // --- Iron Pergola ---
    // Note: bare pergola variants reference their plant children via GTBlocksRegistry.INSTANCE
    // to avoid illegal forward references (plant fields are declared after the bare pergola field).
    public final Supplier<PergolaBlock> IRON_PERGOLA = register("iron_pergola",
            () -> new PergolaBlock(RegistrationContext.copy(Blocks.IRON_BARS),
                    () -> GTBlocksRegistry.INSTANCE.IRON_PERGOLA_VINE.get(),
                    () -> GTBlocksRegistry.INSTANCE.IRON_PERGOLA_IVY.get(),
                    () -> GTBlocksRegistry.INSTANCE.IRON_PERGOLA_GRAPE.get()));
    public final Supplier<PergolaBlock> IRON_PERGOLA_VINE = registerWithItem("iron_pergola_vine",
            () -> new PergolaPlantBlock(RegistrationContext.copy(Blocks.IRON_BARS), () -> IRON_PERGOLA.get()), null);
    public final Supplier<PergolaBlock> IRON_PERGOLA_IVY = registerWithItem("iron_pergola_ivy",
            () -> new PergolaPlantBlock(RegistrationContext.copy(Blocks.IRON_BARS), () -> IRON_PERGOLA.get()), null);
    public final Supplier<PergolaBlock> IRON_PERGOLA_GRAPE = registerWithItem("iron_pergola_grape",
            () -> new PergolaCropBlock(RegistrationContext.copy(Blocks.IRON_BARS), 4, 6, 0, 2, 2, () -> IRON_PERGOLA.get()), null);

    // --- Copper Pergola ---
    public final Supplier<PergolaBlock> COPPER_PERGOLA = register("copper_pergola",
            () -> new PergolaBlock(RegistrationContext.copy(Blocks.COPPER_BLOCK).noOcclusion(),
                    () -> GTBlocksRegistry.INSTANCE.COPPER_PERGOLA_VINE.get(),
                    () -> GTBlocksRegistry.INSTANCE.COPPER_PERGOLA_IVY.get(),
                    () -> GTBlocksRegistry.INSTANCE.COPPER_PERGOLA_GRAPE.get()));
    public final Supplier<PergolaBlock> COPPER_PERGOLA_VINE = registerWithItem("copper_pergola_vine",
            () -> new PergolaPlantBlock(RegistrationContext.copy(Blocks.COPPER_BLOCK).noOcclusion(), () -> COPPER_PERGOLA.get()), null);
    public final Supplier<PergolaBlock> COPPER_PERGOLA_IVY = registerWithItem("copper_pergola_ivy",
            () -> new PergolaPlantBlock(RegistrationContext.copy(Blocks.COPPER_BLOCK).noOcclusion(), () -> COPPER_PERGOLA.get()), null);
    public final Supplier<PergolaBlock> COPPER_PERGOLA_GRAPE = registerWithItem("copper_pergola_grape",
            () -> new PergolaCropBlock(RegistrationContext.copy(Blocks.COPPER_BLOCK).noOcclusion(), 4, 6, 0, 2, 2, () -> COPPER_PERGOLA.get()), null);

    // --- Oxidized Copper Pergola ---
    public final Supplier<PergolaBlock> OXIDIZED_COPPER_PERGOLA = register("oxidized_copper_pergola",
            () -> new PergolaBlock(RegistrationContext.copy(Blocks.OXIDIZED_COPPER).noOcclusion(),
                    () -> GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_PERGOLA_VINE.get(),
                    () -> GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_PERGOLA_IVY.get(),
                    () -> GTBlocksRegistry.INSTANCE.OXIDIZED_COPPER_PERGOLA_GRAPE.get()));
    public final Supplier<PergolaBlock> OXIDIZED_COPPER_PERGOLA_VINE = registerWithItem("oxidized_copper_pergola_vine",
            () -> new PergolaPlantBlock(RegistrationContext.copy(Blocks.OXIDIZED_COPPER).noOcclusion(), () -> OXIDIZED_COPPER_PERGOLA.get()), null);
    public final Supplier<PergolaBlock> OXIDIZED_COPPER_PERGOLA_IVY = registerWithItem("oxidized_copper_pergola_ivy",
            () -> new PergolaPlantBlock(RegistrationContext.copy(Blocks.OXIDIZED_COPPER).noOcclusion(), () -> OXIDIZED_COPPER_PERGOLA.get()), null);
    public final Supplier<PergolaBlock> OXIDIZED_COPPER_PERGOLA_GRAPE = registerWithItem("oxidized_copper_pergola_grape",
            () -> new PergolaCropBlock(RegistrationContext.copy(Blocks.OXIDIZED_COPPER).noOcclusion(), 4, 6, 0, 2, 2, () -> OXIDIZED_COPPER_PERGOLA.get()), null);

    // --- Lattice ---
    public final Supplier<LatticeBlock> IRON_LATTICE = register("iron_lattice",
            () -> new LatticeBlock(RegistrationContext.copy(Blocks.IRON_BARS).noOcclusion()));
    public final Supplier<LatticeBlock> COPPER_LATTICE = register("copper_lattice",
            () -> new LatticeBlock(RegistrationContext.copy(Blocks.COPPER_BLOCK).noOcclusion()));
    public final Supplier<LatticeBlock> OXIDIZED_COPPER_LATTICE = register("oxidized_copper_lattice",
            () -> new LatticeBlock(RegistrationContext.copy(Blocks.OXIDIZED_COPPER).noOcclusion()));

    public void postRegister() {
        CYPRESS = registerWithFlowerPotItem("cypress", () -> new CypressBlock(RegistrationContext.copy(Blocks.SPRUCE_LEAVES).randomTicks()).setBurnable(), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
        MAIZE = registerWithFlowerPotItem("maize", () -> new DoubleCropsBlock(SoilCropsBlock.PlantType.CROP, 4), (block) -> new SoilSeedsItem(block, Foods.MAIZE));
        COMMELINA = registerWithFlowerPotItem("commelina", () -> new SoilCropsBlock(SoilCropsBlock.PlantType.PLAINS), (block) -> new SoilSeedsItem(block, null));
        for(String variantId : MAPLE_VARIANT_IDS) {
            Supplier<Block> trunk = registerWithItem("maple_" + variantId + "_trunk", () -> new MapleTrunkBlock(RegistrationContext.copy(Blocks.SPRUCE_LEAVES)), null, BlockTags.MINEABLE_WITH_AXE);
            Supplier<Block> leaves = registerWithItem("maple_" + variantId + "_leaves", () -> new MapleLeavesBlock(RegistrationContext.copy(Blocks.SPRUCE_LEAVES)), null, BlockTags.MINEABLE_WITH_AXE);
            Supplier<Block> sapling = registerWithFlowerPotItem("maple_" + variantId + "_sapling", () -> new MapleSaplingBlock(RegistrationContext.copy(Blocks.SPRUCE_LEAVES).noCollision(), trunk, leaves), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
            MAPLE_VARIANTS.put(variantId, new MapleVariant(variantId, trunk, leaves, sapling));
        }
        CAMELLIA = registerWithFlowerPotItem("camellia", () -> new ColoredGrowingBushBlock(SoilCropsBlock.PlantType.PLAINS, 3), "camellia_seeds", (block) -> new SoilSeedsItem(block, null));
        MULBERRY = registerWithFlowerPotItem("mulberry", () -> new MulberryBlock(SoilCropsBlock.PlantType.PLAINS, 3, 2), (block) -> new SoilSeedsItem(block, Foods.MULBERRY));
        IVY = registerWithFlowerPotItem("ivy", () -> new IvyBlock(RegistrationContext.copy(Blocks.VINE).randomTicks().strength(0.2F).sound(SoundType.VINE)), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
        GERANIUM_PINK = registerWithFlowerPotItem("geranium_pink", () -> new GeraniumBlock(RegistrationContext.copy(Blocks.SUNFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.GRASS)), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
        GERANIUM_ORANGE = registerWithFlowerPotItem("geranium_orange", () -> new GeraniumBlock(RegistrationContext.copy(Blocks.SUNFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.GRASS)), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
        GERANIUM_PURPLE = registerWithFlowerPotItem("geranium_purple", () -> new GeraniumBlock(RegistrationContext.copy(Blocks.SUNFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.GRASS)), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
        GERANIUM_PURPLEISH = registerWithFlowerPotItem("geranium_purpleish", () -> new GeraniumBlock(RegistrationContext.copy(Blocks.SUNFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.GRASS)), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
        GERANIUM_RED = registerWithFlowerPotItem("geranium_red", () -> new GeraniumBlock(RegistrationContext.copy(Blocks.SUNFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.GRASS)), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
        GERANIUM_WHITE = registerWithFlowerPotItem("geranium_white", () -> new GeraniumBlock(RegistrationContext.copy(Blocks.SUNFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.GRASS)), (block) -> new PotAndBlockItem(block, RegistrationContext.item()));
    }

    public <T extends Block> Supplier<T> register(String id, Supplier<T> block) {
        return this.registerWithItem(id, block, (T blockObject) -> new GTBlockItem(blockObject, RegistrationContext.item()));
    }

    public <T extends Block> Supplier<T> register(String id, Supplier<T> block, TagKey<Block>... tagKeys) {
        return this.registerWithItem(id, block, (T blockObject) -> new GTBlockItem(blockObject, RegistrationContext.item()), tagKeys);
    }

    public abstract <T extends Block, Y extends Item> Supplier<T> registerWithItem(String id, Supplier<T> block, Function<T, Y> item, TagKey<Block>... tags);
    public abstract <T extends Block, Y extends Item & IHasFlowerPot> Supplier<T> registerWithFlowerPotItem(String blockID, Supplier<T> block, String itemID, Function<T, Y> item);

    public <T extends Block, Y extends Item & IHasFlowerPot> Supplier<T> registerWithFlowerPotItem(String id, Supplier<T> block, Function<T, Y> item) {
        return this.registerWithFlowerPotItem(id, block, id, item);
    }
    

    public <T extends Block> void addBlockTag(Supplier<T> block, TagKey<Block> tag){
        blockTagsMap.computeIfAbsent(tag, k -> new HashSet<>()).add((Supplier<Block>) block);
    }

    //Old Function
    private static ToIntFunction<BlockState> litBlockEmission(final int lightValue) {
        return state -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
