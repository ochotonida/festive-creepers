package festivecreepers;

import festivecreepers.client.FestiveCreeperRenderer;
import festivecreepers.client.FireworksCrateRenderer;
import festivecreepers.common.init.Blocks;
import festivecreepers.common.init.EntityTypes;
import festivecreepers.common.init.Items;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("festive_creepers")
public class FestiveCreepers {

    public static final String MODID = "festive_creepers";


    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(EntityTypes.FESTIVE_CREEPER, FestiveCreeperRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(EntityTypes.FIREWORKS_CRATE, FireworksCrateRenderer::new);
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            EntityTypes.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            Items.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            Blocks.register(event.getRegistry());
        }
    }
}
