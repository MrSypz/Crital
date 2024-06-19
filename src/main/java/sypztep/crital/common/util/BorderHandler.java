package sypztep.crital.common.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import sypztep.crital.common.api.crital.BorderTemplate;
import sypztep.crital.common.data.CritTier;

import java.util.List;

import static net.minecraft.client.render.VertexFormats.POSITION_COLOR;

@Environment(EnvType.CLIENT)
public class BorderHandler {
    public static void renderTieredTooltipFromComponents(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CritTier critTier) {
        if (components.isEmpty()) {
            return;
        }

        BorderTemplate borderTemplate = critTier.getBorderTemplate();
        int bgstar = borderTemplate.backgroundStartColor();
        int bgend = borderTemplate.backgroundEndColor();
        int colorStart = borderTemplate.colorStart();
        int colorEnd = borderTemplate.colorEnd();
        Identifier identifier = borderTemplate.identifier();

        int maxWidth = Math.max(components.stream().mapToInt(component -> component != null ? component.getWidth(textRenderer) : 0).max().orElse(0), 64);
        int totalHeight = components.size() == 1 ? -2 : Math.max(components.stream().mapToInt(TooltipComponent::getHeight).sum(), 16);

        Vector2ic position = positioner.getPosition(context.getScaledWindowWidth(), context.getScaledWindowHeight(), x, y, maxWidth, totalHeight);
        int posX = position.x();
        int posY = position.y();

        context.getMatrices().push();
        renderTooltipBackground(context, posX, posY, maxWidth, totalHeight, bgstar, bgend, colorStart, colorEnd);
        context.getMatrices().translate(0.0f, 0.0f, 400.0f);

        int currentY = posY;
        for (TooltipComponent component : components) {
            if (component != null) {
                component.drawText(textRenderer, posX, currentY, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers());
                component.drawItems(textRenderer, posX, currentY, context);
                currentY += component.getHeight() + (components.indexOf(component) == 0 ? 2 : 0);
            }
        }

        int borderIndex = critTier.ordinal();

        context.getMatrices().translate(0.0f, 0.0f, 400.0f);
        context.drawTexture(identifier, posX - 6, posY - 6, 0, borderIndex * 16, 8, 8, 128, 128);
        context.drawTexture(identifier, posX + maxWidth - 2, posY - 6, 56, borderIndex * 16, 8, 8, 128, 128);
        context.drawTexture(identifier, posX - 6, posY + totalHeight - 2, 0, 8 + borderIndex * 16, 8, 8, 128, 128);
        context.drawTexture(identifier, posX + maxWidth - 2, posY + totalHeight - 2, 56, 8 + borderIndex * 16, 8, 8, 128, 128);
        context.drawTexture(identifier, (posX - 6 + posX + maxWidth + 6) / 2 - 24, posY - 9, 8, borderIndex * 16, 48, 8, 128, 128);
        context.drawTexture(identifier, (posX - 6 + posX + maxWidth + 6) / 2 - 24, posY + totalHeight + 1, 8, 8 + borderIndex * 16, 48, 8, 128, 128);

        context.getMatrices().pop();
    }

    private static void renderTooltipBackground(DrawContext context, int x, int y, int width, int height, int bgstar,int bgend, int colorStart, int colorEnd) {
        int i = x - 3;
        int j = y - 3;
        int k = width + 6;
        int l = height + 6;
        renderHorizontalLine(context, i, j - 1, k, 400, bgstar);
        renderHorizontalLine(context, i, j + l, k, 400, bgstar);

        renderRectangleBackground(context, i, j, k, l, 400, bgstar, bgend);

        renderUnderLineText(context.getMatrices(),i + 2,j + 13,width - 2, colorStart);

        renderVerticalLine(context, i - 1, j, l, 400, bgstar);
        renderVerticalLine(context, i + k, j, l, 400, bgstar);
        renderBorder(context, i, j + 1, k, l, 400, colorStart, colorEnd);
    }

    private static void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int startColor, int endColor) {
        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int startColor) {
        context.fill(x, y, x + width, y + 1, z, startColor);
    }

    private static void renderRectangleBackground(DrawContext context, int x, int y, int width, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + width, y + height, z, startColor,endColor);
    }
    public static void renderUnderLineText(MatrixStack stack, int x, int y, int width, int color) {
        stack.push();
        Matrix4f matrix = stack.peek().getPositionMatrix();
        drawGradientRectHorizontal(matrix, 402, x, y, x + width / 2, y + 1, color & 0xFFFFFF, color);
        drawGradientRectHorizontal(matrix, 402, x + width / 2, y, x + width, y + 1, color, color & 0xFFFFFF);
        stack.pop();
    }

    public static void drawGradientRectHorizontal(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed   = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >>  8 & 255) / 255.0F;
        float startBlue  = (float)(startColor       & 255) / 255.0F;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255.0F;
        float endRed     = (float)(endColor   >> 16 & 255) / 255.0F;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255.0F;
        float endBlue    = (float)(endColor         & 255) / 255.0F;

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, POSITION_COLOR);
        bufferBuilder.vertex(mat, right,    top, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha);
        bufferBuilder.vertex(mat,  left,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha);
        bufferBuilder.vertex(mat,  left, bottom, zLevel).color(startRed, startGreen, startBlue, startAlpha);
        bufferBuilder.vertex(mat, right, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha);
        BufferRenderer.drawWithGlobalProgram((bufferBuilder.build()));

        RenderSystem.disableBlend();
    }

}
