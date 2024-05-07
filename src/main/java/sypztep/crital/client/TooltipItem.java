package sypztep.crital.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableFloat;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.data.CritOverhaulConfig;
import sypztep.crital.common.data.CritOverhaulEntry;

import java.util.List;


@Environment(EnvType.CLIENT)
public class TooltipItem {
    private static final CritOverhaulConfig CRIT_OVERHAUL_LOAD_CONFIG = new CritOverhaulConfig();
    public static void onTooltipRender(ItemStack stack, List<Text> lines, Item.TooltipContext  context) {
        String itemId = Registries.ITEM.getId(stack.getItem()).toString();
        if (CRIT_OVERHAUL_LOAD_CONFIG.getCritDataForItem(itemId).isValid()) {
            if (CRIT_OVERHAUL_LOAD_CONFIG.getCritDataForItem(itemId).getCritChance() > 0)
                addCritOverhaulTooltip(stack, lines, Formatting.DARK_GREEN);
             else if (CRIT_OVERHAUL_LOAD_CONFIG.getCritDataForItem(itemId).getCritChance() < 0)
                addCritOverhaulTooltip(stack, lines, Formatting.RED);
        }
    }
    private static void addCritOverhaulTooltip(ItemStack stack, List<Text> lines,Formatting color) {
        String itemName = Registries.ITEM.getId(stack.getItem()).toString();

        CritOverhaulEntry critData = CRIT_OVERHAUL_LOAD_CONFIG.getCritDataForItem(itemName);

        addFormattedTooltip(lines, critData.getCritChance(), "critchance" ,color);
        addFormattedTooltip(lines, critData.getCritDamage(), "critdmg" ,color);
    }

    private static void addFormattedTooltip(List<Text> lines, String value, String key, String... extraKeys) {
        Text tooltip = Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY)
                .append(Text.literal(value).formatted(Formatting.RED));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(CritalMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.GRAY));

        lines.add(tooltip);
    }


    private static void addFormattedTooltip(List<Text> lines, double amount, String key,Formatting formatting, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.literal(" " + mutableFloat + "% ").formatted(formatting).append(Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(formatting));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(CritalMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.DARK_GREEN));

        lines.add(tooltip);
    }
}
