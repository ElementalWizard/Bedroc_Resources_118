package com.alexvr.bedres.entities.treckingcreeper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class TreckingCreeperModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("bedres", "trecking_creeper"), "main");
	private final ModelPart head;
	private final ModelPart backpack;
	private final ModelPart body;
	private final ModelPart left_front_leg;
	private final ModelPart left_hind_leg;
	private final ModelPart right_hind_leg;
	private final ModelPart right_front_leg;


	public TreckingCreeperModel(ModelPart root) {
		this.head = root.getChild("head");
		this.backpack = root.getChild("backpack");
		this.body = root.getChild("body");
		this.left_front_leg = root.getChild("left_front_leg");
		this.left_hind_leg = root.getChild("left_hind_leg");
		this.right_hind_leg = root.getChild("right_hind_leg");
		this.right_front_leg = root.getChild("right_front_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, -4.0F, 1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 2).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float pLimbSwing, float pLimbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 315F);
		this.right_hind_leg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.2F * pLimbSwingAmount;
		this.left_hind_leg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.2F * pLimbSwingAmount;
		this.right_front_leg.xRot = Mth.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.2F * pLimbSwingAmount;
		this.left_front_leg.xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.2F * pLimbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
		backpack.render(poseStack, buffer, packedLight, packedOverlay);
		body.render(poseStack, buffer, packedLight, packedOverlay);
		left_front_leg.render(poseStack, buffer, packedLight, packedOverlay);
		left_hind_leg.render(poseStack, buffer, packedLight, packedOverlay);
		right_front_leg.render(poseStack, buffer, packedLight, packedOverlay);
		right_hind_leg.render(poseStack, buffer, packedLight, packedOverlay);
	}

}