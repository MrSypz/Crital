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
import sypztep.crital.common.util.CritalDataUtil;


@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!stack.isEmpty() && !player.getWorld().isClient()) {
            if (stack.getItem() instanceof ToolItem toolItem) {
                ToolMaterial material = toolItem.getMaterial();
                CritalDataUtil.applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof RangedWeaponItem) {
                CritalDataUtil.applyCritData(stack,ToolMaterials.GOLD, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
                CritalDataUtil.applyCritData(stack, material, CritData::getArmorCritChance);
            }
        }
    }
}


