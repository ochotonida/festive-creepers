package festivecreepers.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import festivecreepers.FestiveCreepers;
import festivecreepers.client.render.model.FestiveHatModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FestiveHatLayer extends LayerRenderer<CreeperEntity, CreeperModel<CreeperEntity>> {

    private final FestiveHatModel model = new FestiveHatModel();

    public FestiveHatLayer(IEntityRenderer<CreeperEntity, CreeperModel<CreeperEntity>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, CreeperEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        model.hat.copyModelAngles(getEntityModel().head);
        IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutoutNoCull(new ResourceLocation(FestiveCreepers.MODID, "textures/entity/festive_hat.png")));
        model.render(matrixStack, builder, packedLight, LivingRenderer.getPackedOverlay(entity, 0), 1, 1, 1, 1);
    }
}
