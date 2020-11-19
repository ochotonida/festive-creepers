package festivecreepers.common.entity;

import festivecreepers.common.FireworksHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class FestiveCreeperEntity extends CreeperEntity {

    private static final DataParameter<CompoundNBT> FIREWORK_NBT = EntityDataManager.createKey(FestiveCreeperEntity.class, DataSerializers.COMPOUND_NBT);

    public FestiveCreeperEntity(EntityType<? extends FestiveCreeperEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FIREWORK_NBT, FireworksHelper.createRandomExplosion(getRNG(), getRNG().nextInt(5) == 0 ? FireworkRocketItem.Shape.CREEPER : FireworkRocketItem.Shape.LARGE_BALL));
    }

    @Override
    protected void explode() {
        if (!world.isRemote()) {
            Explosion.Mode mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            float explosionMultiplier = this.isCharged() ? 2 : 1;
            dead = true;
            Explosion explosion = new Explosion(world, this, null, null, getPosX(), getPosY(), getPosZ(), explosionRadius * explosionMultiplier, false, mode);
            if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) {
                explosion.doExplosionA();
                explosion.doExplosionB(false);
            }
            world.setEntityState(this, (byte)17);
            remove();
            spawnLingeringCloud();
        }
    }

    @Override
    protected @Nonnull ResourceLocation getLootTable() {
        return super.getLootTable();
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
