package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.util.CritalDataUtil;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Text getName();

    @Shadow public abstract ComponentMap getComponents();

    @Shadow public abstract boolean isEmpty();

    @Shadow public abstract ItemStack copy();

    @ModifyVariable(
            method = "getTooltip",
            at = @At("STORE"),
            ordinal = 0,index = 5
    )
    private MutableText setNameColor(MutableText mutableText) {
        NbtCompound value = CritalDataUtil.getNbtCompoundFromStack(this.copy());
        String tier = value.getString(CritData.TIER_FLAG);
        MutableText newtext = Text.empty().append(this.getName()).formatted(CritData.getTierFormatting(tier));

        if (!this.isEmpty() && !tier.isEmpty()) {
            return newtext;
        } else
            return mutableText;
    }
}
