package sypztep.crital.common.init;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.statuseffect.CooldownEffect;

public class ModStatusEffect {
    public static RegistryEntry<StatusEffect> OMNIVAMP_COOLDOWN;

    public static void init() {
        OMNIVAMP_COOLDOWN = register("omnivamp_cooldown", new CooldownEffect(StatusEffectCategory.NEUTRAL));
    }

    public static RegistryEntry<StatusEffect> register(String name, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, CritalMod.id(name), effect);
    }
}
