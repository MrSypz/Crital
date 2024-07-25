package sypztep.crital.common.data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import sypztep.crital.common.init.ModAttributes;
import sypztep.crital.common.util.unique.GoliathStats;
import sypztep.crital.common.util.unique.OmnivampStats;
import sypztep.crital.common.util.unique.UniqueStats;

import java.util.List;
import java.util.Map;

public class CritalUniqueStats {
    public static Map<String, UniqueStats> attributes = Map.of(
            CritalData.VITALITY, new UniqueStats(List.of(EntityAttributes.GENERIC_MAX_HEALTH), "extra.vitality_stats") {
                @Override
                public void applyLogic(LivingEntity entity,  List<NbtCompound> equippedNbt) {
                    if (entity.getHealth() > entity.getMaxHealth()) {
                        entity.setHealth(entity.getMaxHealth());
                    }
                }
            },
            CritalData.OMNIVAMP, new OmnivampStats(List.of(ModAttributes.GENERIC_OMNIVAMP), "extra.omnivamp_stats"),
            CritalData.GOLIATH, new GoliathStats(List.of(EntityAttributes.GENERIC_SCALE, EntityAttributes.GENERIC_MAX_HEALTH, EntityAttributes.GENERIC_STEP_HEIGHT, EntityAttributes.GENERIC_JUMP_STRENGTH, EntityAttributes.GENERIC_SAFE_FALL_DISTANCE), "extra.goliath_stats"),
            CritalData.PROFESSION, new UniqueStats(List.of(EntityAttributes.PLAYER_MINING_EFFICIENCY), "extra.profession_stats")
    );
}
