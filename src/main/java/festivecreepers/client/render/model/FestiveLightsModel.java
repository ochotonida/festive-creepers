package festivecreepers.client.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class FestiveLightsModel extends EntityModel<Entity> {

    public final ModelRenderer lights;

    public FestiveLightsModel() {
        textureWidth = 32;
        textureHeight = 32;

        lights = new ModelRenderer(this);
        lights.setRotationPoint(0.0F, 12.0F, 0.0F);
        lights.addBox(-4, 0, -2, 8, 12, 4);

        ModelRenderer light1 = new ModelRenderer(this);
        light1.setRotationPoint(1, 1.5F, -2);
        lights.addChild(light1);
        light1.setTextureOffset(0, 16).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light2 = new ModelRenderer(this);
        light2.setRotationPoint(-4, 2.5F, -2);
        lights.addChild(light2);
        setRotationAngle(light2, 0, 0.7854F, 0);
        light2.setTextureOffset(6, 16).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light3 = new ModelRenderer(this);
        light3.setRotationPoint(-2, 3.5F, 2);
        lights.addChild(light3);
        setRotationAngle(light3, 0, 3.1416F, 0);
        light3.setTextureOffset(12, 16).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light4 = new ModelRenderer(this);
        light4.setRotationPoint(4, 3.5F, 1);
        lights.addChild(light4);
        setRotationAngle(light4, 0, -1.5708F, 0);
        light4.setTextureOffset(18, 16).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light5 = new ModelRenderer(this);
        light5.setRotationPoint(-2, 6.5F, -2);
        lights.addChild(light5);
        light5.setTextureOffset(24, 16).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light6 = new ModelRenderer(this);
        light6.setRotationPoint(-4, 6.5F, 1);
        lights.addChild(light6);
        setRotationAngle(light6, 0, 1.5708F, 0);
        light6.setTextureOffset(0, 22).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light7 = new ModelRenderer(this);
        light7.setRotationPoint(2, 7.5F, 2);
        lights.addChild(light7);
        setRotationAngle(light7, 0, 3.1416F, 0);
        light7.setTextureOffset(6, 22).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light8 = new ModelRenderer(this);
        light8.setRotationPoint(4, 8.5F, -2);
        lights.addChild(light8);
        setRotationAngle(light8, 0, -0.7854F, 0);
        light8.setTextureOffset(12, 22).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light9 = new ModelRenderer(this);
        light9.setRotationPoint(-4, 9.5F, 2);
        lights.addChild(light9);
        setRotationAngle(light9, 0, 2.3562F, 0);
        light9.setTextureOffset(18, 22).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);

        ModelRenderer light10 = new ModelRenderer(this);
        light10.setRotationPoint(4, 10.5F, 2);
        lights.addChild(light10);
        setRotationAngle(light10, 0, -2.3562F, 0);
        light10.setTextureOffset(24, 22).addBox(0, -1.5F, -3, 0, 3, 3, 0.01F);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        lights.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}