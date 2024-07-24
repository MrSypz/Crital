package sypztep.crital.mixin.vanillachange.newcrit.client;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.init.ModDataComponent;
import sypztep.crital.common.util.CritalDataUtil;
import sypztep.tyrannus.common.util.ItemStackHelper;

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
        NbtCompound value = ItemStackHelper.getNbtCompound(currentStack, ModDataComponent.CRITAL);
        String tier = value.getString(CritalData.TIER_FLAG);
        MutableText newtext = Text.empty().append(this.currentStack.getName()).formatted(CritalDataUtil.getTierFormatting(tier));

        if (!this.currentStack.isEmpty() && !tier.isEmpty()) {
            return newtext;
        } else
            return mutableText;
    }
}

