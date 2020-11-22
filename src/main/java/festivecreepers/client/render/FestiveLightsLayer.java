package festivecreepers.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import festivecreepers.FestiveCreepers;
import festivecreepers.client.RenderTypes;
import festivecreepers.client.render.model.FestiveLightsModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;

public class FestiveLightsLayer extends LayerRenderer<CreeperEntity, CreeperModel<CreeperEntity>> {

    private final FestiveLightsModel model = new FestiveLightsModel();

    public FestiveLightsLayer(IEntityRenderer<CreeperEntity, CreeperModel<CreeperEntity>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, CreeperEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.push();
        model.lights.copyModelAngles(getEntityModel().body);
        matrixStack.translate(0, -1 / 16F, 0);
        matrixStack.scale(9 / 8F, 13 / 12F, 5 / 4F);
        IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation(FestiveCreepers.MODID, "textures/entity/festive_lights.png")));
        model.render(matrixStack, builder, packedLight, LivingRenderer.getPackedOverlay(entity, 0), 1, 1, 1, 1);

        builder = buffer.getBuffer(RenderTypes.unlit(new ResourceLocation(FestiveCreepers.MODID, "textures/entity/festive_lights_glow.png")));
        model.render(matrixStack, builder, 0xF000F0, LivingRenderer.getPackedOverlay(entity, 0), 1, 1, 1, 1);

        matrixStack.pop();
    }
}
