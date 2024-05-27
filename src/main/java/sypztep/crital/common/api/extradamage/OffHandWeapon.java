package sypztep.crital.common.api.extradamage;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public interface OffHandWeapon extends MeleeWeapon {
    default float getOffhandDamage(ItemStack stack) {
        float defaultValue = (float) MathHelper.floor(this.crital$getWeaponDamage(stack) / 3.5f / 0.5f) * 0.5f;
        return Math.max(defaultValue, 0.5f);
    }
}
