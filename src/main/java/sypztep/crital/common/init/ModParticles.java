package sypztep.crital.common.init;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.crital.client.particle.CritAttackParticle;
import sypztep.crital.common.CritalMod;

public class ModParticles {
    public static final SimpleParticleType CRIT_ATTACK = FabricParticleTypes.simple(true);
    public static void init() {
        registerParticle(CRIT_ATTACK,"crit_attack");
    }
    private static void registerParticle(SimpleParticleType simpleParticleType,String name) {
        Registry.register(Registries.PARTICLE_TYPE, CritalMod.id(name),simpleParticleType);
    }
    public static void registerFactor() {
        ParticleFactoryRegistry particleRegistry = ParticleFactoryRegistry.getInstance();
        particleRegistry.register(ModParticles.CRIT_ATTACK, CritAttackParticle.Factory::new);
    }
}
