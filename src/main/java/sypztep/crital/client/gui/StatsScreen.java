package sypztep.crital.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.crital.common.CritalMod;

@Environment(EnvType.CLIENT)
public class StatsScreen extends Screen {
    private static final Identifier PLAYERINFO_TEXTURE = CritalMod.id("textures/gui/container/player_info.png");
    private final int backgroundWidth = 256;
    private final int backgroundHeight = 256;

    public StatsScreen() {
        super(Text.literal("Bruh"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(PLAYERINFO_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
}
