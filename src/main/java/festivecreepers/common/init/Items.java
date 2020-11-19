package festivecreepers.common.init;

import festivecreepers.FestiveCreepers;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.registries.IForgeRegistry;

public class Items {

    public static void register(IForgeRegistry<Item> registry) {
        registry.register(new SpawnEggItem(EntityTypes.FESTIVE_CREEPER, 894731, 0x0088ff, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(FestiveCreepers.MODID, "festive_creeper_spawn_egg"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(FestiveCreepers.MODID, "firework_powder"));
        registry.register(new BlockItem(Blocks.FIREWORKS_CRATE, new Item.Properties().group(ItemGroup.REDSTONE)).setRegistryName(FestiveCreepers.MODID, "fireworks_crate"));
    }
}
