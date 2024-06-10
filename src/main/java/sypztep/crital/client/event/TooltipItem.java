package sypztep.crital.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.util.CritalDataUtil;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TooltipItem {
    public static void onTooltipRender(ItemStack stack, List<Text> lines, Item.TooltipContext context) {
        NbtCompound value = CritalDataUtil.getNbtCompound(stack);
        if (!stack.getDefaultComponents().isEmpty() && stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            addCritOverhaulTooltip(lines, value);
        }
    }

    private static void addCritOverhaulTooltip(List<Text> lines, NbtCompound nbtCompound) {
        String tier = nbtCompound.getString(CritData.TIER_FLAG);
        float critChance = nbtCompound.getFloat(CritData.CRITCHANCE_FLAG);
        float critDamage = nbtCompound.getFloat(CritData.CRITDAMAGE_FLAG);
        float critChanceQuality = nbtCompound.getFloat(CritData.CRITCHANCE_QUALITY_FLAG);
        float critDamageQuality = nbtCompound.getFloat(CritData.CRITDAMAGE_QUALITY_FLAG);
        Formatting critChanceQualityColor = getColorBasedOnQuality(critChanceQuality);
        Formatting critDamageQualityColor = getColorBasedOnQuality(critDamageQuality);
        if (critChance != 0 && critDamage != 0 && tier != null) {
            addCritTooltip(lines, Math.min(critChance, 100), "crit_chance", critChanceQuality, critChanceQualityColor);
            addCritTooltip(lines, critDamage, "crit_damage", critDamageQuality, critDamageQualityColor);
            addFormattedTooltip(lines, tier, "tier_flag", CritData.getTierFormatting(tier), Formatting.BOLD);
        }
    }

    private static void addCritTooltip(List<Text> lines, float amount, String key, float qualityValue, Formatting qualityColor) {
        Formatting color = amount > 0 ? Formatting.DARK_GREEN : Formatting.RED;
        addFormattedTooltip(lines, amount, key, color, qualityValue, qualityColor);
    }
    private static void addFormattedTooltip(List<Text> lines, String tier, String key, Formatting color, Formatting... additionalFormats) {
        Text tooltip = (Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY).append(Text.literal(" " + tier).formatted(color)));
        for (Formatting formatting : additionalFormats)
            tooltip = tooltip.copy().formatted(formatting);

        lines.add(tooltip);
    }
    private static void addFormattedTooltip(List<Text> lines, float amount, String key, Formatting color, float qualityValue, Formatting qualityColor) {
        String formattedAmount = String.format("%.2f", amount);

        Text tooltip = Text.literal(" " + formattedAmount + "% ").formatted(color)
                .append(Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(color));
        if (!ModConfig.CONFIG.itemInfo)
            return;
        if (Screen.hasShiftDown()) {
                tooltip = tooltip.copy()
                        .append(Text.literal(" | Quality: ").formatted(Formatting.GRAY))
                        .append(Text.literal(String.format("%.2f%%", qualityValue)).formatted(qualityColor));
        } else
            tooltip = tooltip.copy().append(Text.literal(" (Hold Shift)").formatted(Formatting.GRAY));

        lines.add(tooltip);
    }
    private static Formatting getColorBasedOnQuality(float quality) {
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
