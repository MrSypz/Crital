package sypztep.crital.mixin.vanillachange.newcrit.client;

import net.minecraft.client.gui.hud.InGameHud;
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

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    private ItemStack currentStack;
    //Color for Item In GUI
        @ModifyVariable(
            method = "renderHeldItemTooltip",
            at = @At("STORE"),
            ordinal = 0,index = 2
    )
    private MutableText setNameColor(MutableText mutableText) {
        NbtCompound value = CritalDataUtil.getNbtCompoundFromStack(this.currentStack);
        String tier = value.getString(CritData.TIER_FLAG);
        MutableText newtext = Text.empty().append(this.currentStack.getName()).formatted(CritData.getTierFormatting(tier));

        if (!this.currentStack.isEmpty() && !tier.isEmpty()) {
            return newtext;
        } else
            return mutableText;
    }
}

