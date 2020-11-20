package festivecreepers.common.entity;

import festivecreepers.common.network.FireworkExplosionPacket;
import festivecreepers.common.network.NetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;

public class FestiveCreeperEntity extends CreeperEntity {

    public FestiveCreeperEntity(EntityType<? extends FestiveCreeperEntity> type, World world) {
        super(type, world);
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
            NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new FireworkExplosionPacket(
                    FireworksHelper.createRandomExplosion(getRNG(), getRNG().nextInt(5) == 0 ? FireworkRocketItem.Shape.CREEPER : FireworkRocketItem.Shape.LARGE_BALL),
                    getPosX(),
                    getPosY(),
                    getPosZ(),
                    getMotion()
            ));
            remove();
            spawnLingeringCloud();
        }
    }

    @Override
    protected @Nonnull ResourceLocation getLootTable() {
        return super.getLootTable();
    }
}
