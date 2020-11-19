package festivecreepers.common.init;

import festivecreepers.FestiveCreepers;
import festivecreepers.common.block.FireworksCrateBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class Blocks {

    @ObjectHolder(FestiveCreepers.MODID + ":fireworks_crate")
    public static Block FIREWORKS_CRATE;

    public static void register(IForgeRegistry<Block> registry) {
        registry.register(new FireworksCrateBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(1, 2).sound(SoundType.WOOD)).setRegistryName(FestiveCreepers.MODID, "fireworks_crate"));
    }
}
