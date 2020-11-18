package festivecreepers.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FestiveHatModel extends EntityModel<Entity> {

	public final ModelRenderer hat;

	public FestiveHatModel() {
		textureWidth = 64;
		textureHeight = 64;

		hat = new ModelRenderer(this);
		hat.setRotationPoint(0, 24, 0);
		hat.setTextureOffset(0, 0).addBox(-5, -10, -5, 10, 3, 10, 0, false);
		hat.setTextureOffset(0, 13).addBox(-5, -10, -5, 10, 3, 10, 0.5F, false);

		ModelRenderer hatPart2 = new ModelRenderer(this);
		hatPart2.setRotationPoint(-4, -10, -4);
		hat.addChild(hatPart2);
		setRotationAngle(hatPart2, -0.1745F, 0, 0.1745F);
		hatPart2.setTextureOffset(0, 26).addBox(0, -5, 0, 8, 5, 8, 0, false);

		ModelRenderer hatPart3 = new ModelRenderer(this);
		hatPart3.setRotationPoint(1, -5, 1);
		hatPart2.addChild(hatPart3);
		setRotationAngle(hatPart3, -0.3491F, 0, 0.3491F);
		hatPart3.setTextureOffset(0, 39).addBox(0, -6, 0, 5, 6, 5, 0, false);

		ModelRenderer hatPart4 = new ModelRenderer(this);
		hatPart4.setRotationPoint(1, -6, 1);
		hatPart3.addChild(hatPart4);
		setRotationAngle(hatPart4, -0.3491F, 0, 0.3491F);
		hatPart4.setTextureOffset(0, 50).addBox(0, -3, 0, 2, 3, 2, 0, false);

		ModelRenderer hatPart5 = new ModelRenderer(this);
		hatPart5.setRotationPoint(1, -3, 1);
		hatPart4.addChild(hatPart5);
		hatPart5.setTextureOffset(0, 55).addBox(-1.5F, -3, -1.5F, 3, 3, 3, 0, false);
		hatPart5.setTextureOffset(12, 55).addBox(-1.5F, -3, -1.5F, 3, 3, 3, 0.5F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){

	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		hat.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}