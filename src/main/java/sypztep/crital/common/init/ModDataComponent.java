package sypztep.crital.common.init;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.crital.common.CritalMod;

public class ModDataComponent {
    public static final ComponentType<NbtComponent> CRITAL = new ComponentType.Builder<NbtComponent>().codec(NbtComponent.CODEC).build();

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, CritalMod.id("crital"), CRITAL);
    }
}
