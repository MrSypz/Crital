//package sypztep.crital.mixin.vanillachange;
//
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
//import net.minecraft.client.gui.screen.ingame.InventoryScreen;
//import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
//import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
//import net.minecraft.client.network.ClientPlayerEntity;
//import net.minecraft.entity.attribute.EntityAttributes;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.screen.PlayerScreenHandler;
//import net.minecraft.text.MutableText;
//import net.minecraft.text.Text;
//import net.minecraft.util.Identifier;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Environment(EnvType.CLIENT)
//@Mixin(InventoryScreen.class)
//public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
//    @Unique
//    private static final String PLAYER_INFO_KEY = MoonayMod.MODID + ".gui.player_info.";
//    @Unique
//    private static final int TEXTURE_SIZE = 128;
//    @Unique
//    private static final Identifier PLAYERINFO_TEXTURE = MoonayMod.id("textures/gui/container/player_info.png");
//
//    @Shadow @Final private RecipeBookWidget recipeBook;
//
//    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
//        super(screenHandler, playerInventory, text);
//    }
//    /**
//     * Draws player information on the screen, including crit chance, crit damage, and damage reduction.
//     *
//     * @param context  The drawing context.
//     * @param mouseX   The x-coordinate of the mouse.
//     * @param mouseY   The y-coordinate of the mouse.
//     */
//    @Inject(method = "drawBackground", at = @At(value = "RETURN"))
//    public void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
//        if (!ModConfig.CONFIG.playerstats || !ModConfig.CONFIG.shouldDoCrit()) {
//            return;
//        }
//
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        if (player != null) {
//            drawPlayerInfo(context, player, mouseX, mouseY);
//        }
//    }
//    /**
//     * Draws player information, including crit chance, crit damage, and damage reduction.
//     *
//     * @param context  The drawing context.
//     * @param player   The client player entity.
//     * @param mouseX   The x-coordinate of the mouse.
//     * @param mouseY   The y-coordinate of the mouse.
//     */
//    @Unique
//    private void drawPlayerInfo(DrawContext context, ClientPlayerEntity player, int mouseX, int mouseY) {
//        double reduce = calculateDamageReduction(player.getArmor(), player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
//
//        MutableText[] information = new MutableText[]{
//                Text.translatable(PLAYER_INFO_KEY + "header"),
//                Text.translatable(PLAYER_INFO_KEY + "critchance").append(String.format(": %.2f%%", getCritRate(player))),
//                Text.translatable(PLAYER_INFO_KEY + "critdamge").append(String.format(": %.2f%%", getCritDamage(player))),
//                Text.translatable(PLAYER_INFO_KEY + "damagereduce").append(String.format(": %.2f%%", reduce)),
//        };
//
//        if (!recipeBook.isOpen()) {
//            int i = this.x - 128;
//            int j = this.y;
//
//            context.drawTexture(PLAYERINFO_TEXTURE, i, j, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
//
//            int yOffset = 10;
//            int xOffset = 35;
//
//            for (int index = 0; index < information.length; index++) {
//                MutableText text = information[index];
//
//                int offset = (index == 0) ? 24 : 15;
//                int xoffset2 = 10;
//
//                MoonayHelper.drawtextcustom(context, textRenderer, text.getString(), i + xOffset, j + yOffset, 0xFFFFFF, 0, false);
//
//                if (index != 0 && isMouseOverText(mouseX, mouseY, i + xOffset, j + yOffset, textRenderer.getWidth(text), 10)) {
//                    Map<Integer, String> tooltipKeyMap = Map.of(
//                            1, "critchance",
//                            2, "critdamage",
//                            3, "damagereduce",
//                            4, ""
//                    );
//                    String tooltipKey = tooltipKeyMap.getOrDefault(index, "unknown"); // Default to "unknown" if index is not found
//
//                    List<Text> tooltipText = new ArrayList<>();
//                    tooltipText.add(Text.translatable(PLAYER_INFO_KEY + "tooltip." + tooltipKey));
//
//                    int x = mouseX - 5;
//                    int y = mouseY + 2;
//                    context.drawTooltip(textRenderer, tooltipText.get(0), x, y);
//                }
//                yOffset += offset;
//                xOffset = xoffset2;
//            }
//        }
//    }
//    @Unique
//    public double calculateDamageReduction(int armorPoints, double armorToughness) {
//        double effectiveDamageReduction = 80.0 * (armorPoints / 20.0);
//        effectiveDamageReduction += armorToughness;
//        effectiveDamageReduction = Math.min(80.0, effectiveDamageReduction);
//        return effectiveDamageReduction;
//    }
//    @Unique
//    private float getCritRate(ClientPlayerEntity player) {
//        if (player instanceof NewCriticalOverhaul invoker)
//            return invoker.getTotalCritRate();
//        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
//    }
//    @Unique
//    private float getCritDamage(ClientPlayerEntity player) {
//        if (player instanceof NewCriticalOverhaul invoker)
//            return invoker.getTotalCritDamage();
//        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
//    }
//
//    @Unique
//    private boolean isMouseOverText(int mouseX, int mouseY, int textX, int textY, int textWidth, int textHeight) {
//        return mouseX >= textX && mouseX <= textX + textWidth && mouseY >= textY && mouseY <= textY + textHeight;
//    }
//}