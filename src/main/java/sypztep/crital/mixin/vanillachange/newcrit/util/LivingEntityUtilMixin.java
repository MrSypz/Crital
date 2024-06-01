package sypztep.crital.mixin.vanillachange.newcrit.util;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.crital.client.payload.CritSyncPayload;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;
import sypztep.crital.common.init.ModConfig;

@Mixin(value = LivingEntity.class , priority =  998)
public abstract class LivingEntityUtilMixin extends Entity implements NewCriticalOverhaul {
    @Unique
    private boolean crit;

    LivingEntityUtilMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void damageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul &&
                    source.getSource() instanceof PersistentProjectileEntity projectile)
                newCriticalOverhaul.setCritical(projectile.isCritical());
        }
    }
    @Inject(method = "damage", at = @At("RETURN"))
    private void handleCrit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul)
                newCriticalOverhaul.setCritical(false);
        }
    }

    @Override
    public void setCritical(boolean setCrit) {
        if (!ModConfig.CONFIG.shouldDoCrit() || this.getWorld().isClient()) {
            return;
        }
        this.crit = setCrit;
        PlayerLookup.tracking(this).forEach(foundPlayer -> CritSyncPayload.send(foundPlayer, this.getId(), this.crit));
    }

    @Override
    public boolean isCritical() {
        return this.crit;
    }
}
