package sypztep.crital.common.util.unique;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;

public class OmnivampStats extends UniqueStats {
    public OmnivampStats(RegistryEntry<EntityAttribute> attribute, String id) {
        super(attribute, id);
    }

    @Override
    public float modifyTotalValue(float totalValue) {
        return totalValue * 0.01f;
    }
}
