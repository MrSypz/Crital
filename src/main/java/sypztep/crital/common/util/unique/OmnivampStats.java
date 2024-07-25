package sypztep.crital.common.util.unique;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;

public class OmnivampStats extends UniqueStats {

    public OmnivampStats(List<RegistryEntry<EntityAttribute>> attributes, String id) {
        super(attributes, id);
    }

    @Override
    public float modifyTotalValue(float totalValue, RegistryEntry<EntityAttribute> attribute) {
        return totalValue * 0.01f;
    }
}
