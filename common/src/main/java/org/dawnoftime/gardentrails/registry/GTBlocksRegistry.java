package org.dawnoftime.gardentrails.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.block.plants.*;
import org.dawnoftime.gardentrails.block.trees.*;
import org.dawnoftime.gardentrails.block.templates.*;
import org.dawnoftime.gardentrails.item.templates.PottablePlantItem;
import org.dawnoftime.gardentrails.item.templates.SoilSeedsItem;
import org.dawnoftime.gardentrails.util.Foods;
import org.dawnoftime.gardentrails.util.GTBlockStateProperties.ClimbingPlant;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.dawnoftime.gardentrails.util.VoxelShapes.*;

@SuppressWarnings({"unused", "unchecked"})
public abstract class GTBlocksRegistry {
    public static GTBlocksRegistry INSTANCE;

    // Plants that have a potted version, read by the item tooltips
    public static final Set<Block> POTTABLE_PLANTS = new HashSet<>();
    // Blocks that need a non solid render layer, read by the client code of the loaders that cannot get it from the model json (Fabric).
    public static final List<Supplier<? extends Block>> CUTOUT_BLOCKS = new ArrayList<>();
    public static final List<Supplier<? extends Block>> TRANSLUCENT_BLOCKS = new ArrayList<>();
    // Burn odds per block, applied by each loader with its own fire API
    public static final Map<Supplier<? extends Block>, FireValues> FLAMMABLE_BLOCKS = new LinkedHashMap<>();

    /** How fast fire spreads to the block and how fast it destroys it (planks: 5 and 20). */
    public record FireValues(int spread, int destruction) {}

    public Supplier<Block> CYPRESS;
    // Grape seeds have no block, so vanilla pots cannot hold them: this pot swaps in through PotItem
    public Supplier<Block> GRAPE_SEEDS_POT;
    public Supplier<Block> BOXWOOD_BUSH;
    public Supplier<Block> BOXWOOD_TALL_HEDGE;
    public Supplier<Block> BOXWOOD_SMALL_HEDGE;
    public Supplier<Block> IVY;
    public final Map<GeraniumColor, Supplier<Block>> GERANIUMS = new EnumMap<>(GeraniumColor.class);
    public Supplier<Block> FLOWER_PLANTER;
    public Supplier<Block> WILD_GRAPE;
    // Drying trays by wood id, in creative tab order. The bamboo tray copies the oak planks.
    public final Map<String, Supplier<Block>> DRYING_TRAYS = new LinkedHashMap<>();
    public Supplier<Block> SAKE_VAT;
    public Supplier<GrowingBushBlock> CAMELLIA;
    public Supplier<MulberryBlock> MULBERRY;
    public Supplier<RottenOakSaplingBlock> ROTTEN_OAK_SAPLING;
    public Supplier<WaterDoubleCropsBlock> RICE;
    public Supplier<Block> STICK_BUNDLE;
    public Supplier<Block> DECAYED_OAK_LOG;
    public Supplier<Block> ROTTEN_OAK_LOG;
    public Supplier<Block> ROTTEN_OAK_PLANKS;
    public Supplier<Block> ROTTEN_OAK_STAIRS;
    public Supplier<Block> ROTTEN_OAK_SLAB;
    public Supplier<Block> ROTTEN_OAK_EDGE;
    public Supplier<Block> ROTTEN_OAK_PLATE;
    public Supplier<Block> ROTTEN_OAK_FENCE;
    public Supplier<Block> ROTTEN_OAK_FENCE_GATE;
    public Supplier<Block> ROTTEN_OAK_TRAPDOOR;
    public static final List<String> MAPLE_VARIANT_IDS = List.of("red", "orange", "green");
    public final Map<String, MapleVariant> MAPLE_VARIANTS = new LinkedHashMap<>();
    public Supplier<SoilCropsBlock> COMMELINA;
    public Supplier<Block> WILD_MAIZE;
    public Supplier<DoubleCropsBlock> MAIZE;
    public Supplier<PergolaBlock> IRON_PERGOLA;
    public Supplier<PergolaBlock> COPPER_PERGOLA;
    public Supplier<PergolaBlock> OXIDIZED_COPPER_PERGOLA;
    public Supplier<LatticeBlock> IRON_LATTICE;
    public Supplier<LatticeBlock> COPPER_LATTICE;
    public Supplier<LatticeBlock> OXIDIZED_COPPER_LATTICE;

