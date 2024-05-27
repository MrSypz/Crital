package sypztep.crital.mixin.vanillachange.offhandextra;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import sypztep.crital.common.api.extradamage.OffHandWeapon;
import sypztep.crital.common.init.ModConfig;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends ToolItem implements OffHandWeapon {
    protected SwordItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public float crital$getWeaponDamage(ItemStack itemStack) {
        if (ModConfig.CONFIG.offhandExtraStats) {
            AttributeModifiersComponent attributes = itemStack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
            double bonusDamage = 0f;
            for (AttributeModifiersComponent.Entry entry : attributes.modifiers()) {
                if (entry.attribute() == EntityAttributes.GENERIC_ATTACK_DAMAGE && entry.modifier().operation() == EntityAttributeModifier.Operation.ADD_VALUE) {
                    bonusDamage += entry.modifier().value();
                }
            }
            return (float) bonusDamage;
        }
        return 0;
    }
}
