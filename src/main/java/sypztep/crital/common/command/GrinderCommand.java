package sypztep.crital.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.data.CritalItemDataEntry;
import sypztep.crital.common.util.CritalDataUtil;

import java.util.Optional;

public class GrinderCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("grinder")
                .then(CommandManager.literal("set")
                        .requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.argument("grindtier", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    for (CritTier tier : CritTier.values()) {
                                        builder.suggest(tier.name().toLowerCase());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> execute(context, StringArgumentType.getString(context, "grindtier"), null, null)) // Handle no additional args
                                .then(CommandManager.argument("chanceperc", FloatArgumentType.floatArg(0.0f, 1.0f))
                                        .executes(context -> execute(context, StringArgumentType.getString(context, "grindtier"), FloatArgumentType.getFloat(context, "chanceperc"), null)) // Handle only chanceperc
                                        .then(CommandManager.argument("damageperc", FloatArgumentType.floatArg(0.0f, 1.0f))
                                                .executes(context -> execute(context, StringArgumentType.getString(context, "grindtier"), FloatArgumentType.getFloat(context, "chanceperc"), FloatArgumentType.getFloat(context, "damageperc")))) // Handle both chanceperc and damageperc
                                )
                        )
                )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context, String grindtier, Float chanceperc, Float damageperc) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player != null) {
            ItemStack stack = player.getMainHandStack();
            Optional<CritalItemDataEntry> itemData = CritalItemDataEntry.getCritalItemData(stack);
            if (itemData.isEmpty()) {
                player.sendMessage(Text.literal("Invalid Item Data (this item not have in Map)").formatted(Formatting.RED), false);
                return 0;
            }

            try {
                CritTier tier = CritTier.valueOf(grindtier.toUpperCase());

                float chance = chanceperc != null ? chanceperc : player.getRandom().nextFloat();
                float damage = damageperc != null ? damageperc : player.getRandom().nextFloat();

                CritalDataUtil.applyCritData(stack, tier, chance, damage);

                player.sendMessage(Text.literal("Tier set to " + tier.getName()).formatted(Formatting.GREEN), false);
                return 1;
            } catch (IllegalArgumentException e) {
                player.sendMessage(Text.literal("Invalid tier").formatted(Formatting.RED), false);
            }
        }
        return 0;
    }
}
