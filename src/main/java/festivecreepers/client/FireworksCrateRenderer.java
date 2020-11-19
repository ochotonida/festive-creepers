package festivecreepers.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import festivecreepers.common.entity.FireworksCrateEntity;
import festivecreepers.common.init.Blocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public class FireworksCrateRenderer extends EntityRenderer<FireworksCrateEntity> {

    public FireworksCrateRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    public void render(FireworksCrateEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.push();
        matrixStack.translate(0, 0, 0);
        if ((float)entity.getFuse() - partialTicks + 1 < 10) {
            float f = 1.0F - ((float)entity.getFuse() - partialTicks + 1) / 10;
            f = MathHelper.clamp(f, 0, 1);
            f = f * f;
            f = f * f;
            float f1 = 1 + f * 0.3F;
            matrixStack.scale(f1, f1, f1);
        }

        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5D, 0, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderTntFlash(Blocks.FIREWORKS_CRATE.getDefaultState(), matrixStack, buffer, packedLight, entity.getFuse() / 5 % 2 == 0);
        matrixStack.pop();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    public @Nonnull ResourceLocation getEntityTexture(FireworksCrateEntity entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }
}
