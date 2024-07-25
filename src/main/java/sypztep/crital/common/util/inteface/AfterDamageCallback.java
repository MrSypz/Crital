package sypztep.crital.common.util.inteface;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface AfterDamageCallback {
    /**
     * Call After modify with projection and all thing
     */
    Event<AfterDamageCallback> EVENT = EventFactory.createArrayBacked(AfterDamageCallback.class,
            (listeners) -> (entity, source, amount) -> {
                for (AfterDamageCallback event : listeners) {
                    ActionResult result = event.afterhurtEntity(entity, source, amount);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult afterhurtEntity(LivingEntity entity, DamageSource source, float amount);
}
