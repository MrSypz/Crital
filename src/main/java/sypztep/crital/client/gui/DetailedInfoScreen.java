package sypztep.crital.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.init.ModConfig;

@Environment(EnvType.CLIENT)
public class DetailedInfoScreen extends Screen {
    private static final String PLAYER_INFO_KEY = CritalMod.MODID + ".gui.detailed_info.";
    private static final Identifier PLAYERINFO_TEXTURE = CritalMod.id("textures/gui/container/player_info.png");
    private static final int TEXTURE_SIZE = 256;

    private final ClientPlayerEntity player;

    public DetailedInfoScreen(ClientPlayerEntity player) {
        super(Text.literal(PLAYER_INFO_KEY));
        this.player = player;
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

        renderDetailedInfo(context, i, j);
    }

    private void renderDetailedInfo(DrawContext context, int textureX, int textureY) {
        int xOffset = (textureX + 85 + ModConfig.CONFIG.xoffset);
        int yOffset = (textureY + 20 + ModConfig.CONFIG.yoffset);

        StatsScreen.ProtectionValues protectionValues = StatsScreen.getProtectionValues(this.player, 1);

        MutableText[] information = new MutableText[]{
                Text.translatable(PLAYER_INFO_KEY + "generic")
                        .append(": ")
                        .append(Text.literal(String.format("%.2f%%", (1 - protectionValues.genericProtection()) * 100))
                        .formatted(Formatting.GOLD)),
                Text.translatable(PLAYER_INFO_KEY + "blast")
                        .append(": ")
                        .append(Text.literal(String.format("%.2f%%", (1 - protectionValues.blastProtection()) * 100))
                        .formatted(Formatting.GOLD)),
                Text.translatable(PLAYER_INFO_KEY + "fire")
                        .append(": ")
                        .append(Text.literal(String.format("%.2f%%", (1 - protectionValues.fireProtection()) * 100))
                        .formatted(Formatting.GOLD)),
                Text.translatable(PLAYER_INFO_KEY + "projectile")
                        .append(": ")
                        .append(Text.literal(String.format("%.2f%%", (1 - protectionValues.projectileProtection()) * 100))
                        .formatted(Formatting.GOLD)),
        };

        for (MutableText text : information) {
            context.drawTextWithShadow(this.textRenderer, text, xOffset, yOffset, 0xFFFFFF);
            yOffset += 22;
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

