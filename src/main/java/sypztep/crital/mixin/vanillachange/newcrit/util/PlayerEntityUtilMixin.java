package sypztep.crital.mixin.vanillachange.newcrit.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.crital.common.CritalConfig;
import sypztep.crital.common.init.ModConfig;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityUtilMixin extends LivingEntityUtilMixin {
    PlayerEntityUtilMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean docrit(boolean crit) {
        if (ModConfig.CONFIG.critOptional == CritalConfig.CritOptional.NEW_OVERHAUL) {
            boolean iscrit = this.crital$isCritical();
            if (iscrit) {
                this.crital$setCritical(true);
                return true;
            }
            return false;
        }
        if (ModConfig.CONFIG.critOptional == CritalConfig.CritOptional.KEEP_JUMPCRIT) {
            boolean iscrit = this.crital$isCritical();
            if (iscrit)
                if (this.isOnGround())
                    crit = true;
                else if (crit)
                    this.crital$setCritical(true);
            return crit;
        }
        return crit;
    }
}
