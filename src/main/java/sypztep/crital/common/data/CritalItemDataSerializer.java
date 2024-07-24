package sypztep.crital.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sypztep.crital.common.CritalMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CritalItemDataSerializer {
    private static Map<String, CritalItemData> configCache;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("crital/crital_item_data.json");
    public static CritalItemDataSerializer serializer = new CritalItemDataSerializer();

    public CritalItemDataSerializer() {
    }

    public void loadConfig() {
        if (configCache == null) {
            try {
                createDirectoriesIfNeeded();
                Path configFilePath = CONFIG_FILE_PATH;
                if (Files.exists(configFilePath)) {
                    try (Reader reader = new FileReader(configFilePath.toFile())) {
                        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                        CritalItemDataMap dataMap = gson.fromJson(jsonObject, CritalItemDataMap.class);
                        int itemCount = dataMap.itemDataMap().size(); // Count items for informative message
                        if (itemCount > 0) {
                            CritalMod.LOGGER.info(String.format("Found %d Crital Data items!", itemCount));
                        } else {
                            CritalMod.LOGGER.warn("No Crital Data found. (Recommend delete this file {} )", CONFIG_FILE_PATH);
                        }
                        configCache = dataMap.itemDataMap();
                    }
                } else {
                    CritalMod.LOGGER.warn("Configuration file does not exist. Creating new file with default data.");
                    saveConfig(new CritalItemDataMap(getDefaultData().itemDataMap()));
                    configCache = new HashMap<>(getDefaultData().itemDataMap());
                }
            } catch (IOException e) {
                CritalMod.LOGGER.error("Error loading configuration from file: {}", CONFIG_FILE_PATH, e);
                configCache = new HashMap<>();
            }
        }
    }

    public static Map<String, CritalItemData> getConfigCache() {
        return configCache;
    }

    public static int encodeConfig() {
        return configCache.hashCode();
    }

    public static Path getConfigFilePath() {
        return CONFIG_FILE_PATH;
    }

    // Save the configuration to JSON file
    public void saveConfig(CritalItemDataMap newData) {
        try {
            createDirectoriesIfNeeded();
            try (Writer writer = new FileWriter(CONFIG_FILE_PATH.toFile())) {
                gson.toJson(newData, writer);
            }
        } catch (IOException e) {
            CritalMod.LOGGER.error("Write fail : {}", CONFIG_FILE_PATH, e);
        }
    }

    // Create directories if they don't exist
    private void createDirectoriesIfNeeded() throws IOException {
        if (!Files.exists(CONFIG_FILE_PATH.getParent())) {
            Files.createDirectories(CONFIG_FILE_PATH.getParent());
        }
    }

    @Contract(" -> new")
    private @NotNull CritalItemDataMap getDefaultData() {
        // Initialize the defaultData map
        Map<String, CritalItemData> defaultData = new HashMap<>();
        // Range Weapons
        String[] rangeWeapons = {"bow", "crossbow", "trident", "shield"};
        float[] baseValues = {3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f};

        for (String weapon : rangeWeapons) {
            Item item = switch (weapon) {
                case "bow" -> Items.BOW;
                case "crossbow" -> Items.CROSSBOW;
                case "trident" -> Items.TRIDENT;
                case "shield" -> Items.SHIELD;
                default -> throw new IllegalArgumentException("Unknown weapon: " + weapon);
            };
            defaultData.put("minecraft:" + weapon, new CritalItemData(key(item), baseValues[0], baseValues[1], baseValues[2], baseValues[3], baseValues[4], baseValues[5]));
        }

        String[] armorTypes = {"helmet", "chestplate", "leggings", "boots"};
        float[] leatherValues = {1.75f, 2.25f}; // Example values for leather
        float[] ironValues = {3.0f, 3.5f}; // Example values for iron
        float[] goldValues = {2.0f, 2.5f}; // Example values for gold
        float[] chainmailValues = {2.5f, 3.0f}; // Example values for chainmail
        float[] diamondValues = {4.0f, 4.5f}; // Example values for diamond
        float[] netheriteValues = {7.0f, 7.5f}; // Example values for netherite

        String[] materials = {"wooden", "stone", "iron", "golden", "diamond", "netherite"};

        // Maces
        defaultData.put("minecraft:mace", new CritalItemData(key(Items.MACE), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Special helmet
        defaultData.put("minecraft:turtle_helmet", new CritalItemData(key(Items.TURTLE_HELMET), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Swords
        float[] swordValues = {3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f}; // Example base values for swords
        for (int i = 0; i < materials.length; i++) {
            String material = materials[i];
            defaultData.put("minecraft:" + material + "_sword", new CritalItemData("minecraft:" + material + "_sword", swordValues[i], swordValues[i] + 0.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Shovels
        for (int i = 0; i < materials.length; i++) {
            String material = materials[i];
            float attack = swordValues[i] - 1.0f; // Shovels value is 1 less than swords
            defaultData.put("minecraft:" + material + "_shovel", new CritalItemData("minecraft:" + material + "_shovel", attack, attack + 0.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Hoes
        for (int i = 0; i < materials.length; i++) {
            String material = materials[i];
            float attack = swordValues[i] - 1.0f; // Hoes value is 1 less than swords
            defaultData.put("minecraft:" + material + "_hoe", new CritalItemData("minecraft:" + material + "_hoe", attack, attack + 0.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Axes
        for (int i = 0; i < materials.length; i++) {
            String material = materials[i];
            float attack = swordValues[i] - 0.5f; // Axes value is 0.5 less than swords
            defaultData.put("minecraft:" + material + "_axe", new CritalItemData("minecraft:" + material + "_axe", attack, attack + 0.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Pickaxes
        for (int i = 0; i < materials.length; i++) {
            String material = materials[i];
            float attack = swordValues[i] - 0.5f; // Pickaxes value is 0.5 less than swords
            defaultData.put("minecraft:" + material + "_pickaxe", new CritalItemData("minecraft:" + material + "_pickaxe", attack, attack + 0.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }
        // Armors
        // Leather
        for (String type : armorTypes) {
            defaultData.put("minecraft:leather_" + type, new CritalItemData("minecraft:leather_" + type, leatherValues[0], leatherValues[1], 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Iron
        for (String type : armorTypes) {
            defaultData.put("minecraft:iron_" + type, new CritalItemData("minecraft:iron_" + type, ironValues[0], ironValues[1], 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Golden
        for (String type : armorTypes) {
            defaultData.put("minecraft:golden_" + type, new CritalItemData("minecraft:golden_" + type, goldValues[0], goldValues[1], 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Chainmail
        for (String type : armorTypes) {
            defaultData.put("minecraft:chainmail_" + type, new CritalItemData("minecraft:chainmail_" + type, chainmailValues[0], chainmailValues[1], 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Diamond
        for (String type : armorTypes) {
            defaultData.put("minecraft:diamond_" + type, new CritalItemData("minecraft:diamond_" + type, diamondValues[0], diamondValues[1], 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Netherite
        for (String type : armorTypes) {
            defaultData.put("minecraft:netherite_" + type, new CritalItemData("minecraft:netherite_" + type, netheriteValues[0], netheriteValues[1], 0.2f, 1.25f, 0.7f, 1.5f));
        }
        return new CritalItemDataMap(defaultData);
    }

    public record CritalItemDataMap(Map<String, CritalItemData> itemDataMap) {
    }

    public static String key(Item items) {
        return Registries.ITEM.getId(items).toString();
    }
}