    /**
     * Registers every block. Each loader calls it right after assigning {@link #INSTANCE}, so the constructors and
     * suppliers below can read INSTANCE. A block that reads another block when it is built (stairs from planks,
     * rotten log from decayed log) must be registered after it.
     */
    public final void registerAll() {
        BOXWOOD_BUSH = cutout(register("boxwood_bush", () -> new BushBlockGT(Block.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES), BUSH_SHAPES, IBiomeColoredBlock.ColorType.FOLIAGE), BlockTags.SWORD_EFFICIENT));
        BOXWOOD_TALL_HEDGE = cutout(register("boxwood_tall_hedge", () -> new PlateBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES), IBiomeColoredBlock.ColorType.FOLIAGE), BlockTags.SWORD_EFFICIENT));
        BOXWOOD_SMALL_HEDGE = cutout(register("boxwood_small_hedge", () -> new EdgeBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES), IBiomeColoredBlock.ColorType.FOLIAGE), BlockTags.SWORD_EFFICIENT));
        FLOWER_PLANTER = cutout(register("flower_planter", () -> new FlowerPlanterBlock(planterProperties())));
        WILD_GRAPE = cutout(register("wild_grape", () -> new WildPlantBlock(Block.Properties.ofFullCopy(Blocks.DANDELION)), BlockTags.SWORD_EFFICIENT));
        Map<String, Block> trayPlanks = new LinkedHashMap<>();
        trayPlanks.put("bamboo", Blocks.OAK_PLANKS);
        trayPlanks.put("oak", Blocks.OAK_PLANKS);
        trayPlanks.put("birch", Blocks.BIRCH_PLANKS);
        trayPlanks.put("spruce", Blocks.SPRUCE_PLANKS);
        trayPlanks.put("jungle", Blocks.JUNGLE_PLANKS);
        trayPlanks.put("acacia", Blocks.ACACIA_PLANKS);
        trayPlanks.put("dark_oak", Blocks.DARK_OAK_PLANKS);
        trayPlanks.put("mangrove", Blocks.MANGROVE_PLANKS);
        trayPlanks.put("cherry", Blocks.CHERRY_PLANKS);
        trayPlanks.forEach((wood, planks) -> DRYING_TRAYS.put(wood, cutout(register(wood + "_drying_tray", () -> new DryerBlock(Block.Properties.ofFullCopy(planks).noOcclusion(), DRYER_SHAPES), BlockTags.MINEABLE_WITH_AXE))));
        SAKE_VAT = translucent(register("sake_vat", () -> new SakeVatBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).noOcclusion()), BlockTags.MINEABLE_WITH_AXE));
        RICE = cutout(registerWithItem("rice", () -> new WaterDoubleCropsBlock(2), (block) -> new SoilSeedsItem(block, Foods.RICE, "lore.gardentrails.rice", "block.minecraft.cauldron"), BlockTags.MINEABLE_WITH_AXE));
        STICK_BUNDLE = cutout(burnable(register("stick_bundle", () -> new StickBundleBlock(Block.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(2.0F, 3.0F).sound(SoundType.GRASS).noOcclusion()), BlockTags.MINEABLE_WITH_AXE)));
        // Registered before the rotten log: the rotten log turns into it once brushed
        // Properties come from the planks: the vanilla log properties read an axis state these blocks do not have
        DECAYED_OAK_LOG = register("decayed_oak_log", () -> new DecayedLogBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_LOG = register("rotten_oak_log", () -> new MoldyLogBlock(DECAYED_OAK_LOG.get(), Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_PLANKS = register("rotten_oak_planks", () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_STAIRS = register("rotten_oak_stairs", () -> new StairBlockGT(ROTTEN_OAK_PLANKS.get().defaultBlockState(), Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_SLAB = register("rotten_oak_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_EDGE = register("rotten_oak_edge", () -> new EdgeBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_PLATE = register("rotten_oak_plate", () -> new PlateBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_FENCE = register("rotten_oak_fence", () -> new FenceBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_FENCE_GATE = register("rotten_oak_fence_gate", () -> new FenceGateBlock(WoodType.OAK, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTags.MINEABLE_WITH_AXE);
        ROTTEN_OAK_TRAPDOOR = cutout(register("rotten_oak_trapdoor", () -> new TrapDoorBlockGT(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion(), BlockSetType.OAK), BlockTags.MINEABLE_WITH_AXE));
        WILD_MAIZE = cutout(register("wild_maize", () -> new WildMaizeBlock(Block.Properties.ofFullCopy(Blocks.DANDELION)), BlockTags.SWORD_EFFICIENT));

        IRON_PERGOLA = registerPergola("iron", Blocks.IRON_BARS);
        COPPER_PERGOLA = registerPergola("copper", Blocks.COPPER_BLOCK);
        OXIDIZED_COPPER_PERGOLA = registerPergola("oxidized_copper", Blocks.OXIDIZED_COPPER);

        IRON_LATTICE = cutout(register("iron_lattice", () -> new LatticeBlock(Block.Properties.ofFullCopy(Blocks.IRON_BARS).noOcclusion())));
        COPPER_LATTICE = cutout(register("copper_lattice", () -> new LatticeBlock(Block.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion())));
        OXIDIZED_COPPER_LATTICE = cutout(register("oxidized_copper_lattice", () -> new LatticeBlock(Block.Properties.ofFullCopy(Blocks.OXIDIZED_COPPER).noOcclusion())));

        // Plants with a potted version
        CYPRESS = cutout(burnable(registerWithFlowerPotItem("cypress", () -> new CypressBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES).randomTicks()), "cypress", (block) -> new PottablePlantItem(block, new Item.Properties()), IBiomeColoredBlock.ColorType.FOLIAGE)));
        GRAPE_SEEDS_POT = cutout(registerWithItem("grape_seeds_flower_pot", () -> new SeedPotBlock(() -> GTItemsRegistry.INSTANCE.GRAPE_SEEDS.get()), null, BlockTags.MINEABLE_WITH_PICKAXE));
        MAIZE = cutout(registerWithFlowerPotItem("maize", () -> new DoubleCropsBlock(SoilCropsBlock.PlantType.CROP, 4), (block) -> new SoilSeedsItem(block, Foods.MAIZE)));
        COMMELINA = cutout(registerWithFlowerPotItem("commelina", () -> new SoilCropsBlock(SoilCropsBlock.PlantType.PLAINS), (block) -> new SoilSeedsItem(block, null)));
        for(String variantId : MAPLE_VARIANT_IDS) {
            Supplier<Block> trunk = cutout(registerWithItem("maple_" + variantId + "_trunk", () -> new MapleTrunkBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)), null, BlockTags.MINEABLE_WITH_AXE));
            Supplier<Block> leaves = cutout(registerWithItem("maple_" + variantId + "_leaves", () -> new MapleLeavesBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES)), null, BlockTags.MINEABLE_WITH_AXE));
            Supplier<Block> sapling = cutout(registerWithFlowerPotItem("maple_" + variantId + "_sapling", () -> new MapleSaplingBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES), trunk, leaves), (block) -> new PottablePlantItem(block, new Item.Properties())));
            MAPLE_VARIANTS.put(variantId, new MapleVariant(variantId, trunk, leaves, sapling));
        }
        ROTTEN_OAK_SAPLING = cutout(registerWithFlowerPotItem("rotten_oak_sapling", () -> new RottenOakSaplingBlock(Block.Properties.ofFullCopy(Blocks.OAK_SAPLING)), (block) -> new PottablePlantItem(block, new Item.Properties())));
        CAMELLIA = cutout(registerWithFlowerPotItem("camellia", () -> new GrowingBushBlock(SoilCropsBlock.PlantType.PLAINS, 3, IBiomeColoredBlock.ColorType.FOLIAGE), "camellia_seeds", (block) -> new SoilSeedsItem(block, null)));
        MULBERRY = cutout(registerWithFlowerPotItem("mulberry", () -> new MulberryBlock(SoilCropsBlock.PlantType.PLAINS, 3, 2), (block) -> new SoilSeedsItem(block, Foods.MULBERRY)));
        IVY = cutout(registerWithFlowerPotItem("ivy", () -> new IvyBlock(Block.Properties.ofFullCopy(Blocks.VINE).randomTicks().strength(0.2F).sound(SoundType.VINE)), (block) -> new PottablePlantItem(block, new Item.Properties())));
        for (GeraniumColor color : GeraniumColor.values()) {
            GERANIUMS.put(color, cutout(registerWithFlowerPotItem(color.blockId(), () -> new GeraniumBlock(Block.Properties.ofFullCopy(Blocks.SUNFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.GRASS)), color.blockId(), (block) -> new PottablePlantItem(block, new Item.Properties()), IBiomeColoredBlock.ColorType.FOLIAGE)));
        }
    }

    /**
     * Registers the pergola of a metal, then its three legacy plant blocks. Before the climbing plant became a
     * state property each plant had its own block: the legacy blocks only convert old worlds (LegacyPergolaBlock).
     */
    private Supplier<PergolaBlock> registerPergola(String metal, Block base) {
        Supplier<PergolaBlock> pergola = cutout(register(metal + "_pergola", () -> new PergolaBlock(Block.Properties.ofFullCopy(base).noOcclusion())));
        for (ClimbingPlant plant : List.of(ClimbingPlant.VINE, ClimbingPlant.IVY, ClimbingPlant.GRAPE)) {
            registerWithItem(metal + "_pergola_" + plant.getSerializedName(), () -> new LegacyPergolaBlock(Block.Properties.ofFullCopy(base).noOcclusion(), pergola, plant), null);
        }
        return pergola;
    }

    public <T extends Block> Supplier<T> register(String id, Supplier<T> block) {
        return this.registerWithItem(id, block, (T blockObject) -> new BlockItem(blockObject, new Item.Properties()));
    }

    public <T extends Block> Supplier<T> register(String id, Supplier<T> block, TagKey<Block>... tagKeys) {
        return this.registerWithItem(id, block, (T blockObject) -> new BlockItem(blockObject, new Item.Properties()), tagKeys);
    }

    public abstract <T extends Block, Y extends Item> Supplier<T> registerWithItem(String id, Supplier<T> block, Function<T, Y> item, TagKey<Block>... tags);
    /**
     * Registers the plant block, its item, its vanilla style potted block (blockID + "_flower_pot").
     * The potted block has no item: it is only obtained by putting the plant item on an empty flower pot.
     * Each loader registers the plant and its item, then calls {@link #registerPot}.
     */
    public abstract <T extends Block, Y extends Item> Supplier<T> registerWithFlowerPotItem(String blockID, Supplier<T> block, String itemID, Function<T, Y> item, @Nullable IBiomeColoredBlock.ColorType potColor);

    public <T extends Block, Y extends Item> Supplier<T> registerWithFlowerPotItem(String id, Supplier<T> block, Function<T, Y> item) {
        return this.registerWithFlowerPotItem(id, block, id, item, null);
    }

    public <T extends Block, Y extends Item> Supplier<T> registerWithFlowerPotItem(String id, Supplier<T> block, String itemID, Function<T, Y> item) {
        return this.registerWithFlowerPotItem(id, block, itemID, item, null);
    }

    protected static Block.Properties planterProperties() {
        return Block.Properties.ofFullCopy(Blocks.FLOWER_POT).strength(0.6F).noOcclusion();
    }

    /** Registers the potted version of a plant. The plant must be registered before, the pot reads its instance. */
    protected void registerPot(String plantID, Supplier<? extends Block> plant, @Nullable IBiomeColoredBlock.ColorType potColor) {
        cutout(registerWithItem(plantID + "_flower_pot", () -> {
            Block plantBlock = plant.get();
            POTTABLE_PLANTS.add(plantBlock);
            return new PottedPlantBlock(plantBlock, potColor);
        }, null, BlockTags.MINEABLE_WITH_PICKAXE));
    }

    /** Marks a registered block as cutout (plants, bars, leaves, anything with transparent pixels). */
    protected static <T extends Block> Supplier<T> cutout(Supplier<T> block) {
        CUTOUT_BLOCKS.add(block);
        return block;
    }

    /** Marks a registered block as flammable like planks. */
    protected static <T extends Block> Supplier<T> burnable(Supplier<T> block) {
        FLAMMABLE_BLOCKS.put(block, new FireValues(5, 20));
        return block;
    }

    /** Marks a registered block as translucent (glass like liquids). */
    protected static <T extends Block> Supplier<T> translucent(Supplier<T> block) {
        TRANSLUCENT_BLOCKS.add(block);
        return block;
    }
}
