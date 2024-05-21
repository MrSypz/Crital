package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.crital.common.data.CritData;

import static sypztep.crital.common.util.CritalDataUtil.applyCritData;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!world.isClient() && !stack.isEmpty()) {
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
}


