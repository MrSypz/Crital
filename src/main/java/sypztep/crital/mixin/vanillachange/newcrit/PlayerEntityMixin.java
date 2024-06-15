package sypztep.crital.mixin.vanillachange.newcrit;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.data.CritData;

import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow public abstract float getMovementSpeed();

    @Unique
    private boolean alreadyCalculated;

    protected PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Override
    public float crital$getCritRateFromEquipped() {
        if (ModConfig.shouldDoCrit()) {
            MutableFloat critRate = new MutableFloat();
            List<NbtCompound> equippedNbt = getNbtFromEquippedSlots();
            for (NbtCompound nbt : equippedNbt)
                critRate.add(nbt.getFloat(CritData.CRITCHANCE_FLAG));
            critRate.add(Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_LUCK)).getValue() * 5);
            return Math.min(critRate.floatValue(), 100);
        }
        return 0;
    }
    @Override
    public float crital$getCritDamageFromEquipped() {
        if (ModConfig.shouldDoCrit()) {
            MutableFloat critDamage = new MutableFloat();
            List<NbtCompound> equippedNbt = getNbtFromEquippedSlots();
            for (NbtCompound nbt : equippedNbt)
                critDamage.add(nbt.getFloat(CritData.CRITDAMAGE_FLAG));
            return critDamage.floatValue();
        }
        return 0;
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    private float storedamage(float original) {
        if (ModConfig.shouldDoCrit()) {
            float modifiedDamage = this.calculateCritDamage(original);
            this.alreadyCalculated = original != modifiedDamage;
            return modifiedDamage;
        }
        return original;
    }
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float storevanillacritdmg(float defaultcritdmg) {
        if (ModConfig.shouldDoCrit()) {
            float modifiedCritDamage = this.alreadyCalculated ? 1.0F : (this.storeCrit().crital$isCritical() ? this.getTotalCritDamage() / 100.0F + 1.0F : defaultcritdmg);
            this.alreadyCalculated = false;
            return modifiedCritDamage;
        }
        return defaultcritdmg;
    }
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 3)
    private boolean canSweepAttack(boolean bl4, @Local(ordinal = 0) boolean bl, @Local(ordinal = 1) boolean bl2, @Local(ordinal = 2) boolean bl3) {
        double d =this.horizontalSpeed - this.prevHorizontalSpeed;
        if (ModConfig.sweepCrit && bl3) {
            return bl && !bl2 && this.isOnGround() && d < this.getMovementSpeed() && this.getStackInHand(Hand.MAIN_HAND).getItem() instanceof SwordItem;  // if sweepCrit is true, use bl3
        } else {
            return bl && !bl3 && !bl2 && this.isOnGround() && d < this.getMovementSpeed() && this.getStackInHand(Hand.MAIN_HAND).getItem() instanceof SwordItem;
        }
    }
}
