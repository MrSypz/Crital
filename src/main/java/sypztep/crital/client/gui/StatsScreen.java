package sypztep.crital.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.ModConfig;
import sypztep.crital.common.api.crital.NewCriticalOverhaul;

@Environment(EnvType.CLIENT)
public class StatsScreen extends Screen {
    private static final String PLAYER_INFO_KEY = CritalMod.MODID + ".gui.player_info.";
    private static final Identifier PLAYERINFO_TEXTURE = CritalMod.id("textures/gui/container/player_info.png");
    private static final Identifier ICON_TEXTURE = CritalMod.id("textures/gui/container/pangaea.png");
    private static final int TEXTURE_SIZE = 256;

    public StatsScreen() {
        super(Text.translatable(CritalMod.MODID + ".gui" + ".statsscreen"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int i = (this.width - 195) / 2;
        int j = (this.height - 138) / 2;
        context.drawTexture(PLAYERINFO_TEXTURE, i, j, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);

        renderInfo(context, i, j);

        InventoryScreen.drawEntity(context, i, j + 20, i + 60, j + 82, 30, 0.0625f, mouseX, mouseY, this.client.player);
    }

    private void renderInfo(DrawContext context, int textureX, int textureY) {
        int xOffset = (textureX + 85 + ModConfig.xoffset);
        int yOffset = (textureY + 20 + ModConfig.yoffset);
        int vOffset = 0;

        MutableText[] information = new MutableText[]{
                Text.translatable(PLAYER_INFO_KEY + "critchance")
                        .append(": ")
                        .append(Text.literal(String.format("%.2f%%", getCritRate(this.client.player)))
                        .formatted(Formatting.GOLD)),
                Text.translatable(PLAYER_INFO_KEY + "critdamage")
                        .append(": ")
                        .append(Text.literal(String.format("%.2f%%", getCritDamage(this.client.player)))
                        .formatted(Formatting.GOLD)),
                Text.translatable(PLAYER_INFO_KEY + "damagereduce")
                        .append(": ")
                        .append(Text.literal(String.format("%.2f%%", getDamageLeft(this.client.player)))
                        .formatted(Formatting.GOLD)),
                Text.translatable(PLAYER_INFO_KEY + "maxhealth")
                        .append(": ")
                        .append(Text.literal(String.format("%.1f", this.client.player.getMaxHealth()))
                        .formatted(Formatting.GOLD)),
        };


        for (int index = 0; index < information.length; index++) {
            MutableText text = information[index];

            context.drawTextWithShadow(this.textRenderer, text, xOffset, yOffset, 0xFFFFFF);
            context.drawTexture(ICON_TEXTURE, xOffset - 20 , yOffset - 2 , vOffset, 0, 16, 16, TEXTURE_SIZE, TEXTURE_SIZE);

            yOffset += 22 ;
            vOffset += 16 ;
        }
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    private static float getCritRate(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritRate();
        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }

    private static float getCritDamage(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritDamage();
        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }
    private static float getDamageLeft(ClientPlayerEntity player) {
        if (player != null) {
            float armorToughnesss = (float) player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            int armor = player.getArmor();
            float f = 2.0f + armorToughnesss / 4.0f;
            float g = MathHelper.clamp(armor - 1 / f, armor * 0.2f, 20.0f);
            return (float) Math.ceil((g / 25.0f) * 100);
        }
        return 0;
    }
}
