package sypztep.crital.mixin.vanillachange.newcrit.item;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.util.CritalDataUtil;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin {
    @ModifyVariable(method = "quickMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;onQuickTransfer(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V"),ordinal = 1)
    private ItemStack anvilnsmithableQuickMoveHandle(ItemStack value) {
        if (ModConfig.modifyOnCraftbyPlayer)
            CritalDataUtil.applyCritData(value);
        return value;
    }
}
