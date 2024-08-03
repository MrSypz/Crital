package sypztep.crital.common.reload;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.data.CritalItemDataEntry;

import java.io.InputStream;
import java.io.InputStreamReader;

public class CritalItemReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Identifier ID = CritalMod.id("critalitemdata");
    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        CritalItemDataEntry.CRITAL_ITEM_ENTRY_MAP.clear();
        manager.findAllResources("grinder", path -> path.getNamespace().equals(CritalMod.MODID) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
            for (Resource resource : resources) {
                try (InputStream stream = resource.getInputStream()) {
                    JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
                    Identifier itemId = Identifier.of(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
                    Item item = Registries.ITEM.get(itemId);

                    if (item == Registries.ITEM.get(Registries.ITEM.getDefaultId()) && !itemId.equals(Registries.ITEM.getDefaultId())) {
                        continue;
                    }
                    float baseCritChance = object.get("baseCritChance").getAsFloat();
                    float baseCritDamage = object.get("baseCritDamage").getAsFloat();
                    float minCritChanceMultiply = object.get("minCritChanceMultiply").getAsFloat();
                    float maxCritChanceMultiply = object.get("maxCritChanceMultiply").getAsFloat();
                    float minCritDamageMultiply = object.get("minCritDamageMultiply").getAsFloat();
                    float maxCritDamageMultiply = object.get("maxCritDamageMultiply").getAsFloat();

                    CritalItemDataEntry entry = new CritalItemDataEntry(
                            baseCritChance,
                            baseCritDamage,
                            minCritChanceMultiply,
                            maxCritChanceMultiply,
                            minCritDamageMultiply,
                            maxCritDamageMultiply
                    );
                    CritalItemDataEntry.CRITAL_ITEM_ENTRY_MAP.put(Registries.ITEM.getEntry(item), entry);

                } catch (Exception ignored) {

                }
            }
        });
    }
}
