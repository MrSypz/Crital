package sypztep.crital.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.packetc2s.GrindQualityPayloadC2S;
import sypztep.crital.common.packetc2s.GrinderPayloadC2S;
import sypztep.crital.common.screen.GrinderScreenHandler;

public class GrinderScreen
        extends HandledScreen<GrinderScreenHandler>
        implements ScreenHandlerListener {
    public static final Identifier TEXTURE = CritalMod.id("textures/gui/container/grinder_screen.png");
    public GrinderScreen.GrindButton grindButton;
    public GrinderScreen.QualityButton qualityButton;

    public GrinderScreen(GrinderScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, Text.translatable(CritalMod.MODID + ".grinder_screen"));
        this.titleX = 60;
    }

    @Override
    protected void init() {
        super.init();
        this.handler.addListener(this);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.grindButton = this.addDrawableChild(new GrinderScreen.GrindButton(i + 74, j + 56, (button) -> {
            if (button instanceof GrinderScreen.GrindButton && !((GrinderScreen.GrindButton) button).disabled)
                GrinderPayloadC2S.send(new GrinderPayloadC2S(true));
        }));
        this.qualityButton = this.addDrawableChild(new GrinderScreen.QualityButton(i + 124, j + 56, (button) -> {
            if (button instanceof GrinderScreen.QualityButton && !((GrinderScreen.QualityButton) button).disabled)
                GrindQualityPayloadC2S.send(new GrindQualityPayloadC2S(true));
        }));
    }

    @Override
    public void removed() {
        super.removed();
        this.handler.removeListener(this);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        RenderSystem.disableBlend();

        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
    }

    public static class GrindButton extends ButtonWidget {
        private boolean disabled;

        public GrindButton(int x, int y, ButtonWidget.PressAction onPress) {
            super(x, y, 36, 18, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
            this.disabled = true;
        }
        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int v = 0;
            if (this.disabled) {
                v += this.height * 2;
            } else if (this.isHovered()) {
                v += this.height;
            }
            context.drawTexture(TEXTURE, this.getX(), this.getY(), 176, v, this.width, this.height);
        }
        public void setDisabled(boolean disable) {
            this.disabled = disable;
        }
    }
    public static class QualityButton extends ButtonWidget {
        private boolean disabled;

        public QualityButton(int x, int y, ButtonWidget.PressAction onPress) {
            super(x, y, 36, 18, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
            this.disabled = true;
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int v = 0;
            if (this.disabled) {
                v += this.height * 2;
            } else if (this.isHovered()) {
                v += this.height;
            }
            context.drawTexture(TEXTURE, this.getX(), this.getY(), 176, v, this.width, this.height);
        }
        public void setDisabled(boolean disable) {
            this.disabled = disable;
        }
    }
}
