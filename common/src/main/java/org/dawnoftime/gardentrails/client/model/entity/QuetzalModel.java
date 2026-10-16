package org.dawnoftime.gardentrails.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.client.renderer.entity.state.QuetzalRenderState;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * The mesh part of this class (fields, constructor and createBodyLayer) is the Blockbench export of the quetzal model.
 * Keep it identical to the export so the model can be re-exported later. Animations go only in setupAnim.
 */
@SuppressWarnings("FieldCanBeLocal")
public class QuetzalModel extends EntityModel<QuetzalRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "quetzal"), "main");
    // About 40 degrees
    private static final float LEG_TRAIL_ANGLE = 0.70F;
    // About 15 degrees, negative because the feathers extend along +Z so a negative pitch lowers their tip
    private static final float FEATHER_DROOP_ANGLE = -0.26F;
    // Cape pose on a player shoulder: about 54 degrees of droop (the feathers hang down, leaning backward)
    // The two outer feathers spread by about 15 degrees and the two middle ones by about 5 degrees on each side
    // The bird sits off center on the shoulder, so the whole fan leans about 24 degrees toward the middle of the back
    private static final float SHOULDER_FEATHER_DROOP = -0.95F;
    private static final float SHOULDER_FEATHER_SPREAD = 0.26F;
    private static final float SHOULDER_FEATHER_INNER_SPREAD = 0.09F;
    private static final float SHOULDER_TRAIN_DROOP = -0.80F;
    private static final float SHOULDER_FEATHER_LEAN = 0.42F;
    // About 17 degrees per beak half, so the beak opens by about 34 degrees in total
    private static final float BEAK_OPEN_ANGLE = 0.30F;
    private final ModelPart Body;
    private final ModelPart RightWingA;
    private final ModelPart RightWingB;
    private final ModelPart RightWingC;
    private final ModelPart LeftWingA;
    private final ModelPart LeftWingB;
    private final ModelPart LeftWingC;
    private final ModelPart Head;
    private final ModelPart Headcrete;
    private final ModelPart Beak;
    // The two beak halves (children of Beak), their origin is the rear of the cube so they pivot at the back of the beak
    private final ModelPart BeakUpper;
    private final ModelPart BeakLower;
    private final ModelPart Back;
    private final ModelPart RightTrain;
    private final ModelPart LeftTrain;
    private final ModelPart SecondaryMiddleFeatherA;
    private final ModelPart ScondaryMiddleFeatherB;
    private final ModelPart SecondaryMiddleFeatherA2;
    private final ModelPart ScondaryMiddleFeatherB2;
    private final ModelPart LeftLeg;
    private final ModelPart LeftFeet;
    private final ModelPart MiddleFeatherA;
    private final ModelPart MiddleFeatherB;
    private final ModelPart MiddleFeatherC;
    // Fourth tail feather, a copy of the middle one
    private final ModelPart MiddleFeatherA2;
    private final ModelPart MiddleFeatherB2;
    private final ModelPart MiddleFeatherC2;
    private final ModelPart RightLeg;
    private final ModelPart RightFeet;

    public QuetzalModel(ModelPart root) {
        super(root);
        this.Body = root.getChild("Body");
        this.RightWingA = this.Body.getChild("RightWingA");
        this.RightWingB = this.RightWingA.getChild("RightWingB");
        this.RightWingC = this.RightWingA.getChild("RightWingC");
        this.LeftWingA = this.Body.getChild("LeftWingA");
        this.LeftWingB = this.LeftWingA.getChild("LeftWingB");
        this.LeftWingC = this.LeftWingA.getChild("LeftWingC");
        this.Head = this.Body.getChild("Head");
        this.Headcrete = this.Head.getChild("Headcrete");
        this.Beak = this.Head.getChild("Beak");
        this.BeakUpper = this.Beak.getChild("BeakMain_r2");
        this.BeakLower = this.Beak.getChild("BeakMain_r1");
        this.Back = this.Body.getChild("Back");
        this.RightTrain = this.Back.getChild("RightTrain");
        this.LeftTrain = this.Back.getChild("LeftTrain");
        this.SecondaryMiddleFeatherA = this.Back.getChild("SecondaryMiddleFeatherA");
        this.ScondaryMiddleFeatherB = this.SecondaryMiddleFeatherA.getChild("ScondaryMiddleFeatherB");
        this.SecondaryMiddleFeatherA2 = this.Back.getChild("SecondaryMiddleFeatherA2");
        this.ScondaryMiddleFeatherB2 = this.SecondaryMiddleFeatherA2.getChild("ScondaryMiddleFeatherB2");
        this.LeftLeg = this.Back.getChild("LeftLeg");
        this.LeftFeet = this.LeftLeg.getChild("LeftFeet");
        this.MiddleFeatherA = this.Back.getChild("MiddleFeatherA");
        this.MiddleFeatherB = this.MiddleFeatherA.getChild("MiddleFeatherB");
        this.MiddleFeatherC = this.MiddleFeatherB.getChild("MiddleFeatherC");
        this.MiddleFeatherA2 = this.Back.getChild("MiddleFeatherA2");
        this.MiddleFeatherB2 = this.MiddleFeatherA2.getChild("MiddleFeatherB2");
        this.MiddleFeatherC2 = this.MiddleFeatherB2.getChild("MiddleFeatherC2");
        this.RightLeg = this.Back.getChild("RightLeg");
        this.RightFeet = this.RightLeg.getChild("RightFeet");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, 0.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.7F, -3.0F, -1.0928F, 0.0F, 0.0F));

        PartDefinition RightWingA = Body.addOrReplaceChild("RightWingA", CubeListBuilder.create().texOffs(46, 21).addBox(-0.7F, -1.3F, -0.2F, 1.0F, 3.0F, 8.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-2.5F, 0.1F, 0.8F, -0.0051F, 0.0221F, -0.0001F));

        PartDefinition RightWingB = RightWingA.addOrReplaceChild("RightWingB", CubeListBuilder.create().texOffs(48, 10).addBox(-0.5F, -1.5F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-0.2F, -1.4047F, 3.4297F, 0.4098F, 0.0F, 0.0F));

        PartDefinition RightWingC = RightWingA.addOrReplaceChild("RightWingC", CubeListBuilder.create().texOffs(50, 0).addBox(-0.7F, -1.3F, -0.2F, 1.0F, 3.0F, 6.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.9105F, 0.0F, 0.0F));

        PartDefinition LeftWingA = Body.addOrReplaceChild("LeftWingA", CubeListBuilder.create().texOffs(27, 21).addBox(-0.3F, -1.3F, -0.2F, 1.0F, 3.0F, 8.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(2.5F, 0.1F, 0.8F, 0.0051F, -0.0221F, -0.0001F));

        PartDefinition LeftWingB = LeftWingA.addOrReplaceChild("LeftWingB", CubeListBuilder.create().texOffs(29, 10).addBox(-0.5F, -1.5F, -3.5F, 1.0F, 3.0F, 7.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.2F, -1.4047F, 3.4297F, 0.4098F, 0.0F, 0.0F));

        PartDefinition LeftWingC = LeftWingA.addOrReplaceChild("LeftWingC", CubeListBuilder.create().texOffs(31, 0).addBox(-0.3F, -1.3F, -0.2F, 1.0F, 3.0F, 6.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.9105F, 0.0F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(42, 10).addBox(-1.5F, -2.0F, -1.8F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.2F, 1.2292F, 0.0F, 0.0F));

        PartDefinition Headcrete = Head.addOrReplaceChild("Headcrete", CubeListBuilder.create().texOffs(47, -3).addBox(0.0F, 13.5F, 0.1F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.7F, 2.8F, -0.2731F, 0.0F, 0.0F));

        PartDefinition Beak = Head.addOrReplaceChild("Beak", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -0.2F, 0.3187F, 0.0F, 0.0F));

        PartDefinition BeakMain_r1 = Beak.addOrReplaceChild("BeakMain_r1", CubeListBuilder.create().texOffs(41, 2).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -0.9119F, -0.3082F, -0.2182F, 0.0F, 0.0F));

        PartDefinition BeakMain_r2 = Beak.addOrReplaceChild("BeakMain_r2", CubeListBuilder.create().texOffs(41, 2).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, -1.4F, -0.2F, -0.2182F, 0.0F, 0.0F));

        PartDefinition Back = Body.addOrReplaceChild("Back", CubeListBuilder.create().texOffs(1, 10).addBox(-2.0F, -1.7F, 0.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 4.6F, 0.6374F, 0.0F, 0.0F));

        PartDefinition RightTrain = Back.addOrReplaceChild("RightTrain", CubeListBuilder.create().texOffs(12, 10).addBox(-3.6268F, 14.6728F, -8.0457F, 3.0F, 0.1F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.7F, 6.0F, 0.2731F, -0.7285F, 0.0456F));

        PartDefinition LeftTrain = Back.addOrReplaceChild("LeftTrain", CubeListBuilder.create().texOffs(6, 10).addBox(0.8268F, 14.6728F, -8.0457F, 3.0F, 0.1F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.7F, 6.0F, 0.2731F, 0.7285F, -0.0456F));

        PartDefinition SecondaryMiddleFeatherA = Back.addOrReplaceChild("SecondaryMiddleFeatherA", CubeListBuilder.create().texOffs(22, 36).addBox(-1.2031F, 11.326F, -11.6646F, 3.0F, 0.1F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -17.5F, 6.0F, 0.5918F, 0.0911F, 0.0F));

        PartDefinition ScondaryMiddleFeatherB = SecondaryMiddleFeatherA.addOrReplaceChild("ScondaryMiddleFeatherB", CubeListBuilder.create().texOffs(29, 35).addBox(-1.1031F, 11.426F, -11.6646F, 3.0F, 0.1F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1F, -0.1F, 14.0F));

        PartDefinition SecondaryMiddleFeatherA2 = Back.addOrReplaceChild("SecondaryMiddleFeatherA2", CubeListBuilder.create().texOffs(22, 36).addBox(-1.3015F, 5.713F, -16.3323F, 3.0F, 0.1F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.1646F, -14.4457F, 12.9672F, 0.5482F, -0.1271F, 0.0F));

        PartDefinition ScondaryMiddleFeatherB2 = SecondaryMiddleFeatherA2.addOrReplaceChild("ScondaryMiddleFeatherB2", CubeListBuilder.create().texOffs(29, 35).addBox(-1.1031F, 11.426F, -11.6646F, 3.0F, 0.1F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1985F, -5.713F, 9.3323F));

        PartDefinition LeftLeg = Back.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(1, 21).addBox(-1.0F, 5.7F, -5.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3F, -6.8667F, 3.2087F, 0.4554F, 0.0F, 0.0F));

        PartDefinition LeftFeet = LeftLeg.addOrReplaceChild("LeftFeet", CubeListBuilder.create().texOffs(3, 26).addBox(-1.0F, 8.7153F, 13.5922F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -6.7F, 4.5F, -1.5481F, 0.0F, 0.0F));

        PartDefinition MiddleFeatherA = Back.addOrReplaceChild("MiddleFeatherA", CubeListBuilder.create().texOffs(-13, 35).addBox(-1.4F, 12.0311F, -11.4816F, 3.0F, 0.1F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.7F, 6.0F, 0.5009F, 0.0F, 0.0F));

        PartDefinition MiddleFeatherB = MiddleFeatherA.addOrReplaceChild("MiddleFeatherB", CubeListBuilder.create(), PartPose.offset(0.0F, -0.4F, 14.0F));

        PartDefinition MiddleFeatherC = MiddleFeatherB.addOrReplaceChild("MiddleFeatherC", CubeListBuilder.create().texOffs(3, 35).addBox(-1.4F, 12.9311F, -17.8816F, 3.0F, 0.1F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 7.4F));

        // Fourth tail feather, same cubes as the middle feather with a slight yaw so it does not overlap it (not part of the Blockbench export)
        PartDefinition MiddleFeatherA2 = Back.addOrReplaceChild("MiddleFeatherA2", CubeListBuilder.create().texOffs(-13, 35).addBox(-1.4F, 12.0311F, -11.4816F, 3.0F, 0.1F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.7F, 6.0F, 0.5009F, -0.05F, 0.0F));

        PartDefinition MiddleFeatherB2 = MiddleFeatherA2.addOrReplaceChild("MiddleFeatherB2", CubeListBuilder.create(), PartPose.offset(0.0F, -0.4F, 14.0F));

        PartDefinition MiddleFeatherC2 = MiddleFeatherB2.addOrReplaceChild("MiddleFeatherC2", CubeListBuilder.create().texOffs(3, 35).addBox(-1.4F, 12.9311F, -17.8816F, 3.0F, 0.1F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 7.4F));

        PartDefinition RightLeg = Back.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(11, 21).addBox(-1.0F, 14.0F, -10.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.3F, -16.3F, 3.6F, 0.4554F, 0.0F, 0.0F));

        PartDefinition RightFeet = RightLeg.addOrReplaceChild("RightFeet", CubeListBuilder.create().texOffs(13, 26).addBox(-1.0F, 8.7153F, 13.5922F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 1.6F, 0.0F, -1.5481F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull QuetzalRenderState state) {
        // Back to the exported pose, then add the parrot animation on top
        super.setupAnim(state);
        if (state.onShoulder) {
            this.poseOnShoulder(state.yRot, state.xRot, state.leftShoulder);
        } else {
            this.animate(state.flapSpeed, state.flapAngle, state.yRot, state.xRot, state.beakOpen);
        }
    }

    /** Idle pose for the shoulder of a player: no flapping, only the head follows the view. */
    private void poseOnShoulder(float netHeadYaw, float headPitch, boolean leftShoulder) {
        this.animate(0.0F, 0.0F, netHeadYaw, headPitch, 0.0F);
        // The four long tail feathers hang down and spread like a cape on the back of the player
        // From the +X side to the -X side of the model: secondary feather A, middle feather, middle feather 2, secondary feather A2
        // The whole fan also leans toward the middle of the player's back: -X is toward the center from the left shoulder
        float lean = leftShoulder ? -SHOULDER_FEATHER_LEAN : SHOULDER_FEATHER_LEAN;
        fanAroundPoint(this.SecondaryMiddleFeatherA, SHOULDER_FEATHER_DROOP, lean + SHOULDER_FEATHER_SPREAD, 0.2969F, 11.376F, -11.6646F);
        fanAroundPoint(this.MiddleFeatherA, SHOULDER_FEATHER_DROOP, lean + SHOULDER_FEATHER_INNER_SPREAD, 0.1F, 12.0811F, -11.4816F);
        fanAroundPoint(this.MiddleFeatherA2, SHOULDER_FEATHER_DROOP, lean - SHOULDER_FEATHER_INNER_SPREAD, 0.1F, 12.0811F, -11.4816F);
        fanAroundPoint(this.SecondaryMiddleFeatherA2, SHOULDER_FEATHER_DROOP, lean - SHOULDER_FEATHER_SPREAD, 0.1985F, 5.713F, -16.3323F);
        // The two short white feathers (trains) follow the cape: same lean, and lowered, each one stays on its own side
        fanAroundPoint(this.LeftTrain, SHOULDER_TRAIN_DROOP, lean, 2.3268F, 14.7228F, -8.0457F);
        fanAroundPoint(this.RightTrain, SHOULDER_TRAIN_DROOP, lean, -2.1268F, 14.7228F, -8.0457F);
    }

    private void animate(float flapSpeed, float ageInTicks, float netHeadYaw, float headPitch, float beakOpen) {
        // The beak halves tilt in opposite directions around their rear edge (negative X rotation raises the tip)
        this.BeakUpper.xRot -= beakOpen * BEAK_OPEN_ANGLE;
        this.BeakLower.xRot += beakOpen * BEAK_OPEN_ANGLE;

        this.Head.xRot += headPitch * ((float) Math.PI / 180F);
        // The body is pitched by about 63 degrees, so the body Z axis is the near vertical one (head yaw)
        // and the wings extend along Z, so they flap around Y
        this.Head.zRot += netHeadYaw * ((float) Math.PI / 180F);
        this.LeftWingA.yRot += 0.0873F + ageInTicks;
        this.RightWingA.yRot += -0.0873F - ageInTicks;
        // Vertical oscillation of the whole body, the children follow
        this.Body.y += ageInTicks * 0.3F;

        // Legs trail backward and tail feathers droop in flight, blended in and out by the flap speed (0 on the ground, 1 in flight)
        float legSwing = flapSpeed * LEG_TRAIL_ANGLE;
        float featherDroop = flapSpeed * FEATHER_DROOP_ANGLE;
        // Pivots are the attach point of each cube in the part frame, not the part origin, so nothing drifts away from the body
        pitchAroundPoint(this.LeftLeg, legSwing, 0.0F, 5.7F, -4.5F);
        pitchAroundPoint(this.RightLeg, legSwing, 0.0F, 14.0F, -9.0F);
        pitchAroundPoint(this.RightTrain, featherDroop, -2.1268F, 14.7228F, -8.0457F);
        pitchAroundPoint(this.LeftTrain, featherDroop, 2.3268F, 14.7228F, -8.0457F);
        pitchAroundPoint(this.SecondaryMiddleFeatherA, featherDroop, 0.2969F, 11.376F, -11.6646F);
        pitchAroundPoint(this.SecondaryMiddleFeatherA2, featherDroop, 0.1985F, 5.713F, -16.3323F);
        pitchAroundPoint(this.MiddleFeatherA, featherDroop, 0.1F, 12.0811F, -11.4816F);
        pitchAroundPoint(this.MiddleFeatherA2, featherDroop, 0.1F, 12.0811F, -11.4816F);
    }

    /**
     * Adds an X rotation to a part around a point given in the part frame instead of the part origin.
     * The Blockbench export places the cubes far from the part origin, so a plain xRot would swing the part through space.
     * The part position is shifted so the pivot point stays where it was.
     */
    private static void pitchAroundPoint(ModelPart part, float extra, float qx, float qy, float qz) {
        Vector3f before = new Vector3f(qx, qy, qz).rotateX(part.xRot).rotateY(part.yRot).rotateZ(part.zRot);
        part.xRot += extra;
        Vector3f after = new Vector3f(qx, qy, qz).rotateX(part.xRot).rotateY(part.yRot).rotateZ(part.zRot);
        part.x += before.x - after.x;
        part.y += before.y - after.y;
        part.z += before.z - after.z;
    }

    /**
     * Spreads a feather sideways (rotation around the feather local Y axis) then lowers it (pitch around the local X axis),
     * both around a point given in the part frame. The feather extends along +Z, so a negative pitch lowers its tip.
     * The part position is shifted so the pivot point stays where it was.
     */
    private static void fanAroundPoint(ModelPart part, float droop, float spread, float qx, float qy, float qz) {
        Quaternionf original = new Quaternionf().rotationZYX(part.zRot, part.yRot, part.xRot);
        Quaternionf fanned = new Quaternionf(original).rotateX(droop).rotateY(spread);
        Vector3f before = original.transform(new Vector3f(qx, qy, qz));
        Vector3f after = fanned.transform(new Vector3f(qx, qy, qz));
        part.x += before.x - after.x;
        part.y += before.y - after.y;
        part.z += before.z - after.z;
        // The rotation matrix of a part is Rz * Ry * Rx, the angles are read back from it
        // (JOML getEulerAnglesZYX is not used, it returns a wrong roll in the bundled version)
        Matrix3f matrix = new Matrix3f().set(fanned);
        part.xRot = (float) java.lang.Math.atan2(matrix.m12, matrix.m22);
        part.yRot = (float) java.lang.Math.asin(-matrix.m02);
        part.zRot = (float) java.lang.Math.atan2(matrix.m01, matrix.m00);
    }
}
