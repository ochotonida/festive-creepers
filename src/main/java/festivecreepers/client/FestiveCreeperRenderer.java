package festivecreepers.client;

import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class FestiveCreeperRenderer extends CreeperRenderer {

    public FestiveCreeperRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        addLayer(new FestiveHatLayer(this));
    }
}
