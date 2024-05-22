package sypztep.crital.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableFloat;
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
        String critChanceQuality = String.format("%.2f", nbtCompound.getFloat(CritData.CRITCHANCE_QUALITY_FLAG));
        String critDamageQuality =String.format("%.2f",nbtCompound.getFloat(CritData.CRITDAMAGE_QUALITY_FLAG));
        addCritTooltip(lines, critChance, "crit_chance", "| Quality", critChanceQuality + "%");
        addCritTooltip(lines, critDamage, "crit_damage", "| Quality", critDamageQuality + "%");
        addFormattedTooltip(lines, tier, "tier_flag", CritData.getTierFormatting(tier), Formatting.BOLD);
    }

    private static void addCritTooltip(List<Text> lines, float amount, String key, String... extraKeys) {
        Formatting color = amount > 0 ? Formatting.DARK_GREEN : Formatting.RED;
        addFormattedTooltip(lines, amount, key, color, extraKeys);
    }

    private static void addFormattedTooltip(List<Text> lines, String tier, String key, Formatting color, String... extraKeys) {
        Text tooltip = (Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY).append(Text.literal(" " + tier).formatted(color)));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(CritalMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.DARK_GREEN));

        lines.add(tooltip);
    }
    private static void addFormattedTooltip(List<Text> lines, String tier, String key, Formatting color, Formatting... additionalFormats) {
        Text tooltip = (Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY).append(Text.literal(" " + tier).formatted(color)));
        for (Formatting formatting : additionalFormats)
            tooltip = tooltip.copy().formatted(formatting);

        lines.add(tooltip);
    }
    private static void addFormattedTooltip(List<Text> lines, float amount, String key, Formatting color, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.literal(" " + mutableFloat + "% ").formatted(color)
                .append(Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(color));

        if (extraKeys.length > 0 && ModConfig.CONFIG.itemInfo) {
            if (Screen.hasShiftDown()) {
                StringBuilder extraText = new StringBuilder();
                for (String extraKey : extraKeys) {
                    extraText.append(" ").append(extraKey);
                }
                tooltip = tooltip.copy().append(Text.literal(extraText.toString()).formatted(color));
            } else {
                tooltip = tooltip.copy().append(Text.literal(" (Hold Shift)").formatted(Formatting.GRAY));
            }
        }

        lines.add(tooltip);
    }
}
