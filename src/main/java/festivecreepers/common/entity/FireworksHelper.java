package festivecreepers.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FireworksHelper {

    private static final List<DyeColor> colors = Arrays.asList(DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK, DyeColor.WHITE);

    public static CompoundNBT createRandomExplosion(Random random, FireworkRocketItem.Shape shape) {
        ListNBT explosionList = new ListNBT();
        FireworksHelper.FireworkExplosionBuilder builder = new FireworksHelper.FireworkExplosionBuilder();
        builder.addColor(colors.get(random.nextInt(colors.size())).getFireworkColor());
        if (random.nextBoolean()) {
            builder.addColor(colors.get(random.nextInt(colors.size())).getFireworkColor());
        }
        if (random.nextBoolean()) {
            builder.addFadeColor(colors.get(random.nextInt(colors.size())).getFireworkColor());
            builder.addFadeColor(colors.get(random.nextInt(colors.size())).getFireworkColor());
        }

        if (random.nextBoolean()) {
            builder.addFlicker();
        }
        if (random.nextBoolean()) {
            builder.addTrail();
        }
        builder.setShape(shape);
        explosionList.add(builder.build());

        CompoundNBT result = new CompoundNBT();
        result.put("Explosions", explosionList);
        return result;
    }

    public static void spawnRandomRocket(Random random, Entity shooter) {
        ItemStack rocketStack = new ItemStack(Items.FIREWORK_ROCKET);
        FireworkRocketItem.Shape shape = FireworkRocketItem.Shape.values()[(random.nextInt(FireworkRocketItem.Shape.values().length))];
        CompoundNBT fireworks = createRandomExplosion(random, shape);
        fireworks.putByte("Flight", (byte) (random.nextInt(2) + 1));
        rocketStack.getOrCreateTag().put("Fireworks", fireworks);

        FireworkRocketEntity rocketEntity = new FireworkRocketEntity(shooter.world, rocketStack, shooter, shooter.getPosX(), shooter.getPosYEye() - (double)0.15F, shooter.getPosZ(), true);
        rocketEntity.shoot(0, 1, 0, 0.7F, 20);
        shooter.world.addEntity(rocketEntity);
    }

    public static class FireworkExplosionBuilder {

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
