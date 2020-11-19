package festivecreepers.common.item;

import festivecreepers.common.entity.FireworksCrateMinecartEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireworksCrateMinecartItem extends Item {

    public static final IDispenseItemBehavior DISPENSE_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior behaviourDefaultDispenseItem = new DefaultDispenseItemBehavior();

        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            Direction direction = source.getBlockState().get(DispenserBlock.FACING);
            World world = source.getWorld();
            double x = source.getX() + direction.getXOffset() * 1.125;
            double y = Math.floor(source.getY()) + direction.getYOffset();
            double z = source.getZ() + direction.getZOffset() * 1.125;
            BlockPos pos = source.getBlockPos().offset(direction);
            BlockState rail = world.getBlockState(pos);
            RailShape railShape = rail.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock)rail.getBlock()).getRailDirection(rail, world, pos, null) : RailShape.NORTH_SOUTH;
            if (rail.isIn(BlockTags.RAILS)) {
                if (railShape.isAscending()) {
                    y += 0.6;
                } else {
                    y += 0.1;
                }
            } else {
                if (!rail.isAir() || !world.getBlockState(pos.down()).isIn(BlockTags.RAILS)) {
                    return this.behaviourDefaultDispenseItem.dispense(source, stack);
                }

                rail = world.getBlockState(pos.down());
                railShape = rail.getBlock() instanceof AbstractRailBlock ? rail.get(((AbstractRailBlock) rail.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && railShape.isAscending()) {
                    y -= 0.4;
                } else {
                    y -= 0.9;
                }
            }

            AbstractMinecartEntity minecart = new FireworksCrateMinecartEntity(world, x, y, z);
            if (stack.hasDisplayName()) {
                minecart.setCustomName(stack.getDisplayName());
            }

            world.addEntity(minecart);
            stack.shrink(1);
            return stack;
        }

        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playEvent(1000, source.getBlockPos(), 0);
        }
    };

    public FireworksCrateMinecartItem(Item.Properties builder) {
        super(builder);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState rail = world.getBlockState(pos);
        if (!rail.isIn(BlockTags.RAILS)) {
            return ActionResultType.FAIL;
        } else {
            ItemStack itemstack = context.getItem();
            if (!world.isRemote) {
                RailShape railShape = rail.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock) rail.getBlock()).getRailDirection(rail, world, pos, null) : RailShape.NORTH_SOUTH;
                double yOffset = 0;
                if (railShape.isAscending()) {
                    yOffset = 0.5;
                }

                AbstractMinecartEntity minecart = new FireworksCrateMinecartEntity(world, pos.getX() + 0.5, pos.getY() + 0.0625 + yOffset, pos.getZ() + 0.5);
                if (itemstack.hasDisplayName()) {
                    minecart.setCustomName(itemstack.getDisplayName());
                }

                world.addEntity(minecart);
            }

            itemstack.shrink(1);
            return ActionResultType.func_233537_a_(world.isRemote);
        }
    }
}
