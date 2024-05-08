package sypztep.crital.common.init;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import sypztep.crital.common.data.CritOverhaulConfig;
import sypztep.crital.common.data.CritOverhaulEntry;

import java.util.List;

import static java.util.List.of;
import static net.minecraft.item.ArmorMaterials.*;
import static net.minecraft.item.ArmorMaterials.NETHERITE;

public class ModCritalData {
    private static final String MC = "minecraft:";
    private static final CritOverhaulConfig CRIT_OVERHAUL_CONFIG = new CritOverhaulConfig();

    public static void initItemData() {

        List<CritOverhaulEntry> itemsToManualAdd = of(
                new CritOverhaulEntry(MC + "trident", 12.0f, 15.0f),
                new CritOverhaulEntry(MC + "bow", 12.0f, 15.0f),
                new CritOverhaulEntry(MC + "crossbow", 12.0f, 15.0f),
                new CritOverhaulEntry(MC + "turtle_helmet", 4.0f, 6.0f),
                new CritOverhaulEntry(MC + "elytra", 5.0f, -12.0f) // I hate elytra :) nothing personal
        );

        for (Item item : Registries.ITEM) {
            String itemId = Registries.ITEM.getId(item).toString();

            if (item instanceof SwordItem swordItem) {
                ToolMaterial swordItemMaterial = swordItem.getMaterial();
                float critChance = getToolCritChance(swordItemMaterial) * 3.0f;
                float critDamage = getToolCritChance(swordItemMaterial) * 3.5f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            else if (item instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> armorMaterial = armorItem.getMaterial();
                float critChance = getArmorCritChance(armorMaterial) * 1.5f;
                float critDamage = getArmorCritDamage(armorMaterial) * 2.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            else if (item instanceof AxeItem axeItem) {
                ToolMaterial axeItemMaterial = axeItem.getMaterial();
                float critChance = getToolCritChance(axeItemMaterial) * 1.5f;
                float critDamage = getToolCritChance(axeItemMaterial) * 3.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            else if (item instanceof HoeItem hoeItem) {
                ToolMaterial hoeItemMaterial = hoeItem.getMaterial();
                float critChance = getToolCritChance(hoeItemMaterial) * 5.0f;
                float critDamage = getToolCritChance(hoeItemMaterial);
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            else if (item instanceof PickaxeItem pickaxeItem) {
                ToolMaterial pickaxeItemMaterial = pickaxeItem.getMaterial();
                float critChance = getToolCritChance(pickaxeItemMaterial) * 4.5f;
                float critDamage = getToolCritChance(pickaxeItemMaterial) * 2.5f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            else if (item instanceof ShovelItem shovelItem) {
                ToolMaterial shovelItemMaterial = shovelItem.getMaterial();
                float critChance = getToolCritChance(shovelItemMaterial) * 1.25f;
                float critDamage = getToolCritChance(shovelItemMaterial) ;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }
        }
        CRIT_OVERHAUL_CONFIG.addItems(itemsToManualAdd);
    }
    private static float getArmorCritChance(RegistryEntry<ArmorMaterial> armorMaterial) {
        if (armorMaterial.equals(LEATHER)) {
            return 2.25f; // 9
        } else if (armorMaterial.equals(IRON)) {
            return 3.5f; // 14
        } else if (armorMaterial.equals(GOLD)) {
            return 2.75f; // 11
        } else if (armorMaterial.equals(CHAIN)) {
            return 4.00f; // 16
        } else if (armorMaterial.equals(DIAMOND)) {
            return 4.25f; // 17
        } else if (armorMaterial.equals(NETHERITE)) {
            return 4.75f; // 19
        }
        return 0f;  // Set a default value for other armor materials
    }
    private static float getArmorCritDamage(RegistryEntry<ArmorMaterial> armorMaterial) {
        if (armorMaterial.equals(LEATHER)) {
            return 1.5f; // 9
        } else if (armorMaterial.equals(IRON)) {
            return 2.5f; // 14
        } else if (armorMaterial.equals(GOLD)) {
            return 2.25f; // 11
        } else if (armorMaterial.equals(CHAIN)) {
            return 3.50f; // 16
        } else if (armorMaterial.equals(DIAMOND)) {
            return 4.25f; // 17
        } else if (armorMaterial.equals(NETHERITE)) {
            return 7.75f; // 19
        }
        return 0f;  // Set a default value for other armor materials
    }
    private static float getToolCritChance(ToolMaterial toolMaterial) {
        if (toolMaterial == ToolMaterials.WOOD) {
            return 2.0f;
        } else if (toolMaterial == ToolMaterials.STONE) {
            return 2.5f;
        } else if (toolMaterial == ToolMaterials.IRON) {
            return 3.0f;
        } else if (toolMaterial == ToolMaterials.GOLD) {
            return 3.5f;
        } else if (toolMaterial == ToolMaterials.DIAMOND) {
            return 4.0f;
        } else if (toolMaterial == ToolMaterials.NETHERITE) {
            return 4.5f;
        } else {
            return 0f;
        }
    }
}