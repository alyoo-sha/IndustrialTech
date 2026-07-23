package org.mod.industrialtech_ae.client.gui.element;

import appeng.api.stacks.AEItemKey;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class ItemSlotElement implements GuiElement {
    private final Rect rect;
    private final Supplier<AEItemKey> keySupplier;
    private final Supplier<List<Component>> tooltipSupplier;
    private final int acceptedButton;
    private final IntConsumer onClick;

    public ItemSlotElement(Rect rect, Supplier<AEItemKey> keySupplier, Supplier<List<Component>> tooltipSupplier, int acceptedButton, IntConsumer onClick) {
        this.rect = rect;
        this.keySupplier = keySupplier;
        this.tooltipSupplier = tooltipSupplier;
        this.acceptedButton = acceptedButton;
        this.onClick = onClick;
    }

    private ItemStack getStack() {
        AEItemKey key = this.keySupplier.get();
        if (key == null) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = key.toStack();
            return stack != null && !stack.isEmpty() ? stack : ItemStack.EMPTY;
        }
    }

    public void render(GuiGraphics guiGraphics, Font font, int leftPos, int topPos, int mouseX, int mouseY, float partialTick) {
        ItemStack stack = this.getStack();
        if (!stack.isEmpty()) {
            int x = this.rect.screenX(leftPos);
            int y = this.rect.screenY(topPos);
            guiGraphics.renderItem(stack, x, y);
            guiGraphics.renderItemDecorations(font, stack, x, y);
        }
    }

    public void renderTooltip(GuiGraphics guiGraphics, Font font, int leftPos, int topPos, int mouseX, int mouseY) {
        if (this.rect.contains(mouseX, mouseY, leftPos, topPos)) {
            if (this.tooltipSupplier != null) {
                List<Component> tooltip = this.tooltipSupplier.get();
                if (tooltip != null && !tooltip.isEmpty()) {
                    guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
                    return;
                }
            }

            ItemStack stack = this.getStack();
            if (!stack.isEmpty()) {
                guiGraphics.renderTooltip(font, stack, mouseX, mouseY);
            }

        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, int leftPos, int topPos) {
        if (!this.rect.contains(mouseX, mouseY, leftPos, topPos)) {
            return false;
        } else if (this.acceptedButton != -1 && button != this.acceptedButton) {
            return false;
        } else if (this.onClick != null) {
            this.onClick.accept(button);
            return true;
        } else {
            return false;
        }
    }
}
