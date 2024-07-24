package sypztep.crital.common.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import sypztep.crital.common.init.ModAttributes;
import sypztep.crital.common.util.inteface.AfterDamageCallback;

@Environment(EnvType.SERVER)
public class AfterDamageEvent implements AfterDamageCallback {
    @Override
    public ActionResult afterhurtEntity(LivingEntity entity, DamageSource source, float amount) {
        if (entity.getEntityWorld().isClient) {
            return ActionResult.PASS;
        }
        if (source != null) {
            if (source.getAttacker() instanceof PlayerEntity attacker) {
                float omni = (float) attacker.getAttributeValue(ModAttributes.GENERIC_OMNIVAMP);
                float applyAmount = omni * amount;
                attacker.heal(applyAmount);
            }
        }
        return ActionResult.PASS;
    }
}
