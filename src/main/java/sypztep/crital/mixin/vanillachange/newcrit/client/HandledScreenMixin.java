package sypztep.crital.mixin.vanillachange.newcrit.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.util.BorderHandler;
import sypztep.crital.common.util.CritalDataUtil;

import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "drawMouseoverTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    protected void drawMouseoverTooltipMixin(DrawContext context, int x, int y, CallbackInfo info, ItemStack stack) {
        if (CritalDataUtil.getNbtCompoundFromStack(stack).contains(CritData.TIER_FLAG) && client != null) {
            List<Text> text = Screen.getTooltipFromItem(client, stack);

            List<TooltipComponent> list = text.stream().map(Text::asOrderedText).map(TooltipComponent::of).collect(Collectors.toList());
            stack.getTooltipData().ifPresent(data -> list.add(1, TooltipComponent.of(data)));

            CritTier critTier = CritalDataUtil.getCritTierFromStack(stack);

            BorderHandler.renderTieredTooltipFromComponents(context, this.textRenderer, list, x, y, HoveredTooltipPositioner.INSTANCE, critTier);
            info.cancel();
        }
    }
}
