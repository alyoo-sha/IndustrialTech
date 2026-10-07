package org.mod.industrialtech.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TexturedButton extends Button {
    private final ResourceLocation texture;
    private final int texWidth;
    private final int texHeight;
    private final int texU;
    private final int texV;
    private final int texUDark;  // Координаты затемненной версии
    private final int texVDark;

    public TexturedButton(int x, int y, int width, int height, ResourceLocation texture, int texWidth, int texHeight, int texU, int texV, int texUDark, int texVDark, Button.OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.texU = texU;
        this.texV = texV;
        this.texUDark = texUDark;
        this.texVDark = texVDark;
    }

    @Override
    public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        // 🎨 Выбираем координаты в зависимости от состояния
        int u = this.isHovered ? this.texUDark : this.texU;
        int v = this.isHovered ? this.texVDark : this.texV;
        float alpha = this.isHovered ? 1.0f : 0.7f;

        // 🎨 Рисуем текстуру с прозрачностью
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

        gui.blit(
                this.texture,
                this.getX(),
                this.getY(),
                u,
                v,
                this.getWidth(),
                this.getHeight(),
                this.texWidth,
                this.texHeight
        );

        // 🎨 Восстанавливаем нормальное состояние
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }
}
