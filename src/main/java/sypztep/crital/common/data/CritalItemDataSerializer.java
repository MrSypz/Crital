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
        defaultData.put("minecraft:bow", new CritalItemData(key(Items.BOW), 3.0f, 3.5f, 0.7f, 2.25f, 1.7f, 1.5f));
        defaultData.put("minecraft:crossbow", new CritalItemData(key(Items.CROSSBOW), 3.0f, 3.5f, 0.7f, 2.25f, 1.7f, 1.5f));
        defaultData.put("minecraft:trident", new CritalItemData(key(Items.TRIDENT), 3.0f, 3.5f, 1.2f, 2.75f, 1.8f, 2.55f));
        defaultData.put("minecraft:shield", new CritalItemData(key(Items.SHIELD), 3.0f, 3.5f, 0.5f, 0.75f, 1.7f, 3.75f));

        // Mace (assuming it is a custom item)
        defaultData.put("minecraft:mace", new CritalItemData(key(Items.MACE), 3.0f, 3.5f, 0.2f, 1.25f, 2.0f, 3.55f));

        // Special Helmet
        defaultData.put("minecraft:turtle_helmet", new CritalItemData(key(Items.TURTLE_HELMET), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Melee Weapons (you might want to include all melee weapons similarly if they exist)

        // Shovels
        String[] materials = {"wooden", "stone", "iron", "golden", "diamond", "netherite"};
        for (String material : materials) {
            Item item = switch (material) {
                case "wooden" -> Items.WOODEN_SHOVEL;
                case "stone" -> Items.STONE_SHOVEL;
                case "iron" -> Items.IRON_SHOVEL;
                case "golden" -> Items.GOLDEN_SHOVEL;
                case "diamond" -> Items.DIAMOND_SHOVEL;
                case "netherite" -> Items.NETHERITE_SHOVEL;
                default -> throw new IllegalArgumentException("Unknown material: " + material);
            };
            defaultData.put("minecraft:" + material + "_shovel", new CritalItemData(key(item), 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Hoes
        for (String material : materials) {
            Item item = switch (material) {
                case "wooden" -> Items.WOODEN_HOE;
                case "stone" -> Items.STONE_HOE;
                case "iron" -> Items.IRON_HOE;
                case "golden" -> Items.GOLDEN_HOE;
                case "diamond" -> Items.DIAMOND_HOE;
                case "netherite" -> Items.NETHERITE_HOE;
                default -> throw new IllegalArgumentException("Unknown material: " + material);
            };
            defaultData.put("minecraft:" + material + "_hoe", new CritalItemData(key(item), 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Axes
        for (String material : materials) {
            Item item = switch (material) {
                case "wooden" -> Items.WOODEN_AXE;
                case "stone" -> Items.STONE_AXE;
                case "iron" -> Items.IRON_AXE;
                case "golden" -> Items.GOLDEN_AXE;
                case "diamond" -> Items.DIAMOND_AXE;
                case "netherite" -> Items.NETHERITE_AXE;
                default -> throw new IllegalArgumentException("Unknown material: " + material);
            };
            defaultData.put("minecraft:" + material + "_axe", new CritalItemData(key(item), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Pickaxes
        for (String material : materials) {
            Item item = switch (material) {
                case "wooden" -> Items.WOODEN_PICKAXE;
                case "stone" -> Items.STONE_PICKAXE;
                case "iron" -> Items.IRON_PICKAXE;
                case "golden" -> Items.GOLDEN_PICKAXE;
                case "diamond" -> Items.DIAMOND_PICKAXE;
                case "netherite" -> Items.NETHERITE_PICKAXE;
                default -> throw new IllegalArgumentException("Unknown material: " + material);
            };
            defaultData.put("minecraft:" + material + "_pickaxe", new CritalItemData(key(item), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        // Armors
        String[] armorTypes = {"helmet", "chestplate", "leggings", "boots"};
        for (String type : armorTypes) {
            // Leather
            defaultData.put("minecraft:leather_" + type, new CritalItemData(key(Items.LEATHER_HELMET), 1.75f, 2.25f, 0.2f, 1.25f, 0.7f, 1.5f));
            // Iron
            defaultData.put("minecraft:iron_" + type, new CritalItemData(key(Items.IRON_HELMET), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
            // Golden
            defaultData.put("minecraft:golden_" + type, new CritalItemData(key(Items.GOLDEN_HELMET), 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
            // Chainmail
            defaultData.put("minecraft:chainmail_" + type, new CritalItemData(key(Items.CHAINMAIL_HELMET), 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
            // Diamond
            defaultData.put("minecraft:diamond_" + type, new CritalItemData(key(Items.DIAMOND_HELMET), 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));
            // Netherite
            defaultData.put("minecraft:netherite_" + type, new CritalItemData(key(Items.NETHERITE_HELMET), 7.0f, 7.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        }

        return new CritalItemDataMap(defaultData);
    }

    public record CritalItemDataMap(Map<String, CritalItemData> itemDataMap) {
    }

    public static String key(Item items) {
        return Registries.ITEM.getId(items).toString();
    }
}
