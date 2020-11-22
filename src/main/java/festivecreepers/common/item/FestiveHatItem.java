package festivecreepers.common.item;

import festivecreepers.FestiveCreepers;
import festivecreepers.client.render.model.FestiveHatModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FestiveHatItem extends ArmorItem {

    private Object armorModel;
    private final String armorTexture = new ResourceLocation(FestiveCreepers.MODID, "textures/entity/festive_hat.png").toString();

    public FestiveHatItem(Properties builder) {
        super(ArmorMaterial.LEATHER, EquipmentSlotType.HEAD, builder);

    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        if (armorModel == null) {
            BipedModel<LivingEntity> armorModel = new BipedModel<>(0);
            armorModel.bipedHead = new FestiveHatModel().hat;
            armorModel.bipedHeadwear = new ModelRenderer(armorModel);
            this.armorModel = armorModel;
        }
        //noinspection unchecked
        return (A) armorModel;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return armorTexture;
    }
}
