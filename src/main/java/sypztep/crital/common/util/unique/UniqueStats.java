package sypztep.crital.common.util.unique;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;


import java.util.List;

public class UniqueStats {
    private final List<RegistryEntry<EntityAttribute>> attributes;
    private final String id;

    public UniqueStats(List<RegistryEntry<EntityAttribute>> attributes, String id) {
        this.attributes = attributes;
        this.id = id;
    }

    public List<RegistryEntry<EntityAttribute>> getAttributes() {
        return attributes;
    }

    public String getId() {
        return id;
    }

    // New method to modify the totalValue
    public float modifyTotalValue(float totalValue, RegistryEntry<EntityAttribute> attribute) {
        String roundedValue = String.format("%.2f", totalValue);
        return Float.parseFloat(roundedValue); // Default implementation returns the value unchanged
    }

    public void applyLogic(LivingEntity entity, List<NbtCompound> equippedNbt) {
        // Implement logic based on equippedNbt
    }

}
