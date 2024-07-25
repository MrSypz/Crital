package sypztep.crital.mixin.vanillachange.newcrit.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.crital.client.event.CritalTooltipRender;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.init.ModDataComponent;
import sypztep.crital.common.util.CritalDataUtil;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Unique
    private final ItemStack stack = (ItemStack) (Object) this;

    @Shadow
    public abstract Text getName();

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract Item getItem();

    @ModifyVariable(
            method = "getTooltip",
            at = @At("STORE"),
            ordinal = 0, index = 5
    )
    private MutableText setNameColor(MutableText mutableText) {
        NbtCompound value = ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL);
        String tier = value.getString(CritalData.TIER);
        MutableText newtext = Text.empty().append(this.getName()).formatted(CritalDataUtil.getTierFormatting(tier));

        if (!this.isEmpty() && !tier.isEmpty()) {
            return newtext;
        } else
            return mutableText;
    }

    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    ordinal = 3),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void replaceAppendEnhancementTooltip(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, List<Text> list, MutableText mutableText, Consumer<Text> consumer) {
        if (!ModConfig.NewToolTip)
            return;
        if (stack.contains(ModDataComponent.CRITAL)) {
            CritalTooltipRender.getTooltip(stack, list, context);
        }
    }

    @WrapOperation(method = "getTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
                    ordinal = 3)
    )
    private void removeEnchantmentTooltip(ItemStack instance, ComponentType<?> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, Operation<Void> original) {
        if (!ModConfig.NewToolTip || !instance.contains(ModDataComponent.CRITAL)) // if config is disable call default
            original.call(instance, componentType, context, textConsumer, type);
    }

    @WrapOperation(method = "getTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendAttributeModifiersTooltip(Ljava/util/function/Consumer;Lnet/minecraft/entity/player/PlayerEntity;)V")
    )
    private void removeAttributeModifiersTooltip(ItemStack instance, Consumer<Text> textConsumer, PlayerEntity player, Operation<Void> original) {
        if (!ModConfig.NewToolTip || !instance.contains(ModDataComponent.CRITAL))
            original.call(instance, textConsumer, player);
    }
}
