package sypztep.crital.common.init;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import sypztep.crital.common.CritalMod;

public class ModEntityAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_CRIT_CHANCE = ModEntityAttributes.register("generic.crit_chance",new ClampedEntityAttribute("attribute.generic.crit_chance",0.0D, -100D, 100D).setTracked(true)) ;
    public static final RegistryEntry<EntityAttribute> GENERIC_CRIT_DAMAGE = ModEntityAttributes.register("generic.crit_damage",new ClampedEntityAttribute("attribute.generic.crit_damage",0.0D,-1024D, 1024D).setTracked(true));

    private static RegistryEntry<EntityAttribute> register(String name, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, CritalMod.id(name), attribute);
    }
}
