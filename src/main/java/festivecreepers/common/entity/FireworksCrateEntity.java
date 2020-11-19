package festivecreepers.common.entity;

import festivecreepers.common.FireworksHelper;
import festivecreepers.common.init.EntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FireworksCrateEntity extends TNTEntity {

    private static final DataParameter<CompoundNBT> FIREWORK_NBT = EntityDataManager.createKey(FireworksCrateEntity.class, DataSerializers.COMPOUND_NBT);

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
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FIREWORK_NBT, FireworksHelper.createRandomExplosion(rand, FireworkRocketItem.Shape.LARGE_BALL));
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
        world.setEntityState(this, (byte)17);
        super.explode();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 17 && world.isRemote) {
            CompoundNBT fireworks = dataManager.get(FIREWORK_NBT);
            Vector3d vector3d = getMotion();
            world.makeFireworks(getPosX(), getPosY(), getPosZ(), vector3d.x, vector3d.y, vector3d.z, fireworks);
        }
        super.handleStatusUpdate(id);
    }
}
