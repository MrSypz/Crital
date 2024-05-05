package sypztep.crital.mixin.vanillachange.newcrit.util;

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
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.util.NewCriticalOverhaul;

@Mixin(LivingEntity.class)
public abstract class LivingEntityUtilMixin extends Entity implements NewCriticalOverhaul {
    @Unique
    private boolean crit;

    LivingEntityUtilMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Injects into the "damage" method at the HEAD to handle critical hit logic before damage is applied.
     * This method sets the critical hit status of the attacking entity based on the source.
     *
     * @param source The source of the damage.
     * @param amount The amount of damage to be dealt.
     * @param cir    The callback info.
     */
    @Inject(method = "damage", at = @At("HEAD"))
    private void damageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul &&
                    source.getSource() instanceof PersistentProjectileEntity)
                newCriticalOverhaul.crital$setCritical(this.crital$isCritical());
        }
    }

    /**
     * Injects into the "damage" method at the RETURN to handle critical hit logic after damage is applied.
     * This method resets the critical hit status of the attacking entity to false.
     *
     * @param source The source of the damage.
     * @param amount The amount of damage dealt.
     * @param cir    The callback info.
     */
    @Inject(method = "damage", at = @At("RETURN"))
    private void handleCrit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul)
                newCriticalOverhaul.crital$setCritical(false);
        }
    }

//    @Override
//    public void setCritical(boolean setCrit) {
//        if (ModConfig.CONFIG.shouldDoCrit()) {
//            this.crit = setCrit;
//            if (!this.getWorld().isClient) {
//                PacketByteBuf byteBuf = new SyncCritPacket(this.getId(), this.crit).write(PacketByteBufs.create());
//                this.getWorld().getServer().getPlayerManager().sendToAll(new CustomPayloadS2CPacket(SyncCritS2CPacket.ID, byteBuf));
//            }
//        }
//    }
    //TODO: ADD PACKET for server sync 1.20.5 got me recode :(
    @Override
    public void crital$setCritical(boolean setCrit) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            this.crit = setCrit;
        }
    }

    @Override
    public boolean crital$isCritical() {
        return this.crit;
    }
}
