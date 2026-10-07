package org.mod.industrialtech.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.mod.industrialtech.client.ModClientForgeEvents;
import org.mod.industrialtech.client.render.ModBlurConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @ModifyConstant(method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;)V", constant = @Constant(intValue = -804253680))
    private int secondValues(int constant){
        if (ModBlurConfig.guiExlusions.contains(this.getClass().getName())) return 0;
        return ModClientForgeEvents.getBackgroundColor(true);
    }

    @ModifyConstant(method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;)V", constant = @Constant(intValue = -1072689136))
    private int firstValues(int constant){
        if (ModBlurConfig.guiExlusions.contains(this.getClass().getName())) return 0;
        return ModClientForgeEvents.getBackgroundColor(false);
    }
    @Inject(method = "init", at = @At("TAIL"))
    private void addSettingsButton(CallbackInfo ci) {

        Screen screen = (Screen)(Object)this;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return; // только в игре

//        screen.addRenderableWidget(
//                Button.builder(Component.literal("⚙"), btn -> {
//                    mc.setScreen(new ModBlurSettingsScreen());
//                }).bounds(5, 5, 20, 20).build()
//        );
    }
}
