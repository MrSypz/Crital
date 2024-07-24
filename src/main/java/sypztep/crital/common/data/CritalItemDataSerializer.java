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

    private void createDirectoriesIfNeeded() throws IOException {
        if (!Files.exists(CONFIG_FILE_PATH.getParent())) {
            Files.createDirectories(CONFIG_FILE_PATH.getParent());
        }
    }

    @Contract(" -> new")
    private @NotNull CritalItemDataMap getDefaultData() {
        Map<String, CritalItemData> defaultData = new HashMap<>();

        // Range Weapons
        defaultData.put("minecraft:bow", new CritalItemData(key(Items.BOW), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:crossbow", new CritalItemData(key(Items.CROSSBOW), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:trident", new CritalItemData(key(Items.TRIDENT), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:shield", new CritalItemData(key(Items.SHIELD), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Maces
        defaultData.put("minecraft:mace", new CritalItemData(key(Items.MACE), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Special helmet
        defaultData.put("minecraft:turtle_helmet", new CritalItemData(key(Items.TURTLE_HELMET), 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Swords
        defaultData.put("minecraft:wooden_sword", new CritalItemData("minecraft:wooden_sword", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:stone_sword", new CritalItemData("minecraft:stone_sword", 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_sword", new CritalItemData("minecraft:iron_sword", 5.0f, 5.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_sword", new CritalItemData("minecraft:golden_sword", 3.5f, 4.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_sword", new CritalItemData("minecraft:diamond_sword", 6.0f, 6.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_sword", new CritalItemData("minecraft:netherite_sword", 7.0f, 7.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Shovels
        defaultData.put("minecraft:wooden_shovel", new CritalItemData("minecraft:wooden_shovel", 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:stone_shovel", new CritalItemData("minecraft:stone_shovel", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_shovel", new CritalItemData("minecraft:iron_shovel", 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_shovel", new CritalItemData("minecraft:golden_shovel", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_shovel", new CritalItemData("minecraft:diamond_shovel", 5.0f, 5.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_shovel", new CritalItemData("minecraft:netherite_shovel", 6.0f, 6.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Hoes
        defaultData.put("minecraft:wooden_hoe", new CritalItemData("minecraft:wooden_hoe", 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:stone_hoe", new CritalItemData("minecraft:stone_hoe", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_hoe", new CritalItemData("minecraft:iron_hoe", 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_hoe", new CritalItemData("minecraft:golden_hoe", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_hoe", new CritalItemData("minecraft:diamond_hoe", 5.0f, 5.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_hoe", new CritalItemData("minecraft:netherite_hoe", 6.0f, 6.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Axes
        defaultData.put("minecraft:wooden_axe", new CritalItemData("minecraft:wooden_axe", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:stone_axe", new CritalItemData("minecraft:stone_axe", 3.5f, 4.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_axe", new CritalItemData("minecraft:iron_axe", 4.5f, 5.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_axe", new CritalItemData("minecraft:golden_axe", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_axe", new CritalItemData("minecraft:diamond_axe", 5.5f, 6.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_axe", new CritalItemData("minecraft:netherite_axe", 6.5f, 7.0f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Pickaxes
        defaultData.put("minecraft:wooden_pickaxe", new CritalItemData("minecraft:wooden_pickaxe", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:stone_pickaxe", new CritalItemData("minecraft:stone_pickaxe", 3.5f, 4.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_pickaxe", new CritalItemData("minecraft:iron_pickaxe", 4.5f, 5.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_pickaxe", new CritalItemData("minecraft:golden_pickaxe", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_pickaxe", new CritalItemData("minecraft:diamond_pickaxe", 5.5f, 6.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_pickaxe", new CritalItemData("minecraft:netherite_pickaxe", 6.5f, 7.0f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Armors
        // Leather
        defaultData.put("minecraft:leather_helmet", new CritalItemData("minecraft:leather_helmet", 1.75f, 2.25f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:leather_chestplate", new CritalItemData("minecraft:leather_chestplate", 1.75f, 2.25f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:leather_leggings", new CritalItemData("minecraft:leather_leggings", 1.75f, 2.25f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:leather_boots", new CritalItemData("minecraft:leather_boots", 1.75f, 2.25f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Iron
        defaultData.put("minecraft:iron_helmet", new CritalItemData("minecraft:iron_helmet", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_chestplate", new CritalItemData("minecraft:iron_chestplate", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_leggings", new CritalItemData("minecraft:iron_leggings", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:iron_boots", new CritalItemData("minecraft:iron_boots", 3.0f, 3.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Golden
        defaultData.put("minecraft:golden_helmet", new CritalItemData("minecraft:golden_helmet", 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_chestplate", new CritalItemData("minecraft:golden_chestplate", 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_leggings", new CritalItemData("minecraft:golden_leggings", 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:golden_boots", new CritalItemData("minecraft:golden_boots", 2.0f, 2.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Chainmail
        defaultData.put("minecraft:chainmail_helmet", new CritalItemData("minecraft:chainmail_helmet", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:chainmail_chestplate", new CritalItemData("minecraft:chainmail_chestplate", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:chainmail_leggings", new CritalItemData("minecraft:chainmail_leggings", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:chainmail_boots", new CritalItemData("minecraft:chainmail_boots", 2.5f, 3.0f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Diamond
        defaultData.put("minecraft:diamond_helmet", new CritalItemData("minecraft:diamond_helmet", 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_chestplate", new CritalItemData("minecraft:diamond_chestplate", 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_leggings", new CritalItemData("minecraft:diamond_leggings", 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:diamond_boots", new CritalItemData("minecraft:diamond_boots", 4.0f, 4.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        // Netherite
        defaultData.put("minecraft:netherite_helmet", new CritalItemData("minecraft:netherite_helmet", 7.0f, 7.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_chestplate", new CritalItemData("minecraft:netherite_chestplate", 7.0f, 7.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_leggings", new CritalItemData("minecraft:netherite_leggings", 7.0f, 7.5f, 0.2f, 1.25f, 0.7f, 1.5f));
        defaultData.put("minecraft:netherite_boots", new CritalItemData("minecraft:netherite_boots", 7.0f, 7.5f, 0.2f, 1.25f, 0.7f, 1.5f));

        return new CritalItemDataMap(defaultData);
    }

    public record CritalItemDataMap(Map<String, CritalItemData> itemDataMap) {
    }

    private static String key(Item items) {
        return Registries.ITEM.getId(items).toString();
    }
}
