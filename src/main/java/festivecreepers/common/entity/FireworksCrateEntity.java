package festivecreepers.common.entity;

import festivecreepers.common.init.EntityTypes;
import festivecreepers.common.network.FireworkExplosionPacket;
import festivecreepers.common.network.NetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.network.IPacket;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FireworksCrateEntity extends TNTEntity {

    public FireworksCrateEntity(EntityType<? extends FireworksCrateEntity> type, World world) {
        super(type, world);
    }

    public FireworksCrateEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityTypes.FIREWORKS_CRATE, world);
        setPosition(x, y, z);
        double direction = world.rand.nextDouble() * Math.PI * 2;
        setMotion(-Math.sin(direction) * 0.02, 0.2, -Math.cos(direction) * 0.02);
        setFuse(120);
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        tntPlacedBy = igniter;
    }

    @Override
    public @Nonnull IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote() && getFuse() > 0 && getFuse() <= 60 && getFuse() % 10 == 0) {
            FireworksHelper.spawnRandomRocket(rand, this);
        }
    }

    @Override
    protected void explode() {
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new FireworkExplosionPacket(
                FireworksHelper.createRandomExplosion(rand, FireworkRocketItem.Shape.LARGE_BALL),
                getPosX(),
                getPosY(),
                getPosZ(),
                getMotion()
        ));
        world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625), this.getPosZ(), 4, Explosion.Mode.NONE);
    }
}
