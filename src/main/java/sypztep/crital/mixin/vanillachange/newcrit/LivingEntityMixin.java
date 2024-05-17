package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.api.NewCriticalOverhaul;

import java.util.Random;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements NewCriticalOverhaul {
    @Shadow public abstract @Nullable EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract ItemStack getMainHandStack();

    @Unique
    private static final TrackedData<Float> CRIT_RATE;
    @Unique
    private static final TrackedData<Float> CRIT_DMG;
    @Unique
    private final Random critRateRandom = new Random();
    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = {"initDataTracker"},at = {@At("TAIL")})
    protected void initDataTracker(DataTracker.Builder builder ,CallbackInfo ci) {
        builder.add(CRIT_RATE, 0.0F); //Start With 0% that was default vanilla
        builder.add(CRIT_DMG, 50.0F); //Start With 50% that was default vanilla
    }

    @Inject(method = {"writeCustomDataToNbt"},at = {@At("TAIL")})
    private void write(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("CritRate", this.crital$getCritRate());
        nbt.putFloat("CritDamage", this.crital$getCritDamage());
    }

    @Inject(method = {"readCustomDataFromNbt"},at = {@At("TAIL")})
    private void read(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CritRate"))
            this.crital$setCritRate(nbt.getFloat("CritRate"));
        if (nbt.contains("CritDamage"))
            this.crital$setCritDamage(nbt.getFloat("CritDamage"));
    }
    /**
     * Modifies the damage amount before applying it, considering the new crit overhaul configuration (apply for projectile).
     *
     * @param amount The original damage amount.
     * @param source The source of the damage.
     * @return The modified damage amount.
     */
    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float applyDamageFirst(float amount, DamageSource source) {
        if (ModConfig.CONFIG.shouldDoCrit() && !this.getWorld().isClient()) {
            Entity attacker = source.getAttacker();
            if (attacker instanceof NewCriticalOverhaul invoker) {
                Entity projectileSource = source.getSource();
                if (projectileSource instanceof PersistentProjectileEntity) {
                    invoker.newCrit().crital$setCritical(this.crital$isCritical());
                    amount = invoker.calculateCritDamage(amount);
                    return amount;
                }
            }
        }
        return amount;
    }

    public Random crital$getRand() {
        return this.critRateRandom;
    }
    public void crital$setCritRate(float critRate) {
        this.dataTracker.set(CRIT_RATE, critRate);
    }

    public void crital$setCritDamage(float critDamage) {
        this.dataTracker.set(CRIT_DMG, critDamage);
    }
//
    public float crital$getCritRate() {
        return this.dataTracker.get(CRIT_RATE);
    }
    public float crital$getCritDamage() {
        return this.dataTracker.get(CRIT_DMG);
    }

    static {
        CRIT_RATE = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
        CRIT_DMG = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
    }
}
