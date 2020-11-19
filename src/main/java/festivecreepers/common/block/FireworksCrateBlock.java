package festivecreepers.common.block;

import festivecreepers.common.entity.FireworksCrateEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.TNTBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FireworksCrateBlock extends TNTBlock {

    public FireworksCrateBlock(Properties properties) {
        super(properties);
    }

    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter);
    }

    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion) {
        if (!world.isRemote) {
            FireworksCrateEntity crate = new FireworksCrateEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5, explosion.getExplosivePlacedBy());
            crate.setFuse((short)(world.rand.nextInt(crate.getFuse() / 2) + crate.getFuse() / 2));
            world.addEntity(crate);
        }
    }

    private static void explode(World world, BlockPos pos, @Nullable LivingEntity entity) {
        if (!world.isRemote) {
            FireworksCrateEntity crate = new FireworksCrateEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, entity);
            world.addEntity(crate);
            world.playSound(null, crate.getPosX(), crate.getPosY(), crate.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1, 1);
        }
    }
}
