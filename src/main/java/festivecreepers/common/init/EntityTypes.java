package festivecreepers.common.init;

import festivecreepers.FestiveCreepers;
import festivecreepers.common.entity.FestiveCreeperEntity;
import festivecreepers.common.entity.FireworksCrateEntity;
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
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Calendar;


@Mod.EventBusSubscriber(modid = FestiveCreepers.MODID)
public class EntityTypes {

    private static final boolean isDecember = Calendar.getInstance().get(Calendar.MONTH) + 1 == 12;

    public static EntityType<FestiveCreeperEntity> FESTIVE_CREEPER = EntityType.Builder.create(FestiveCreeperEntity::new, EntityClassification.MONSTER).size(0.6F, 1.7F).trackingRange(8).build(new ResourceLocation(FestiveCreepers.MODID, "festive_creeper").toString());
    public static EntityType<FireworksCrateEntity> FIREWORKS_CRATE = EntityType.Builder.<FireworksCrateEntity>create(FireworksCrateEntity::new, EntityClassification.MISC).immuneToFire().size(0.98F, 0.98F).trackingRange(10).func_233608_b_(10).build(new ResourceLocation(FestiveCreepers.MODID, "fireworks_crate").toString());

    public static void register(IForgeRegistry<EntityType<?>> registry) {
        FESTIVE_CREEPER.setRegistryName(FestiveCreepers.MODID, "festive_creeper");
        FIREWORKS_CRATE.setRegistryName(FestiveCreepers.MODID, "fireworks_crate");
        registry.registerAll(FESTIVE_CREEPER, FIREWORKS_CRATE);
        EntitySpawnPlacementRegistry.register(FESTIVE_CREEPER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        GlobalEntityTypeAttributes.put(FESTIVE_CREEPER, CreeperEntity.registerAttributes().create());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addSpawns(BiomeLoadingEvent event) {
        if (event.getClimate().precipitation == Biome.RainType.SNOW) {
            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(EntityTypes.FESTIVE_CREEPER, 20, 1, 4));
        } else if (isDecember) {
            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(EntityTypes.FESTIVE_CREEPER, 10, 1, 2));
        }
    }
}
