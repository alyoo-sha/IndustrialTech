package org.mod.industrialtech_ae.client.gui.element;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface GuiElement {
    void render(GuiGraphics var1, Font var2, int var3, int var4, int var5, int var6, float var7);

    default void renderTooltip(GuiGraphics guiGraphics, Font font, int leftPos, int topPos, int mouseX, int mouseY) {
    }

    default boolean mouseClicked(double mouseX, double mouseY, int button, int leftPos, int topPos) {
        return false;
    }
}
