package festivecreepers.common.entity;

import festivecreepers.common.FireworksHelper;
import festivecreepers.common.init.Blocks;
import festivecreepers.common.init.EntityTypes;
import festivecreepers.common.init.Items;
import festivecreepers.common.network.FireworkExplosionPacket;
import festivecreepers.common.network.NetworkHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;

public class FireworksCrateMinecartEntity extends AbstractMinecartEntity {

    private int rocketsRemaining = 5;
    private int rocketCooldown = 0;

    public FireworksCrateMinecartEntity(EntityType<? extends FireworksCrateMinecartEntity> type, World world) {
        super(type, world);
    }

    public FireworksCrateMinecartEntity(World world, double x, double y, double z) {
        this(EntityTypes.FIREWORKS_CRATE_MINECART, world);
        setPosition(x, y, z);
        setMotion(Vector3d.ZERO);
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
    }

    public AbstractMinecartEntity.Type getMinecartType() {
        return AbstractMinecartEntity.Type.TNT;
    }

    @Override
    public BlockState getDefaultDisplayTile() {
        return Blocks.FIREWORKS_CRATE.getDefaultState();
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(Items.FIREWORKS_CRATE_MINECART);
    }

    @Override
    public void killMinecart(DamageSource source) {
        super.killMinecart(source);
        if (!source.isExplosion() && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS) && rocketsRemaining > 0) {
            entityDropItem(new ItemStack(Items.FIREWORK_POWDER, rocketsRemaining));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (rocketCooldown > 0) {
            rocketCooldown--;
        }
    }

    @Override
    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
        if (receivingPower && !world.isRemote() && rocketCooldown <= 0) {
            if (rocketsRemaining-- > 0) {
                FireworksHelper.spawnRandomRocket(rand, this);
                rocketCooldown = 10;
            } else {
                explodeCart(horizontalMag(getMotion()));
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getImmediateSource();
        if (entity instanceof AbstractArrowEntity) {
            AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity)entity;
            if (abstractarrowentity.isBurning()) {
                this.explodeCart(abstractarrowentity.getMotion().lengthSquared());
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    protected void explodeCart(double radiusModifier) {
        if (!world.isRemote()) {
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new FireworkExplosionPacket(
                    FireworksHelper.createRandomExplosion(rand, FireworkRocketItem.Shape.LARGE_BALL),
                    getPosX(),
                    getPosY(),
                    getPosZ(),
                    getMotion()
            ));
            world.createExplosion(this, getPosX(), getPosY(), getPosZ(), (float)(4 + rand.nextDouble() * 1.5 * Math.min(5, Math.sqrt(radiusModifier))), Explosion.Mode.NONE);
            remove();
        }
    }

    public float getExplosionResistance(Explosion explosionIn, IBlockReader worldIn, BlockPos pos, BlockState blockStateIn, FluidState fluidState, float explosionPower) {
        return !blockStateIn.isIn(BlockTags.RAILS) && !worldIn.getBlockState(pos.up()).isIn(BlockTags.RAILS) ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn, fluidState, explosionPower) : 0.0F;
    }

    public boolean canExplosionDestroyBlock(Explosion explosionIn, IBlockReader worldIn, BlockPos pos, BlockState blockStateIn, float explosionPower) {
        return (!blockStateIn.isIn(BlockTags.RAILS) && !worldIn.getBlockState(pos.up()).isIn(BlockTags.RAILS)) && super.canExplosionDestroyBlock(explosionIn, worldIn, pos, blockStateIn, explosionPower);
    }

    @Override
    public @Nonnull
    IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("RocketsRemaining", 99)) {
            rocketsRemaining = compound.getInt("RocketsRemaining");
        }
        if (compound.contains("RocketCooldown", 99)) {
            rocketCooldown = compound.getInt("RocketCooldown");
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("RocketsRemaining", rocketsRemaining);
        compound.putInt("RocketCooldown", rocketCooldown);
    }
}
