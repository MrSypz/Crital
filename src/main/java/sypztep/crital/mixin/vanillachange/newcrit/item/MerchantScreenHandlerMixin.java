package sypztep.crital.mixin.vanillachange.newcrit.item;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.MerchantScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.util.CritalDataUtil;

@Mixin(MerchantScreenHandler.class)
public class MerchantScreenHandlerMixin {

    @ModifyVariable(method = "quickMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/MerchantScreenHandler;insertItem(Lnet/minecraft/item/ItemStack;IIZ)Z", ordinal = 0), ordinal = 1)
    private ItemStack villagerTradeQuickMoveHandle(ItemStack original) {
        if (ModConfig.modifyOnCraftbyPlayer)
            CritalDataUtil.applyCritData(original);
        return original;
    }
}
