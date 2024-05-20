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
                applyCritData(stack, material, CritData::getToolCritChance, 1.5f, 2f);
            } else if (stack.getItem() instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
                applyCritData(stack, material, CritData::getArmorCritChance, 1, 1.5f);
            }
        }
    }
}


