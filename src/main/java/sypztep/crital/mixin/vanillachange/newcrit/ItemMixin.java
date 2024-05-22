package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.crital.common.api.MaterialCritChanceProvider;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritResult;

import static sypztep.crital.common.data.CritData.calculateCritValues;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!stack.isEmpty() && !player.getWorld().isClient()) {
            if (stack.getItem() instanceof SwordItem swordItem) {
                ToolMaterial material = swordItem.getMaterial();
                applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof RangedWeaponItem) {
                applyCritData(stack,ToolMaterials.GOLD, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof AxeItem axeItem) {
                ToolMaterial material = axeItem.getMaterial();
                applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof PickaxeItem pickaxeItem) {
                ToolMaterial material = pickaxeItem.getMaterial();
                applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof ShovelItem shovelItem) {
                ToolMaterial material = shovelItem.getMaterial();
                applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof HoeItem hoeItem) {
                ToolMaterial material = hoeItem.getMaterial();
                applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
                applyCritData(stack, material, CritData::getArmorCritChance);
            }
        }
    }
    @Unique
    private  <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider) {
        CritResult result = calculateCritValues(material, critChanceProvider);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(itemnbt -> {
            itemnbt.putFloat(CritData.CRITCHANCE_FLAG, result.critChance());
            itemnbt.putFloat(CritData.CRITDAMAGE_FLAG, result.critDamage());
            itemnbt.putFloat(CritData.CRITCHANCE_QUALITY_FLAG, result.critChanceQuality());
            itemnbt.putFloat(CritData.CRITDAMAGE_QUALITY_FLAG, result.critDamageQuality());
            itemnbt.putString(CritData.TIER_FLAG, result.tier().getName());
        }));
    }
}


