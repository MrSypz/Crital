package sypztep.crital.mixin.vanillachange.newcrit.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.crital.common.util.CritalDataUtil;


@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "onCraftByPlayer", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (!stack.isEmpty() && !player.getWorld().isClient()) {
            if (CritalDataUtil.matchesItemData(stack))
                CritalDataUtil.applyCritData(stack);
        }
    }
    @Inject(method = "onCraft", at = @At("HEAD"))
    public void onCraft(ItemStack stack, World world, CallbackInfo ci) {
        if (!stack.isEmpty() && !world.isClient()) {
            if (CritalDataUtil.matchesItemData(stack))
                CritalDataUtil.applyCritData(stack);
        }
    }
}


