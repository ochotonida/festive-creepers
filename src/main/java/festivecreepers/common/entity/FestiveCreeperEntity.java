package festivecreepers.common.entity;

import festivecreepers.common.FireworksHelper;
import festivecreepers.common.network.FireworkExplosionPacket;
import festivecreepers.common.network.NetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
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
            float explosionMultiplier = isCharged() ? 2 : 1;
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

            if (isCharged()) {
                double direction = rand.nextDouble() * 2 * Math.PI;
                for (int i = 0; i < 3; i++) {
                    FireworksCrateEntity crate = new FireworksCrateEntity(world, getPosX(), getPosY(), getPosZ(), this);
                    crate.setMotion(0.5 * Math.cos(direction + i * Math.PI * 2 / 3), 0.5, 0.5 * Math.sin(direction + i * Math.PI * 2 / 3));
                    crate.setFuse(100 + rand.nextInt(20));
                    world.addEntity(crate);
                }
            }

            remove();
            spawnLingeringCloud();
        }
    }

    @Override
    public void ignite() {
        if (!world.isRemote() && !hasIgnited()) {
            playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 3, 1);
        }
        super.ignite();

    }

    @Override
    public void tick() {
        if (isAlive() && hasIgnited()) {
            setMotion(getMotion().getX(), 1, getMotion().getZ());
            if (world.isRemote) {
                world.addParticle(ParticleTypes.FIREWORK, getPosX(), getPosY() + 0.5, getPosZ(), rand.nextGaussian() * 0.05, -getMotion().y * 0.5, rand.nextGaussian() * 0.05);
            }
        }

        super.tick();
    }

    @Override
    protected @Nonnull
    ResourceLocation getLootTable() {
        return super.getLootTable();
    }
}
