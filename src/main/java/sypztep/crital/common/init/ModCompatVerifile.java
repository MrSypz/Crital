package sypztep.crital.common.init;

import net.fabricmc.loader.api.FabricLoader;

import sypztep.crital.common.CritalMod;


public class ModCompatVerifile {
    public static final String PENOMIORMODID = "penomior";
    public static void init() {
        FabricLoader.getInstance().getModContainer(PENOMIORMODID).ifPresent(modContainer -> {
            String installedVersion = removeSuffix(modContainer.getMetadata().getVersion().getFriendlyString());
            String requiredVersion = "0.3.1";

            if (compareVersions(installedVersion, requiredVersion) < 0) {
                throw new RuntimeException("Penomior version mismatch. Required: " + requiredVersion + ", Installed: " + installedVersion);
            }
        });

        if (!CritalMod.isPenomiorLoaded) {
            CritalMod.LOGGER.info("Penomior is not installed");
        }
    }

    private static String removeSuffix(String version) {
        int hyphenIndex = version.indexOf("-");
        return hyphenIndex != -1 ? version.substring(0, hyphenIndex) : version;
    }

    private static int compareVersions(String installed, String required) {
        String[] installedParts = installed.split("\\.");
        String[] requiredParts = required.split("\\.");

        for (int i = 0; i < Math.max(installedParts.length, requiredParts.length); i++) {
            int installedPart = i < installedParts.length ? Integer.parseInt(installedParts[i]) : 0;
            int requiredPart = i < requiredParts.length ? Integer.parseInt(requiredParts[i]) : 0;

            if (installedPart != requiredPart) {
                return Integer.compare(installedPart, requiredPart);
            }
        }
        return 0;
    }
}
