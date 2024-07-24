package sypztep.crital.common.init;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import sypztep.crital.common.CritalMod;

public class ModAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_OMNIVAMP = register("generic.omnivamp", new ClampedEntityAttribute("attribute.name.generic.omnivamp", 0.0, 0.0, 2.0).setTracked(true));
    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, CritalMod.id(id), attribute);
    }
}
