package org.mod.industrialtech_ae.client.gui.element;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class TextElement implements GuiElement {
    private final Rect rect;
    private final Supplier<Component> textSupplier;
    private final int color;
    private final boolean shadow;
    private final TextAlignment alignment;
    private final Supplier<List<Component>> tooltipSupplier;

    public TextElement(Rect rect, Supplier<Component> textSupplier, int color, boolean shadow, TextAlignment alignment, Supplier<List<Component>> tooltipSupplier) {
        this.rect = rect;
        this.textSupplier = textSupplier;
        this.color = color;
        this.shadow = shadow;
        this.alignment = alignment;
        this.tooltipSupplier = tooltipSupplier;
    }

    public void render(GuiGraphics guiGraphics, Font font, int leftPos, int topPos, int mouseX, int mouseY, float partialTick) {
        Component text = trimFormattedTextToFit(font, this.textSupplier.get(), this.rect.width());
        int sx = this.rect.screenX(leftPos);
        int sy = this.rect.screenY(topPos);
        int textWidth = font.width(text);
        int var10000;
        switch (this.alignment) {
            case LEFT -> var10000 = sx;
            case CENTER -> var10000 = sx + (this.rect.width() - textWidth) / 2;
            case RIGHT -> var10000 = sx + this.rect.width() - textWidth;
            default -> throw new IncompatibleClassChangeError();
        }

        int textX = var10000;
        int var10001 = this.rect.height();
        Objects.requireNonNull(font);
        int textY = sy + (var10001 - 9) / 2 + 1;
        guiGraphics.drawString(font, text, textX, textY, this.color, this.shadow);
    }

    public void renderTooltip(GuiGraphics guiGraphics, Font font, int leftPos, int topPos, int mouseX, int mouseY) {
        if (this.tooltipSupplier != null && this.rect.contains(mouseX, mouseY, leftPos, topPos)) {
            List<Component> tooltip = this.tooltipSupplier.get();
            if (tooltip != null && !tooltip.isEmpty()) {
                guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
            }

        }
    }

    private static MutableComponent trimFormattedTextToFit(Font font, Component text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text.copy();
        } else {
            String ellipsis = "...";
            int ellipsisWidth = font.width(ellipsis);
            if (maxWidth <= ellipsisWidth) {
                return Component.literal(ellipsis).withStyle(text.getStyle());
            } else {
                int availableWidth = maxWidth - ellipsisWidth;
                FormattedText trimmedText = font.substrByWidth(text, availableWidth);
                MutableComponent result = Component.empty();
                Style[] lastStyle = new Style[]{text.getStyle()};
                trimmedText.visit((style, part) -> {
                    if (!part.isEmpty()) {
                        result.append(Component.literal(part).withStyle(style));
                        lastStyle[0] = style;
                    }

                    return Optional.empty();
                }, Style.EMPTY);
                result.append(Component.literal(ellipsis).withStyle(lastStyle[0]));
                return result;
            }
        }
    }
}
