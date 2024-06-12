package sypztep.crital.client.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ArmorItem;
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
public class CritalTooltipRender implements ItemTooltipCallback {
    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
        NbtCompound nbt = CritalDataUtil.getNbtCompound(stack);
        if (stack.contains(DataComponentTypes.CUSTOM_DATA)) {
            addCritTooltips(lines, nbt, stack);
        }
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
            if (stack.getItem() instanceof ArmorItem) {
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

        if (ModConfig.CONFIG.itemInfo) {
            if (Screen.hasShiftDown()) {
                tooltip = tooltip.copy().append(Text.literal(" | Quality: ").formatted(Formatting.GRAY))
                        .append(Text.literal(String.format("%.2f%%", quality)).formatted(qualityColor));
            } else {
                tooltip = tooltip.copy().append(Text.literal(" (Hold Shift)").formatted(Formatting.GRAY));
            }
        }

        lines.add(tooltip);
    }

    private static void addSimpleTooltip(List<Text> lines, float value, String key, Formatting color) {
        String formattedValue = String.format("%.2f", value);
        Text tooltip = Text.translatable(CritalMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY)
                .append(Text.literal(" " + formattedValue).formatted(color));
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
