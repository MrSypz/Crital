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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;
import sypztep.crital.common.init.ModConfig;

import java.util.Random;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements NewCriticalOverhaul {
    @Shadow
    public abstract @Nullable EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Unique
    private static final TrackedData<Float> CRIT_RATE;
    @Unique
    private static final TrackedData<Float> CRIT_DMG;
    @Unique
    private final Random critRateRandom = new Random();
    @Unique
    private boolean mobisCrit = false;
    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(method = {"initDataTracker"}, at = {@At("TAIL")})
    protected void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(CRIT_RATE, 0.0F); //Start With 0% that was default vanilla
        builder.add(CRIT_DMG, 50.0F); //Start With 50% that was default vanilla
    }

    @Inject(method = {"writeCustomDataToNbt"}, at = {@At("TAIL")})
    private void write(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("CritRate", this.getCritRate());
        nbt.putFloat("CritDamage", this.getCritDamage());
    }

    @Inject(method = {"readCustomDataFromNbt"}, at = {@At("TAIL")})
    private void read(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CritRate")) this.setCritRate(nbt.getFloat("CritRate"));
        if (nbt.contains("CritDamage")) this.setCritDamage(nbt.getFloat("CritDamage"));
    }

    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float applyDamageFirst(float amount, DamageSource source) {
        if (ModConfig.CONFIG.shouldDoCrit() && !this.getWorld().isClient()) {
            Entity attacker;
            attacker = source.getAttacker();

            if (attacker instanceof NewCriticalOverhaul invoker) {
                Entity projectileSource = source.getSource();
                if (projectileSource instanceof PersistentProjectileEntity) {
                    invoker.storeCrit().setCritical(this.isCritical());
                    return invoker.calculateCritDamage(amount);
                }
            }
            //for monster part
            if (!(source.getAttacker() instanceof PlayerEntity)) {
                attacker = source.getAttacker();
                if (attacker instanceof NewCriticalOverhaul invoker) {
                    float critDamage = invoker.calculateCritDamage(amount);
                    this.mobisCrit = amount - critDamage != 0;
                    amount = critDamage;
                }
            }
        }
        return amount;
    }
    @Inject(method = "applyDamage", at = @At("TAIL"))
    private void addmonsterCritParticle(DamageSource source, float amount, CallbackInfo ci) {
        if (ModConfig.CONFIG.shouldDoCrit() && !this.getWorld().isClient()) {
            Entity attacker = source.getAttacker();
            if (!(source.getAttacker() instanceof PlayerEntity) && attacker != null) {
                if (attacker instanceof NewCriticalOverhaul && this.mobisCrit) {
                    ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.ELECTRIC_SPARK, attacker.getX(), attacker.getBodyY(0.5), attacker.getZ(), 32, 0.3, 0.0, 0.3, 0.2);
                    this.getWorld().playSound(attacker, attacker.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.HOSTILE, 1, 1);
                }
            }
        }
    }

    @Override
    public Random getRand() {
        return this.critRateRandom;
    }

    @Override
    public void setCritRate(float critRate) {
        this.dataTracker.set(CRIT_RATE, critRate);
    }

    @Override
    public void setCritDamage(float critDamage) {
        this.dataTracker.set(CRIT_DMG, critDamage);
    }

    @Override
    public float getCritRate() {
        return this.dataTracker.get(CRIT_RATE);
    }

    @Override
    public float getCritDamage() {
        return this.dataTracker.get(CRIT_DMG);
    }
    static {
        CRIT_RATE = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
        CRIT_DMG = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
    }
}
