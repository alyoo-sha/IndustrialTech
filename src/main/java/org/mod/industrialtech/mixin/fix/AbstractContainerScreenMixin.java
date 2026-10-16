package org.mod.industrialtech.mixin.fix;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void enableBlend(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void disableBlend(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {

        RenderSystem.disableBlend();
    }
}