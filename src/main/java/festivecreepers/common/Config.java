package festivecreepers.common;

import festivecreepers.FestiveCreepers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = FestiveCreepers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static final ForgeConfigSpec COMMON_SPEC;
    private static final CommonConfig COMMON;
    public static Set<ResourceLocation> biomeBlacklist;
    public static int snowyBiomeSpawnWeight;
    public static int decemberSpawnWeight;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static void bakeCommon() {
        Config.biomeBlacklist = COMMON.biomeBlacklist.get().stream().map(ResourceLocation::new).collect(Collectors.toSet());
        Config.snowyBiomeSpawnWeight = COMMON.snowyBiomeSpawnWeight.get();
        Config.decemberSpawnWeight = COMMON.decemberSpawnWeight.get();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void onModConfigEvent(ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == Config.COMMON_SPEC) {
            bakeCommon();
        }
    }

    public static class CommonConfig {

        final ForgeConfigSpec.ConfigValue<List<String>> biomeBlacklist;
        final ForgeConfigSpec.ConfigValue<Integer> snowyBiomeSpawnWeight;
        final ForgeConfigSpec.ConfigValue<Integer> decemberSpawnWeight;

        CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("spawns");
            biomeBlacklist = builder
                    .worldRestart()
                    .comment("List of biome IDs in which festive creepers are not allowed to spawn. End and nether biomes are excluded by default. Festive creepers only spawn in snowy biomes, except during the month of december, during which they spawn in any biome")
                    .translation(FestiveCreepers.MODID + ".config.biome_blacklist")
                    .define("biome_blacklist", Collections.emptyList());
            snowyBiomeSpawnWeight = builder
                    .worldRestart()
                    .comment("Spawn weight of festive creepers in snowy biomes, in groups of 1-4")
                    .translation(FestiveCreepers.MODID + ".config.snowy_biome_spawn_weight")
                    .define("snowy_biome_spawn_weight", 20);
            decemberSpawnWeight = builder
                    .worldRestart()
                    .comment("Spawn weight of festive creepers in any biome during december, in groups of 1-2")
                    .translation(FestiveCreepers.MODID + ".config.december_spawn_weight")
                    .define("december_spawn_weight", 10);
            builder.pop();
        }
    }
}