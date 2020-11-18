package festivecreepers;

import festivecreepers.client.FestiveCreeperRenderer;
import festivecreepers.common.entity.FestiveCreeperEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Calendar;

@Mod("festive_creepers")
public class FestiveCreepers {

    public static final String MODID = "festive_creepers";

    public static final EntityType<FestiveCreeperEntity> FESTIVE_CREEPER = EntityType.Builder.create(FestiveCreeperEntity::new, EntityClassification.MONSTER).size(0.6F, 1.7F).trackingRange(8).build(new ResourceLocation(MODID, "festive_creeper").toString());

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(FESTIVE_CREEPER, FestiveCreeperRenderer::new);
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            FESTIVE_CREEPER.setRegistryName(MODID, "festive_creeper");
            event.getRegistry().registerAll(FESTIVE_CREEPER);
            EntitySpawnPlacementRegistry.register(FESTIVE_CREEPER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
            GlobalEntityTypeAttributes.put(FESTIVE_CREEPER, CreeperEntity.registerAttributes().create());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new SpawnEggItem(FESTIVE_CREEPER, 894731, 0x0088ff, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(new ResourceLocation(MODID, "festive_creeper_spawn_egg")));
        }
    }

    @Mod.EventBusSubscriber
    public static class Events {

        private static final boolean isDecember = Calendar.getInstance().get(Calendar.MONTH) + 1 == 12;

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void addSpawns(BiomeLoadingEvent event) {
            if (event.getClimate().precipitation == Biome.RainType.SNOW) {
                event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(FESTIVE_CREEPER, 20, 1, 4));
            } else if (isDecember) {
                event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(FESTIVE_CREEPER, 10, 1, 2));
            }
        }
    }
}
