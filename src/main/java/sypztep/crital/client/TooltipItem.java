package sypztep.crital.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableFloat;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.util.CritalDataUtil;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TooltipItem {
    public static void onTooltipRender(ItemStack stack, List<Text> lines, Item.TooltipContext context) {
        NbtCompound value = CritalDataUtil.getNbtCompoundFromStack(stack);
        float critChance = value.getFloat(CritData.CRITCHANCE_FLAG);
        if (!stack.getDefaultComponents().isEmpty() && stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            if (critChance > 0)
                addCritOverhaulTooltip(stack, lines, Formatting.DARK_GREEN, value);
             else addCritOverhaulTooltip(stack, lines, Formatting.RED, value);

        }
    }

    private static void addCritOverhaulTooltip(ItemStack stack, List<Text> lines, Formatting color, NbtCompound value) {
        String tier = value.getString(CritData.TIER_FLAG);
        float critChance = value.getFloat(CritData.CRITCHANCE_FLAG);
        float critDamage = value.getFloat(CritData.CRITDAMAGE_FLAG);
        //TODO:Add a flag color and border!
        addFormattedTooltip(lines, tier, "tier_flag", color);
        addFormattedTooltip(lines, critChance, "crit_chance", color);
        addFormattedTooltip(lines, critDamage, "crit_damage", color);
    }
    private static void addFormattedTooltip(List<Text> lines, String tier, String key, Formatting color, String... extraKeys) {
        Text tooltip = (Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY).append(Text.literal(" " + tier).formatted(color)));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(CritalMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.DARK_GREEN));

        lines.add(tooltip);
    }

    private static void addFormattedTooltip(List<Text> lines, double amount, String key, Formatting color, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.literal(" " + mutableFloat + "% ").formatted(color).append(Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(color));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(CritalMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.DARK_GREEN));

        lines.add(tooltip);
    }
}
