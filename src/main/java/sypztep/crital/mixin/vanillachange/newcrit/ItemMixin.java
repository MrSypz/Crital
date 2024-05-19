package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
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

import static sypztep.crital.common.data.CritData.calculateCritValues;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "onCraft", at = @At("TAIL"))
    public void onCraft(ItemStack stack, World world, CallbackInfo ci) {
        if (!world.isClient() && !stack.isEmpty()) {
            if (stack.getItem() instanceof SwordItem swordItem) {
                ToolMaterial material = swordItem.getMaterial();
                applyCritData(stack, material, CritData::getToolCritChance,1.5f, 2f);
            } else if (stack.getItem() instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
                applyCritData(stack, material, CritData::getArmorCritChance,1, 1.5f);
            }
        }
    }

    @Unique
    private <T> void applyCritData(ItemStack stack, T material, MaterialCritChanceProvider<T> critChanceProvider, float critChanceMultiplier, float critDamageMultiplier) {
        CritData.CritResult result = calculateCritValues(material, critChanceProvider,critChanceMultiplier,critDamageMultiplier);
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.putString(CritData.TIER_FLAG, result.tier().getName());
            currentNbt.putFloat(CritData.CRITCHANCE_FLAG, result.critChance());
            currentNbt.putFloat(CritData.CRITDAMAGE_FLAG, result.critDamage());
        }));
    }
}

