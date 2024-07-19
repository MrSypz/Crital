package sypztep.crital.common.init;

import net.fabricmc.loader.api.FabricLoader;

import sypztep.crital.common.CritalMod;


public class ModCompatVerifile {
    public static final String PENOMIORMODID = "penomior";
    public static void init() {
        FabricLoader.getInstance().getModContainer(PENOMIORMODID).ifPresent(modContainer -> {
            String installedVersion = modContainer.getMetadata().getVersion().getFriendlyString();

            if (!installedVersion.equals("0.1.6")) {
                throw new RuntimeException("Penomior version mismatch. Required: " + "0.1.6" + ", Installed: " + installedVersion);
            }
        });
        if (!CritalMod.isPenomiorLoaded) {
            CritalMod.LOGGER.info("Penomior is not installed");
        }
    }
}
