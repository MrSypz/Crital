package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.init.ModConfig;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Unique
    private boolean alreadyCalculated;

    protected PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Unique
    NbtCompound value = new NbtCompound();
    public float crital$getCritRateFromEquipped() {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            updateNBTData();
            MutableFloat critRate = new MutableFloat();
            critRate.add(getCritChance());
            critRate.add(Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_LUCK)).getValue() * 5); //Get From attribute
            return critRate.floatValue();
        }
        return 0;
    }

    public float crital$getCritDamageFromEquipped() {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            updateNBTData();
            MutableFloat critDamage = new MutableFloat();
            critDamage.add(getCritDamage());
            return critDamage.floatValue();
        }
        return 0; // or return a default value
    }
    @Unique
    private void updateNBTData() {
        @Nullable var data = this.getMainHandStack().get(DataComponentTypes.CUSTOM_DATA);
        if (data != null)
            value = data.copyNbt();
    }
    @Unique
    private float getCritChance() {
        return value.getFloat(CritData.CRITCHANCE_FLAG);
    }
    @Unique
    private float getCritDamage() {
        return value.getFloat(CritData.CRITDAMAGE_FLAG);
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    private float storedamage(float original) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            float modifiedDamage = this.calculateCritDamage(original);
            this.alreadyCalculated = original != modifiedDamage;
            return modifiedDamage;
        }
        return original;
    }
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float storevanillacritdmg(float defaultcritdmg) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            float modifiedCritDamage = this.alreadyCalculated ? 1.0F : (this.newCrit().crital$isCritical() ? this.getTotalCritDamage() / 100.0F + 1.0F : defaultcritdmg);
            this.alreadyCalculated = false;
            return modifiedCritDamage;
        }
        return defaultcritdmg;
    }
}
