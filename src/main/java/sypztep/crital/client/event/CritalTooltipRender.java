package sypztep.crital.client.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.init.ModDataComponent;

import sypztep.crital.common.util.CritalDataUtil;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.*;

public class CritalTooltipRender implements ItemTooltipCallback {

    public static void getTooltip(ItemStack stack, List<Text> lines, Item.TooltipContext tooltipContext) {
        NbtCompound nbt = ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL);
        if (stack.contains(ModDataComponent.CRITAL)) {
            lines.add(Text.of(ScreenTexts.EMPTY));
            PlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;

            float baseDamage = getItemValue(stack, Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, EntityAttributes.GENERIC_ATTACK_DAMAGE);
            float baseAttackSpeed = getItemValue(stack, Item.BASE_ATTACK_SPEED_MODIFIER_ID, EntityAttributes.GENERIC_ATTACK_SPEED);

            float critChance = nbt.getFloat(CritalData.CRITCHANCE);
            float critDamage = nbt.getFloat(CritalData.CRITDAMAGE);
            float critChanceQuality = nbt.getFloat(CritalData.CRITCHANCE_QUALITY);
            float critDamageQuality = nbt.getFloat(CritalData.CRITDAMAGE_QUALITY);

            String tier = nbt.getString(CritalData.TIER);
            if (ModConfig.tierTypes == ModConfig.TierTypes.STAR)
                addTierStar(lines, tier);
            else addTierTooltip(lines, tier);

            addEnchantmentSlotsTooltip(lines, stack, tooltipContext);

            if (!(stack.getItem() instanceof ArmorItem || stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem)) { // sword
                applyIfValid(baseDamage, () -> addFormattedTooltip(lines, "⚔ Damage", baseDamage, Formatting.GRAY, Formatting.GREEN, false, stack));
                applyIfValid(baseAttackSpeed, () -> addFormattedTooltip(lines, "  ° Attack Speed", baseAttackSpeed, Formatting.GRAY, Formatting.GREEN, false));
                applyIfValid(critChance, () -> addFormattedTooltip(lines, "  ° Crit Chance", critChance, Formatting.GRAY, greenOrRed(critChance), true));
                applyIfValid(critDamage, () -> addFormattedTooltip(lines, "  ° Crit Damage", critDamage, Formatting.GRAY, greenOrRed(critDamage), true));
            } else { // armor
                lines.add(Text.literal("⚔ Stats").formatted(Formatting.GRAY));
                applyIfValid(critChance, () -> addFormattedTooltip(lines, "  ° Crit Chance", critChance, Formatting.GRAY, greenOrRed(critChance), true));
                applyIfValid(critDamage, () -> addFormattedTooltip(lines, "  ° Crit Damage", critDamage, Formatting.GRAY, greenOrRed(critDamage), true));
            }

            if (ModConfig.itemInfo) {
                if (Screen.hasShiftDown()) {
                    applyIfValid(critChanceQuality, () -> addFormattedTooltip(lines, "  ° Crit Chance Quality", critChanceQuality, Formatting.GRAY, getQualityColor(critChanceQuality), true));
                    applyIfValid(critDamageQuality, () -> addFormattedTooltip(lines, "  ° Crit Damage Quality", critDamageQuality, Formatting.GRAY, getQualityColor(critDamageQuality), true));
                } else {
                    lines.add(Text.literal(" (Hold Shift)").formatted(Formatting.GRAY));
                }
            }

            if (stack.getItem() instanceof ArmorItem) {
                float armor = getItemValue(stack, EntityAttributes.GENERIC_ARMOR);
                float armorToughness = getItemValue(stack, EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
                applyIfValid(armor, () -> addFormattedTooltip(lines, "⛊ Armor", armor, Formatting.GRAY, Formatting.GREEN, "+"));
                applyIfValid(armorToughness, () -> addFormattedTooltip(lines, "  ° Armor Toughness", armorToughness, Formatting.GRAY, Formatting.GREEN, "+"));
            }
        }
    }


    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
        if (!ModConfig.NewToolTip) {
            NbtCompound nbt = ItemStackHelper.getNbtCompound(stack, ModDataComponent.CRITAL);
            addCritTooltips(lines, nbt, stack);
        }
    }

    private static <T extends TooltipAppender> List<String> getEnchantmentTooltip(ItemStack stack, ComponentType<T> componentType, Item.TooltipContext context) {
        TooltipAppender tooltipAppender = stack.get(componentType);
        if (tooltipAppender != null) {
            Set<String> tooltipText = new HashSet<>();
            tooltipAppender.appendTooltip(context, text -> tooltipText.add(text.getString()), TooltipType.BASIC);
            return new ArrayList<>(tooltipText);
        }
        return Collections.emptyList();
    }

    public static boolean isValid(Number value) {
        if (value == null) return false;
        return value.doubleValue() != 0;
    }

    public static void applyIfValid(Number value, Runnable action) {
        if (isValid(value)) {
            action.run();
        }
    }
    private static void applyIfValid(float critChance, float critDamage, String tier, Runnable action) {
        if (critChance != 0 && critDamage != 0 && tier != null) {
            action.run();
        }
    }

    private static void addEnchantmentSlotsTooltip(List<Text> lines, ItemStack stack, Item.TooltipContext tooltipContext) {
        List<String> enchantments = getEnchantmentTooltip(stack, DataComponentTypes.ENCHANTMENTS, tooltipContext);
        String s = !enchantments.isEmpty() ? String.valueOf(enchantments.size()) : "Empty";
        Text enchantmentSlotsLabel = Text.literal("❖ Enchantment Slots " + "(" + s + ")").formatted(Formatting.GRAY);
        lines.add(enchantmentSlotsLabel);

        for (String enchantment : enchantments) {
            Text grayArrow = Text.literal("  ◇ ").formatted(Formatting.GRAY);
            Text enchantmentText = Text.literal(enchantment).formatted(Formatting.GREEN);
            Text slotText = grayArrow.copy().append(enchantmentText);
            lines.add(slotText);
        }
    }

    private static float getItemValue(ItemStack stack, Identifier identifier, RegistryEntry<EntityAttribute> attribute) {
        float[] value = {0.0f};
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            for (AttributeModifierSlot attributeModifierSlot : AttributeModifierSlot.values()) {
                stack.applyAttributeModifier(attributeModifierSlot, (attr, modifier) -> {
                    if (modifier.idMatches(identifier)) {
                        value[0] += (float) modifier.value();
                        value[0] += (float) player.getAttributeBaseValue(attribute);
                    }
                });
            }
        }
        return value[0];
    }

    private static float getItemValue(ItemStack stack, RegistryEntry<EntityAttribute> attribute) {
        float[] value = {0.0f};
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            for (AttributeModifierSlot attributeModifierSlot : AttributeModifierSlot.values()) {
                stack.applyAttributeModifier(attributeModifierSlot, (attr, modifier) -> {
                    if (attr.getKey().equals(attribute.getKey())) {
                        value[0] += (float) modifier.value();
                        value[0] += (float) player.getAttributeBaseValue(attribute);
                    }
                });
            }
        }
        return value[0];
    }

    public static void addFormattedTooltip(List<Text> lines, String label, float value, Formatting labelFormatting, Formatting valueFormatting, String extra) {
        Text labelText = Text.literal(label + ": ").formatted(labelFormatting);
        Text valueText = Text.literal(extra + String.format("%.1f", value)).formatted(valueFormatting);
        Text tooltip = labelText.copy().append(valueText);
        lines.add(tooltip);
    }

    private static void addFormattedTooltip(List<Text> lines, String label, float value, Formatting labelFormatting, Formatting valueFormatting, boolean percent, ItemStack stack) {
        float sharpnessBonus = 0.0f;
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        RegistryEntry<Enchantment> enchant = player.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.SHARPNESS).get();
        int sharpnessLevel = stack.getEnchantments().getLevel(enchant);
        if (sharpnessLevel > 0)
            sharpnessBonus = 0.5f * sharpnessLevel + 0.5f;
        Text labelText = Text.literal(label + ": ").formatted(labelFormatting);
        Text valueText;
        if (percent)
            valueText = Text.literal(String.format(plusOrMinus(value + sharpnessBonus) + "%.2f", value + sharpnessBonus) + "%").formatted(valueFormatting);
        else
            valueText = Text.literal(plusOrMinus(value + sharpnessBonus) + String.format("%.1f", value + sharpnessBonus)).formatted(valueFormatting);

        Text tooltip = labelText.copy().append(valueText);
        lines.add(tooltip);
    }

    public static void addFormattedTooltip(List<Text> lines, String label, float value, Formatting labelFormatting, Formatting valueFormatting, boolean percent) {
        Text labelText = Text.literal(label + ": ").formatted(labelFormatting);
        Text valueText;
        if (percent)
            valueText = Text.literal(String.format(plusOrMinus(value) + "%.2f", value) + "%").formatted(valueFormatting);
        else valueText = Text.literal(String.format("%.1f", value)).formatted(valueFormatting);
        Text tooltip = labelText.copy().append(valueText);
        lines.add(tooltip);
    }

    private static void addCritTooltips(List<Text> lines, NbtCompound nbt, ItemStack stack) {
        if (stack.contains(ModDataComponent.CRITAL)) {
            String tier = nbt.getString(CritalData.TIER);
            float critChance = nbt.getFloat(CritalData.CRITCHANCE);
            float critDamage = nbt.getFloat(CritalData.CRITDAMAGE);
            float critChanceQuality = nbt.getFloat(CritalData.CRITCHANCE_QUALITY);
            float critDamageQuality = nbt.getFloat(CritalData.CRITDAMAGE_QUALITY);

            applyIfValid(critChance, critDamage, tier, () -> {
                addCritTooltip(lines, critChance, "crit_chance", critChanceQuality);
                addCritTooltip(lines, critDamage, "crit_damage", critDamageQuality);
                addTierTooltip(lines, tier);
            });
        }
    }

    private static void addCritTooltip(List<Text> lines, float value, String key, float quality) {
        Formatting valueColor = value > 0 ? Formatting.DARK_GREEN : Formatting.RED;
        Formatting qualityColor = getQualityColor(quality);
        String formattedValue = String.format("%.2f", value);

        Text tooltip = Text.literal(" " + formattedValue + "% ").formatted(valueColor)
                .append(Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(valueColor));

        if (ModConfig.itemInfo) {
            if (Screen.hasShiftDown()) {
                tooltip = tooltip.copy().append(Text.literal(" | Quality: ").formatted(Formatting.GRAY))
                        .append(Text.literal(String.format("%.2f%%", quality)).formatted(qualityColor));
            } else {
                tooltip = tooltip.copy().append(Text.literal(" (Hold Shift)").formatted(Formatting.GRAY));
            }
        }

        lines.add(tooltip);
    }

    private static void addTierTooltip(List<Text> lines, String tier) {
        Formatting color = CritalDataUtil.getTierFormatting(tier);
        Text tooltip = Text.translatable(CritalMod.MODID + ".modifytooltip.tier_flag").formatted(Formatting.GRAY)
                .append(Text.literal(" " + tier).formatted(color).formatted(Formatting.BOLD));
        lines.add(tooltip);
    }

    private static void addTierStar(List<Text> lines, String tier) {
        int i = getTierValue(tier);
        Text tierinfo = Text.literal("✠ Tier ─ ").formatted(Formatting.GRAY);
        Formatting color = CritalDataUtil.getTierFormatting(tier);

        for (int j = 0; j < 7; j++) {
            if (j < i) {
                tierinfo = tierinfo.copy().append(Text.literal("★").formatted(color));
            } else {
                tierinfo = tierinfo.copy().append(Text.literal("☆").formatted(color));
            }
        }
        lines.add(tierinfo);
    }

    public static int getTierValue(String tierName) {
        return switch (tierName.toLowerCase()) {
            case "common" -> 1;
            case "uncommon" -> 2;
            case "rare" -> 3;
            case "epic" -> 4;
            case "legendary" -> 5;
            case "mythic" -> 6;
            case "celestial" -> 7;
            default -> 0; // Handle unknown tiers as needed
        };
    }

    private static Formatting getQualityColor(float quality) {
        if (quality >= 75) {
            return Formatting.DARK_GREEN;
        } else if (quality >= 50) {
            return Formatting.GREEN;
        } else if (quality >= 25) {
            return Formatting.YELLOW;
        } else {
            return Formatting.RED;
        }
    }

    private static String plusOrMinus(float value) {
        return value > 0 ? "+" : "";
    }

    private static Formatting greenOrRed(float value) {
        return value >= 0 ? Formatting.GREEN : Formatting.RED;
    }
}
