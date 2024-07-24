package sypztep.crital.common.util.unique;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;


public class UniqueStats {
    private final RegistryEntry<EntityAttribute> attribute;
    private final String id;

    public UniqueStats(RegistryEntry<EntityAttribute> attribute, String id) {
        this.attribute = attribute;
        this.id = id;
    }

    public RegistryEntry<EntityAttribute> getAttribute() {
        return attribute;
    }

    public String getId() {
        return id;
    }

    // New method to modify the totalValue
    public float modifyTotalValue(float totalValue) {
        return totalValue; // Default implementation returns the value unchanged
    }

    public void applyLogic(LivingEntity entity, float totalValue) {

    }
}