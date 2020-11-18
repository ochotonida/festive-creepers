package festivecreepers.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FestiveCreeperEntity extends CreeperEntity {

    private static final DataParameter<CompoundNBT> FIREWORK_NBT = EntityDataManager.createKey(FestiveCreeperEntity.class, DataSerializers.COMPOUND_NBT);

    public FestiveCreeperEntity(EntityType<? extends FestiveCreeperEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FIREWORK_NBT, createFireworksNBT(getRNG()));
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

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 17 && world.isRemote) {
            CompoundNBT fireworks = dataManager.get(FIREWORK_NBT);
            Vector3d vector3d = getMotion();
            world.makeFireworks(getPosX(), getPosY(), getPosZ(), vector3d.x, vector3d.y, vector3d.z, fireworks);
        }
        super.handleStatusUpdate(id);
    }

    private static CompoundNBT createFireworksNBT(Random random) {
        ListNBT explosionList = new ListNBT();
        FireworkExplosionBuilder builder = new FireworkExplosionBuilder();
        builder.addColor(DyeColor.values()[random.nextInt(DyeColor.values().length)].getFireworkColor());
        if (random.nextBoolean()) {
            builder.addColor(DyeColor.values()[random.nextInt(DyeColor.values().length)].getFireworkColor());
        }
        if (random.nextBoolean()) {
            builder.addFadeColor(DyeColor.values()[random.nextInt(DyeColor.values().length)].getFireworkColor());
            builder.addFadeColor(DyeColor.values()[random.nextInt(DyeColor.values().length)].getFireworkColor());
        }

        if (random.nextBoolean()) {
            builder.addFlicker();
        }
        if (random.nextBoolean()) {
            builder.addTrail();
        }
        builder.setShape(random.nextInt(5) == 0 ? FireworkRocketItem.Shape.CREEPER : FireworkRocketItem.Shape.LARGE_BALL);
        explosionList.add(builder.build());

        CompoundNBT result = new CompoundNBT();
        result.put("Explosions", explosionList);
        return result;
    }

    private static class FireworkExplosionBuilder {

        private final CompoundNBT explosionNBT = new CompoundNBT();
        private final List<Integer> colors = new ArrayList<>();
        private final List<Integer> fadeColors = new ArrayList<>();

        public CompoundNBT build() {
            CompoundNBT result = explosionNBT.copy();
            result.putIntArray("Colors", colors);
            if (!fadeColors.isEmpty()) {
                result.putIntArray("FadeColors", fadeColors);
            }
            return result;
        }

        public void setShape(FireworkRocketItem.Shape shape) {
            explosionNBT.putByte("Type", (byte) shape.getIndex());
        }

        public void addFlicker() {
            explosionNBT.putBoolean("Flicker", true);
        }

        public void addTrail() {
            explosionNBT.putBoolean("Trail", true);
        }

        public void addColor(int color) {
            colors.add(color);
        }

        public void addFadeColor(int color) {
            fadeColors.add(color);
        }
    }
}
