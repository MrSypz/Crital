package sypztep.crital.mixin.vanillachange.newcrit.client;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.crital.client.event.CritalTooltipRender;
import sypztep.crital.common.ModConfig;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendAttributeModifiersTooltip(Ljava/util/function/Consumer;Lnet/minecraft/entity/player/PlayerEntity;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void replaceAppendAttributeModifiersTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, List<Text> list, MutableText mutableText, Consumer<Text> consumer) {
        // Call addCritTooltips if stack contains CUSTOM_DATA.
        if (!ModConfig.NewToolTip)
            return;
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            CritalTooltipRender.getTooltip(stack,list);
            cir.setReturnValue(list);
        }
    }
}

