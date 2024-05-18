package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.crital.common.api.CritResultHandler;
import sypztep.crital.common.data.CritData;

import static sypztep.crital.common.data.CritData.calculateCritChance;

@Mixin(Item.class)
public class ItemMixin implements CritResultHandler {
    @Unique
    private CritData.CritResult critResult;
    @Inject(method = "onCraft", at = @At("TAIL"))
    public void onCraft(ItemStack stack, World world, CallbackInfo ci) {
        if (!world.isClient() && !stack.isEmpty()) {
            if (stack.getItem() instanceof SwordItem swordItem) {
                ToolMaterial material = swordItem.getMaterial();
                CritData.CritResult result = calculateCritChance(material);
                stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
                    currentNbt.putString(CritData.TIER_FLAG, result.tier().getName());
                    currentNbt.putFloat(CritData.CRITCHANCE_FLAG, result.critChance());
                }));
                crital$setCritResult(result);
            }
        }
    }

    @Override
    public void crital$setCritResult(CritData.CritResult result) {
        this.critResult = result;
    }

    @Override
    public CritData.CritResult crital$getCritResult() {
        return critResult;
    }
}

