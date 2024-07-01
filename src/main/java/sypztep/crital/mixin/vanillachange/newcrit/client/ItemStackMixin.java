package sypztep.crital.mixin.vanillachange.newcrit.client;

import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.crital.client.event.CritalTooltipRender;
import sypztep.crital.common.ModConfig;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isDamaged();

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract int getDamage();

    @Shadow public abstract Item getItem();

    @Shadow @Final private ComponentMapImpl components;

    @Shadow @Final private static Text DISABLED_TEXT;

    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    shift = At.Shift.BY,by = 4),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void replaceAppendEnchantmentTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, List<Text> list, MutableText mutableText, Consumer<Text> consumer) {
        if (!ModConfig.NewToolTip)
            return;
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            CritalTooltipRender.getTooltip(stack, list, context);

            //I'm too lazy to modify mixin to copy paste from getTooltip it self
            if (type.isAdvanced()) {
                if (this.isDamaged())
                    list.add(Text.translatable("item.durability", this.getMaxDamage() - this.getDamage(), this.getMaxDamage()));
                list.add(Text.literal(Registries.ITEM.getId(this.getItem()).toString()).formatted(Formatting.DARK_GRAY));
                int i = this.components.size();
                if (i > 0)
                    list.add(Text.translatable("item.components", i).formatted(Formatting.DARK_GRAY));
            }
            if (player != null && !this.getItem().isEnabled(player.getWorld().getEnabledFeatures()))
                list.add(DISABLED_TEXT);

            cir.setReturnValue(list);
        }
    }
}

