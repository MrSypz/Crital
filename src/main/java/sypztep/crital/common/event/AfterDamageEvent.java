package sypztep.crital.common.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import sypztep.crital.common.init.ModAttributes;
import sypztep.crital.common.init.ModStatusEffect;
import sypztep.crital.common.util.CritalDataUtil;
import sypztep.crital.common.util.inteface.AfterDamageCallback;

public class AfterDamageEvent implements AfterDamageCallback {
    @Override
    public ActionResult afterhurtEntity(LivingEntity entity, DamageSource source, float amount) {
        if (entity.getEntityWorld().isClient) {
            return ActionResult.PASS;
        }
        if (source != null) {
            if (source.getAttacker() instanceof PlayerEntity attacker && CritalDataUtil.getGoliath(attacker.getMainHandStack()) > 0) {
                float omni = (float) attacker.getAttributeValue(ModAttributes.GENERIC_OMNIVAMP);
                float applyAmount = omni * amount;
                if (attacker.hasStatusEffect(ModStatusEffect.OMNIVAMP_COOLDOWN))
                    applyAmount *= 0.75f; // 75% effective
                attacker.heal(applyAmount);
                if (!attacker.hasStatusEffect(ModStatusEffect.OMNIVAMP_COOLDOWN)) //apply when the attacker not have effect
                    attacker.addStatusEffect(new StatusEffectInstance(ModStatusEffect.OMNIVAMP_COOLDOWN, 300, 0));
            }
        }
        return ActionResult.PASS;
    }
}
