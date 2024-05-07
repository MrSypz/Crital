package sypztep.crital.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.api.NewCriticalOverhaul;

@Environment(EnvType.CLIENT)
public class StatsScreen extends Screen {
    private static final String PLAYER_INFO_KEY = CritalMod.MODID + ".gui.player_info.";

    private static final Identifier PLAYERINFO_TEXTURE = CritalMod.id("textures/gui/container/player_info.png");

    public StatsScreen() {
        super(Text.literal("statsScreen"));
    }
    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context,mouseX,mouseY,delta);
        int TEXTURE_SIZE = 256;
        int i = (this.width - TEXTURE_SIZE) / 2;
        int j = (this.height - TEXTURE_SIZE) / 2;

        renderInfo(context);

        context.drawTexture(PLAYERINFO_TEXTURE, i, j, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
        InventoryScreen.drawEntity(context, i + 20, j + 8, i + 75, j + 78, 30, 0.0625f, mouseX, mouseY, this.client.player);
    }
    private void renderInfo(DrawContext context) {
        MutableText[] information = new MutableText[]{
                Text.translatable(PLAYER_INFO_KEY + "critchance").append(String.format(": %.2f%%", getCritRate(this.client.player))),
                Text.translatable(PLAYER_INFO_KEY + "critdamge").append(String.format(": %.2f%%", getCritDamage(this.client.player))),
        };
        int xOffset = this.width / 2;
        int yOffset = 40 ;

        for (int index = 0; index < information.length; index++) {
            MutableText text = information[index];

            int offset = 15;

            context.drawTextWithShadow(textRenderer,text, xOffset, yOffset, 0xFFFFFF);
            yOffset += offset;
        }
    }
    @Override
    public boolean shouldPause() {
        return false;
    }

    private float getCritRate(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritRate();
        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }
    private float getCritDamage(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritDamage();
        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }
}
