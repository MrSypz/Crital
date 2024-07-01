package sypztep.crital.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.util.CritalDataUtil;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CritalTooltipRender implements ItemTooltipCallback {

    public static void getTooltip(ItemStack stack, List<Text> lines) {
        NbtCompound nbt = CritalDataUtil.getNbtCompound(stack);
        if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            lines.add(Text.of(ScreenTexts.EMPTY));
            PlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;
            // Get the base damage value
            float baseDamage = getItemValue(stack, Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, EntityAttributes.GENERIC_ATTACK_DAMAGE);
            float baseAttackSpeed = getItemValue(stack, Item.BASE_ATTACK_SPEED_MODIFIER_ID, EntityAttributes.GENERIC_ATTACK_SPEED);

            float critChance = nbt.getFloat(CritData.CRITCHANCE_FLAG);
            float critDamage = nbt.getFloat(CritData.CRITDAMAGE_FLAG);
            float critChanceQuality = nbt.getFloat(CritData.CRITCHANCE_QUALITY_FLAG);
            float critDamageQuality = nbt.getFloat(CritData.CRITDAMAGE_QUALITY_FLAG);

            addFormattedTooltip(lines, "\uD83D\uDDE1 Damage", baseDamage, Formatting.GRAY, Formatting.GREEN, false);
            addFormattedTooltip(lines, "  ° Attack Speed", baseAttackSpeed, Formatting.GRAY, Formatting.GREEN, false);
            addFormattedTooltip(lines, "  ° Crit Chance", critChance, Formatting.GRAY, Formatting.GREEN, true);
            addFormattedTooltip(lines, "  ° Crit Damage", critDamage, Formatting.GRAY, Formatting.GREEN, true);
            if (ModConfig.itemInfo) {
                if (Screen.hasShiftDown()) {
                    addFormattedTooltip(lines, "  ° Crit Chance Quality", critChanceQuality, Formatting.GRAY, getQualityColor(critChanceQuality), true);
                    addFormattedTooltip(lines, "  ° Crit Damage Quality", critDamageQuality, Formatting.GRAY, getQualityColor(critDamageQuality), true);
                } else {
                    lines.add(Text.literal(" (Hold Shift)").formatted(Formatting.GRAY));
                }
            }
            // Get armor-related attributes
            float armor = getItemValue(stack, EntityAttributes.GENERIC_ARMOR);
            float armorToughness = getItemValue(stack, EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            float health = nbt.getFloat(CritData.HEALTH_FLAG);

            addFormattedTooltip(lines, "⛊ Armor", armor, Formatting.GRAY, Formatting.GREEN, "+");
            addFormattedTooltip(lines, "  ° Armor Thoughness", armorToughness, Formatting.GRAY, Formatting.GREEN, "+");
            addFormattedTooltip(lines, "  ° Health", health, Formatting.GRAY, Formatting.GREEN, "+");
            lines.add(Text.of(ScreenTexts.EMPTY));
        }
    }

    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
        if (!ModConfig.NewToolTip) {
            NbtCompound nbt = CritalDataUtil.getNbtCompound(stack);
            if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
                addCritTooltips(lines,nbt,stack);
            }
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

    public static void addFormattedTooltip(List<Text> lines, String label, float value, Formatting labelFormatting, Formatting valueFormatting, boolean percent) {
        Text labelText = Text.literal(label + ": ").formatted(labelFormatting);
        Text valueText;
        if (percent)
            valueText = Text.literal(String.format("+" + "%.2f", value) + "%").formatted(valueFormatting);
        else valueText = Text.literal(String.format("%.1f", value)).formatted(valueFormatting);
        Text tooltip = labelText.copy().append(valueText);
        lines.add(tooltip);
    }

    private static void addCritTooltips(List<Text> lines, NbtCompound nbt, ItemStack stack) {
        String tier = nbt.getString(CritData.TIER_FLAG);
        float critChance = nbt.getFloat(CritData.CRITCHANCE_FLAG);
        float critDamage = nbt.getFloat(CritData.CRITDAMAGE_FLAG);
        float critChanceQuality = nbt.getFloat(CritData.CRITCHANCE_QUALITY_FLAG);
        float critDamageQuality = nbt.getFloat(CritData.CRITDAMAGE_QUALITY_FLAG);
        float healthAmount = nbt.getFloat(CritData.HEALTH_FLAG);

        if (critChance != 0 && critDamage != 0 && tier != null) {
            addCritTooltip(lines, critChance, "crit_chance", critChanceQuality);
            addCritTooltip(lines, critDamage, "crit_damage", critDamageQuality);
            if (stack.getItem() instanceof ArmorItem && ModConfig.chestplateExtraStats) {
                addValueSimpleTooltip(lines, healthAmount, "health");
            }
            addTierTooltip(lines, tier);
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

    private static void addValueSimpleTooltip(List<Text> lines, float value, String key) {
        Formatting valueColor = value > 0 ? Formatting.DARK_GREEN : Formatting.RED;
        String IsGood = value > 0 ? "+" : "";
        String formattedValue = String.format("%.2f", value);
        Text tooltip = Text.literal(" " + IsGood + formattedValue + " ").formatted(valueColor)
                .append(Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY));
        lines.add(tooltip);
    }

    private static void addTierTooltip(List<Text> lines, String tier) {
        Text tooltip = Text.translatable(CritalMod.MODID + ".modifytooltip.tier_flag").formatted(Formatting.GRAY)
                .append(Text.literal(" " + tier).formatted(CritData.getTierFormatting(tier)).formatted(Formatting.BOLD));
        lines.add(tooltip);
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


}
