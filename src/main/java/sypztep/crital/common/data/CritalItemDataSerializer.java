package sypztep.crital.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
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
                        CritalItemDataMap dataMap = gson.fromJson(jsonObject, CritalItemDataSerializer.CritalItemDataMap.class);
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
                    saveConfig(new CritalItemDataSerializer.CritalItemDataMap(getDefaultData().itemDataMap()));
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
    // Save the configuration to JSON file
    public void saveConfig(CritalItemDataSerializer.CritalItemDataMap newData) {
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
    private @NotNull CritalItemDataSerializer.CritalItemDataMap getDefaultData() {
        Map<String, CritalItemData> defaultData = new HashMap<>();
        // Swords
        defaultData.put("minecraft:wooden_sword", new CritalItemData("minecraft:wooden_sword", 2.0f, 2.5f, 0, 0.2f, 1.25f,0.7f,1.5f));

        return new CritalItemDataSerializer.CritalItemDataMap(defaultData);
    }


    public record CritalItemDataMap(Map<String, CritalItemData> itemDataMap) {
    }
}
