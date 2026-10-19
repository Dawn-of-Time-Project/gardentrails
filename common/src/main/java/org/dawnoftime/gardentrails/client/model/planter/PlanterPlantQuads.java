package org.dawnoftime.gardentrails.client.model.planter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import org.dawnoftime.gardentrails.block.plants.GeraniumColor;
import org.dawnoftime.gardentrails.block.templates.PlanterBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Builds the quads of the plant shown in a flower planter from the model of its potted block, so a planter holds
 * exactly what a flower pot shows, without any model file per plant.
 * The pot and the soil are removed from the potted model (they are recognized by their texture), what is left is moved
 * from the middle of the pot to the middle of the planter and turned like the planter.
 * Both loaders only call {@link #quads}, the way the result reaches the chunk mesh is the only loader specific part.
 */
public final class PlanterPlantQuads {
    private static final Identifier POT_SPRITE = Identifier.parse("block/flower_pot");
    private static final Identifier SOIL_SPRITE = Identifier.parse("block/dirt");

    // Soil of a pot is at y=4, the planter top is at y=7, the plant is centered on the planter body (z=13)
    private static final float PLANT_SHIFT_Y = 3F / 16F;
    private static final float PLANT_SHIFT_Z = 5F / 16F;
    private static final float UPPER_HALF_SHIFT_Y = 0.5F;

    /** Set by the loader: all the quads of a model registered with the additional models of the loader, null if unknown. */
    public static Function<Identifier, List<BakedQuad>> standaloneModels = id -> null;

    private PlanterPlantQuads() {}

    /** Models that are not bound to a block and have to be loaded by the loader. */
    public static List<Identifier> additionalModels() {
        List<Identifier> models = new ArrayList<>();
        for (GeraniumColor color : GeraniumColor.values()) {
            models.add(leavesModel(color));
        }
        return models;
    }

    private static Identifier leavesModel(GeraniumColor color) {
        return Identifier.fromNamespaceAndPath("gardentrails", "block/" + color.blockId() + "_planter");
    }

    /** The geranium has hanging leaves in a planter, a model made for it instead of the small potted plant. */
    private static Identifier leavesModelOf(Identifier pottedId) {
        if (pottedId.getNamespace().equals("gardentrails")) {
            for (GeraniumColor color : GeraniumColor.values()) {
                if (pottedId.getPath().equals(color.blockId() + "_flower_pot")) {
                    return leavesModel(color);
                }
            }
        }
        return null;
    }

    /** All the quads of the plant, to add to the unculled quads of the planter model. */
    public static List<BakedQuad> quads(Block potted, Direction facing, Half half) {
        float shiftY = half == Half.TOP ? UPPER_HALF_SHIFT_Y : 0F;
        Identifier leaves = leavesModelOf(BuiltInRegistries.BLOCK.getKey(potted));
        List<BakedQuad> source;
        List<BakedQuad> leavesQuads = leaves == null ? null : standaloneModels.apply(leaves);
        if (leavesQuads != null) {
            return transform(leavesQuads, 0F, shiftY, 0F, facing);
        }
        BlockState pottedState = potted.defaultBlockState();
        BlockStateModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(pottedState);
        source = allQuads(model, true);
        if (source.isEmpty() && potted instanceof FlowerPotBlock pot) {
            // The model of this pot has no recognizable pot texture, the plant is shown with its own block model
            BlockState plantState = pot.getPotted().defaultBlockState();
            source = allQuads(Minecraft.getInstance().getBlockRenderer().getBlockModel(plantState), false);
        }
        return transform(source, 0F, shiftY + PLANT_SHIFT_Y, PLANT_SHIFT_Z, facing);
    }

    private static List<BakedQuad> allQuads(BlockStateModel model, boolean withoutPot) {
        List<BakedQuad> quads = new ArrayList<>();
        for (BlockModelPart part : model.collectParts(RandomSource.create(42L))) {
            for (int i = 0; i <= Direction.values().length; i++) {
                Direction side = i == Direction.values().length ? null : Direction.values()[i];
                for (BakedQuad quad : part.getQuads(side)) {
                    if (!withoutPot || !isPot(quad)) {
                        quads.add(quad);
                    }
                }
            }
        }
        return quads;
    }

    private static boolean isPot(BakedQuad quad) {
        Identifier name = quad.sprite().contents().name();
        return name.equals(POT_SPRITE) || name.equals(SOIL_SPRITE);
    }

    private static List<BakedQuad> transform(List<BakedQuad> quads, float dx, float dy, float dz, Direction facing) {
        // Same turn as the y rotation of the blockstate: south 0, west 90, north 180, east 270
        int turns = facing.get2DDataValue();
        List<BakedQuad> result = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) {
            Vector3fc[] positions = new Vector3fc[BakedQuad.VERTEX_COUNT];
            for (int vertex = 0; vertex < BakedQuad.VERTEX_COUNT; vertex++) {
                Vector3fc source = quad.position(vertex);
                float x = source.x() + dx;
                float y = source.y() + dy;
                float z = source.z() + dz;
                for (int turn = 0; turn < turns; turn++) {
                    float turnedX = 1F - z;
                    z = x;
                    x = turnedX;
                }
                positions[vertex] = new Vector3f(x, y, z);
            }
            Direction direction = quad.direction();
            for (int turn = 0; turn < turns && direction.getAxis().isHorizontal(); turn++) {
                direction = direction.getClockWise();
            }
            result.add(new BakedQuad(positions[0], positions[1], positions[2], positions[3], quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(), quad.tintIndex(), direction, quad.sprite(), quad.shade(), quad.lightEmission()));
        }
        return result;
    }

    /** Key of the quads of one plant in one planter state. */
    public record Key(Block potted, Direction facing, Half half) {
        public static Key of(Block potted, BlockState planterState) {
            return new Key(potted, planterState.getValue(PlanterBlock.FACING), planterState.getValue(PlanterBlock.HALF));
        }
    }
}
