package sypztep.crital.common.util.unique;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Map;

public class GoliathStats extends UniqueStats {
    public GoliathStats(List<RegistryEntry<EntityAttribute>> attributes, String id) {
        super(attributes, id);
    }

    @Override
    public float modifyTotalValue(float totalValue, RegistryEntry<EntityAttribute> attribute) {
        Map<RegistryEntry<EntityAttribute>, Float> attributeMultipliers = Map.of(
                EntityAttributes.GENERIC_SCALE, 0.02f,  // Multiply by 0.02
                EntityAttributes.GENERIC_MAX_HEALTH, 0.5f,
                EntityAttributes.GENERIC_STEP_HEIGHT, 0.02f,
                EntityAttributes.GENERIC_JUMP_STRENGTH, 0.005f,
                EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 0.1f
        );

        Float multiplier = attributeMultipliers.get(attribute);
        if (multiplier != null)
            return totalValue * multiplier;

        return totalValue; // Default if no specific multiplier
    }


    @Override
    public void applyLogic(LivingEntity entity,  List<NbtCompound> equippedNbt) {
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
