package org.mod.industrialtech.mixin.blur;

import net.minecraft.client.gui.screens.Screen;
import org.mod.industrialtech.client.IClientForgeEvents;
import org.mod.industrialtech.client.render.IBlurConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @ModifyConstant(method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;)V", constant = @Constant(intValue = -804253680))
    private int secondValues(int constant){
        if (IBlurConfig.guiExlusions.contains(this.getClass().getName())) return 0;
        return IClientForgeEvents.getBackgroundColor(true);
    }

    @ModifyConstant(method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;)V", constant = @Constant(intValue = -1072689136))
    private int firstValues(int constant){
        if (IBlurConfig.guiExlusions.contains(this.getClass().getName())) return 0;
        return IClientForgeEvents.getBackgroundColor(false);
    }
}
