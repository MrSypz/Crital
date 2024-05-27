package sypztep.crital.mixin.vanillachange.offhandextra;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.crital.common.api.extradamage.OffHandWeapon;
import sypztep.crital.common.init.ModAttributes;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Mutable
    @Shadow @Final private ComponentMap components;

    @Shadow public abstract ItemStack getDefaultStack();

    @Unique
    private boolean appliedOffhand = false;
    @Inject(method="getComponents", at=@At("HEAD"))
    private void placeOffhandAttributes (CallbackInfoReturnable<ComponentMap> cir) {
        if (this instanceof OffHandWeapon offhand && !this.appliedOffhand) {
            this.appliedOffhand = true;
            AttributeModifiersComponent attributes = this.components.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
            attributes = attributes.with(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ModAttributes.OFFHAND_DAMAGE, "Offhand Weapon Modifier", offhand.getOffhandDamage(this.getDefaultStack()), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.OFFHAND);
            this.components = ComponentMap.builder().addAll(this.components).add(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes).build();
        }
    }
}
