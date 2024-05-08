package sypztep.crital.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CritOverhaulConfig {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("newcritoverhaul/crit_chances.json").toString();
    private final CritOverhaulData critOverhaulData;
    private static final CritOverhaulData CLIENT_DEFAULT_DATA = new CritOverhaulData();
    public static String getConfigFilePath() {
        return CONFIG_FILE_PATH;
    }
    public CritOverhaulConfig() {
        this.critOverhaulData = loadConfig();
    }
    public CritOverhaulData loadConfig() {
        try {
            Path configFilePath = Paths.get(CONFIG_FILE_PATH);
            if (Files.exists(configFilePath)) {
                try (Reader reader = new FileReader(configFilePath.toFile())) {
                    return gson.fromJson(reader, CritOverhaulData.class);
                }
            } else {
                saveConfig(CLIENT_DEFAULT_DATA);
                return CLIENT_DEFAULT_DATA;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new CritOverhaulData();
        }
    }

    private void saveConfig(CritOverhaulData newData) {
        try {
            Path configFilePath = Paths.get(CONFIG_FILE_PATH);
            Files.createDirectories(configFilePath.getParent());

            if (!Files.exists(configFilePath) || Files.size(configFilePath) == 0) {
                Files.createFile(configFilePath);
                try (Writer writer = new FileWriter(configFilePath.toFile())) {
                    gson.toJson(newData, writer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CritOverhaulEntry getCritDataForItem(String itemName) {
        return critOverhaulData.getItems().getOrDefault(itemName, new CritOverhaulEntry(0.0f, 0.0f));
    }

    /**
     * Adds or updates crit chance and crit damage values for a specific item.
     *
     * @param IDItemName The identifier of the item (e.g., "minecraft:iron_sword").
     * @param critChance The crit chance value to be set for the item.
     * @param critDamage The crit damage value to be set for the item.
     */
    public void addItem(String IDItemName, float critChance, float critDamage) {
        critOverhaulData.getItems().put(IDItemName, new CritOverhaulEntry(critChance, critDamage));
        saveConfig(critOverhaulData);
    }
    /**
     * Adds or updates crit chance and crit damage values for a list of items.
     *
     * @param itemValuesList List of ItemCritValues containing IDItemName, critChance, and critDamage.
     */
    public void addItems(List<CritOverhaulEntry> itemValuesList) {
        for (CritOverhaulEntry itemValues : itemValuesList) {
            critOverhaulData.getItems().put(itemValues.getId(), itemValues);
        }
        saveConfig(critOverhaulData);
    }
}
