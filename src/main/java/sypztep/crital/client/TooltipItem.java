package sypztep.crital.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.CritDataHandler;
import sypztep.crital.common.data.CritData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TooltipItem {
    private static CritDataHandler critResultHolder;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void onTooltipRender(ItemStack stack, List<Text> lines, Item.TooltipContext context) {
        assert client.player != null;
        if (!stack.getDefaultComponents().isEmpty() && stack.contains(DataComponentTypes.CUSTOM_DATA))
            addCritOverhaulTooltip(stack, lines, Formatting.DARK_GREEN);
//        else if (getCritRate(client.player) < 0)
//            addCritOverhaulTooltip(stack, lines, Formatting.RED);
    }

    private static void addCritOverhaulTooltip(ItemStack stack, List<Text> lines, Formatting color) {
        NbtCompound value = new NbtCompound();
        @Nullable var data = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (data != null)
            value = data.copyNbt();
        String tier = value.getString(CritData.TIER_FLAG);
        float critChance = value.getFloat(CritData.CRITCHANCE_FLAG);
        float critDamage = value.getFloat(CritData.CRITDAMAGE_FLAG);
        //TODO:Add a flag color and boarder!
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
