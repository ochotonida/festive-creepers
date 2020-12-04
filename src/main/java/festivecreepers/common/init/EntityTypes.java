package festivecreepers.common.init;

import festivecreepers.FestiveCreepers;
import festivecreepers.client.render.FestiveHatLayer;
import festivecreepers.client.render.FestiveLightsLayer;
import festivecreepers.client.render.FireworksCrateRenderer;
import festivecreepers.common.Config;
import festivecreepers.common.entity.FestiveCreeperEntity;
import festivecreepers.common.entity.FireworksCrateEntity;
import festivecreepers.common.entity.FireworksCrateMinecartEntity;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Calendar;


@Mod.EventBusSubscriber(modid = FestiveCreepers.MODID)
public class EntityTypes {

    private static final boolean isDecember = Calendar.getInstance().get(Calendar.MONTH) + 1 == 12;

    public static EntityType<FestiveCreeperEntity> FESTIVE_CREEPER = EntityType.Builder.create(FestiveCreeperEntity::new, EntityClassification.MONSTER).size(0.6F, 1.7F).trackingRange(8).build(new ResourceLocation(FestiveCreepers.MODID, "festive_creeper").toString());
    public static EntityType<FireworksCrateEntity> FIREWORKS_CRATE = EntityType.Builder.<FireworksCrateEntity>create(FireworksCrateEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).trackingRange(10).func_233608_b_(10).build(new ResourceLocation(FestiveCreepers.MODID, "fireworks_crate").toString());
    public static EntityType<FireworksCrateMinecartEntity> FIREWORKS_CRATE_MINECART = EntityType.Builder.<FireworksCrateMinecartEntity>create(FireworksCrateMinecartEntity::new, EntityClassification.MISC).size(0.98F, 0.7F).trackingRange(8).build(new ResourceLocation(FestiveCreepers.MODID, "fireworks_crate_minecart").toString());

    public static void register(IForgeRegistry<EntityType<?>> registry) {
        FESTIVE_CREEPER.setRegistryName(FestiveCreepers.MODID, "festive_creeper");
        FIREWORKS_CRATE.setRegistryName(FestiveCreepers.MODID, "fireworks_crate");
        FIREWORKS_CRATE_MINECART.setRegistryName(FestiveCreepers.MODID, "fireworks_crate_minecart");

        registry.registerAll(FESTIVE_CREEPER, FIREWORKS_CRATE, FIREWORKS_CRATE_MINECART);
        EntitySpawnPlacementRegistry.register(FESTIVE_CREEPER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        GlobalEntityTypeAttributes.put(FESTIVE_CREEPER, CreeperEntity.registerAttributes().create());
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.FESTIVE_CREEPER, (manager) -> {
            CreeperRenderer renderer = new CreeperRenderer(manager);
            renderer.addLayer(new FestiveHatLayer(renderer));
            renderer.addLayer(new FestiveLightsLayer(renderer));
            return renderer;
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.FIREWORKS_CRATE, FireworksCrateRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.FIREWORKS_CRATE_MINECART, MinecartRenderer::new);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addSpawns(BiomeLoadingEvent event) {
        if (!Config.biomeBlacklist.contains(event.getName()) && event.getCategory() != Biome.Category.NETHER && event.getCategory() != Biome.Category.THEEND) {
            if (Config.snowyBiomeSpawnWeight > 0 && event.getClimate().precipitation == Biome.RainType.SNOW) {
                event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(EntityTypes.FESTIVE_CREEPER, Config.snowyBiomeSpawnWeight, 1, 4));
            } else if (Config.decemberSpawnWeight > 0 && isDecember) {
                event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(EntityTypes.FESTIVE_CREEPER, Config.decemberSpawnWeight, 1, 2));
            }
        }
    }
}
