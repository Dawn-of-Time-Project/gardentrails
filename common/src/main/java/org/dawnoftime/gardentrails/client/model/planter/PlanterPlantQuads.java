package org.dawnoftime.gardentrails.client.model.planter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
    private static final ResourceLocation POT_SPRITE = new ResourceLocation("block/flower_pot");
    private static final ResourceLocation SOIL_SPRITE = new ResourceLocation("block/dirt");

    // Soil of a pot is at y=4, the planter top is at y=7, the plant is centered on the planter body (z=13)
    private static final float PLANT_SHIFT_Y = 3F / 16F;
    private static final float PLANT_SHIFT_Z = 5F / 16F;
    private static final float UPPER_HALF_SHIFT_Y = 0.5F;

    /** Set by the loader: gets a model registered with the additional models of the loader. */
    public static Function<ResourceLocation, BakedModel> standaloneModels = id -> null;

    private PlanterPlantQuads() {}

    /** Models that are not bound to a block and have to be loaded by the loader. */
    public static List<ResourceLocation> additionalModels() {
        List<ResourceLocation> models = new ArrayList<>();
        for (GeraniumColor color : GeraniumColor.values()) {
            models.add(leavesModel(color));
        }
        return models;
    }

    private static ResourceLocation leavesModel(GeraniumColor color) {
        return new ResourceLocation("gardentrails", "block/" + color.blockId() + "_planter");
    }

    /** The geranium has hanging leaves in a planter, a model made for it instead of the small potted plant. */
    private static ResourceLocation leavesModelOf(ResourceLocation pottedId) {
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
        ResourceLocation leaves = leavesModelOf(BuiltInRegistries.BLOCK.getKey(potted));
        List<BakedQuad> source;
        if (leaves != null && standaloneModels.apply(leaves) != null) {
            source = allQuads(standaloneModels.apply(leaves), null, false);
            return transform(source, 0F, shiftY, 0F, facing);
        }
        BlockState pottedState = potted.defaultBlockState();
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(pottedState);
        source = allQuads(model, pottedState, true);
        if (source.isEmpty() && potted instanceof FlowerPotBlock pot) {
            // The model of this pot has no recognizable pot texture, the plant is shown with its own block model
            BlockState plantState = pot.getContent().defaultBlockState();
            source = allQuads(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(plantState), plantState, false);
        }
        return transform(source, 0F, shiftY + PLANT_SHIFT_Y, PLANT_SHIFT_Z, facing);
    }

    private static List<BakedQuad> allQuads(BakedModel model, BlockState state, boolean withoutPot) {
        List<BakedQuad> quads = new ArrayList<>();
        RandomSource random = RandomSource.create(42L);
        for (int i = 0; i <= Direction.values().length; i++) {
            Direction side = i == Direction.values().length ? null : Direction.values()[i];
            for (BakedQuad quad : model.getQuads(state, side, random)) {
                if (!withoutPot || !isPot(quad)) {
                    quads.add(quad);
                }
            }
        }
        return quads;
    }

    private static boolean isPot(BakedQuad quad) {
        ResourceLocation name = quad.getSprite().contents().name();
        return name.equals(POT_SPRITE) || name.equals(SOIL_SPRITE);
    }

    private static List<BakedQuad> transform(List<BakedQuad> quads, float dx, float dy, float dz, Direction facing) {
        // Same turn as the y rotation of the blockstate: south 0, west 90, north 180, east 270
        int turns = facing.get2DDataValue();
        List<BakedQuad> result = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads) {
            int[] data = quad.getVertices().clone();
            for (int vertex = 0; vertex < 4; vertex++) {
                int base = vertex * 8;
                float x = Float.intBitsToFloat(data[base]) + dx;
                float y = Float.intBitsToFloat(data[base + 1]) + dy;
                float z = Float.intBitsToFloat(data[base + 2]) + dz;
                int packedNormal = data[base + 7];
                float nx = (byte) (packedNormal & 255);
                float nz = (byte) ((packedNormal >> 16) & 255);
                for (int turn = 0; turn < turns; turn++) {
                    float turnedX = 1F - z;
                    z = x;
                    x = turnedX;
                    float turnedNormalX = -nz;
                    nz = nx;
                    nx = turnedNormalX;
                }
                data[base] = Float.floatToRawIntBits(x);
                data[base + 1] = Float.floatToRawIntBits(y);
                data[base + 2] = Float.floatToRawIntBits(z);
                data[base + 7] = (packedNormal & 0xFF00FF00) | ((int) nx & 255) | (((int) nz & 255) << 16);
            }
            Direction direction = quad.getDirection();
            for (int turn = 0; turn < turns && direction.getAxis().isHorizontal(); turn++) {
                direction = direction.getClockWise();
            }
            result.add(new BakedQuad(data, quad.getTintIndex(), direction, quad.getSprite(), quad.isShade()));
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
