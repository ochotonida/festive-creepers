package festivecreepers;

import festivecreepers.common.entity.FireworksCrateEntity;
import festivecreepers.common.init.Blocks;
import festivecreepers.common.init.EntityTypes;
import festivecreepers.common.init.Items;
import festivecreepers.common.item.FireworksCrateMinecartItem;
import festivecreepers.common.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod("festive_creepers")
public class FestiveCreepers {

    public static final String MODID = "festive_creepers";

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            EntityTypes.registerRenderers();
        }

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                DispenserBlock.registerDispenseBehavior(Blocks.FIREWORKS_CRATE, new DefaultDispenseItemBehavior() {
                protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                    World world = source.getWorld();
                    BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                    FireworksCrateEntity crate = new FireworksCrateEntity(world, blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5, null);
                    world.addEntity(crate);
                    world.playSound(null, crate.getPosX(), crate.getPosY(), crate.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1, 1);
                    stack.shrink(1);
                    return stack;
                }});
                DispenserBlock.registerDispenseBehavior(Items.FIREWORKS_CRATE_MINECART, FireworksCrateMinecartItem.DISPENSE_BEHAVIOR);
                NetworkHandler.register();
            });
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
