package sypztep.crital.common.data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import sypztep.crital.common.init.ModAttributes;
import sypztep.crital.common.util.unique.OmnivampStats;
import sypztep.crital.common.util.unique.UniqueStats;

import java.util.Map;

public class CritalUniqueStats {
    public static Map<String, UniqueStats> attributes = Map.of(
            CritalData.VITALITY, new UniqueStats(EntityAttributes.GENERIC_MAX_HEALTH, "extra.vitality_stats") {
                @Override
                public void applyLogic(LivingEntity entity, float totalValue) {
                    if (entity.getHealth() > entity.getMaxHealth()) {
                        entity.setHealth(entity.getMaxHealth());
                    }
                }
            },
            CritalData.OMNIVAMP, new OmnivampStats(ModAttributes.GENERIC_OMNIVAMP, "extra.omnivamp_stats")
    );
}
