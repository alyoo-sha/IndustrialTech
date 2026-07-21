package org.mod.industrialtech_ae.client.gui;

import org.mod.industrialtech_ae.Industrialtech_ae;
import org.mod.industrialtech_ae.client.gui.element.*;
import org.mod.industrialtech_ae.init.AEModNetworking;
import org.mod.industrialtech_ae.menu.BulkItemChestMenu;
import org.mod.industrialtech_ae.network.ItemFilterInteractPacket;
import org.mod.industrialtech_ae.network.ItemInteractPacket;
import org.mod.industrialtech_ae.util.AmountFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class BulkItemChestScreen extends AbstractContainerScreen<BulkItemChestMenu> {
    private static final ResourceLocation BACKGROUND = Industrialtech_ae.makeId("textures/gui/item_chest.png");
    private static final Size BACKGROUND_SIZE = new Size(176, 186);
    private static final int GRAY = 11184810;
    private static final Rect TITLE_RECT = new Rect(14, 9, 152, 11);
    private static final Rect STATUS_RECT = new Rect(14, 25, 152, 11);
    private static final Rect FILTER_RECT = new Rect(14, 39, 152, 11);
    private static final Rect AMOUNT_RECT = new Rect(14, 53, 152, 11);
    private static final Rect FILTER_LABEL_RECT = new Rect(14, 72, 54, 11);
    private static final Rect FILTER_SLOT_RECT = new Rect(70, 72, 16, 16);
    private static final Rect ITEM_SLOT_RECT = new Rect(145, 72, 16, 16);
    private final List<GuiElement> elements = new ArrayList();

    public BulkItemChestScreen(BulkItemChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventoryLabelX = -1000;
        this.inventoryLabelY = -1000;
        this.imageWidth = BACKGROUND_SIZE.width;
        this.imageHeight = BACKGROUND_SIZE.height;
    }

    protected void init() {
        super.init();
        this.elements.clear();
        this.elements.add(new TextElement(TITLE_RECT, () -> this.title, 16777215, false, TextAlignment.CENTER, (Supplier)null));
        this.elements.add(new TextElement(STATUS_RECT, () -> {
            MutableComponent res = Component.translatable("gui.industrialtech_ae.status").append(": ");
            if (((BulkItemChestMenu)this.menu).isOnline()) {
                res.append(Component.translatable("gui.industrialtech_ae.online").withStyle(ChatFormatting.GREEN));
            } else {
                res.append(Component.translatable("gui.industrialtech_ae.offline").withStyle(ChatFormatting.RED));
            }

            return res;
        }, 11184810, false, TextAlignment.LEFT, (Supplier)null));
        this.elements.add(new TextElement(FILTER_RECT, () -> ((BulkItemChestMenu)this.menu).getFilter() == null ? Component.translatable("gui.industrialtech_ae.no_filter").withStyle(ChatFormatting.RED) : Component.translatable("gui.industrialtech_ae.contains").withStyle(ChatFormatting.GRAY).append(Component.literal(": ").withStyle(ChatFormatting.GRAY)).append(((BulkItemChestMenu)this.menu).getFilter().getDisplayName().copy().withStyle(ChatFormatting.GREEN)), 11184810, false, TextAlignment.LEFT, () -> ((BulkItemChestMenu)this.menu).getFilter() != null ? Collections.singletonList(((BulkItemChestMenu)this.menu).getFilter().getDisplayName().copy().withStyle(ChatFormatting.GREEN)) : null));
        this.elements.add(new TextElement(AMOUNT_RECT, () -> ((BulkItemChestMenu)this.menu).getItem() != null && ((BulkItemChestMenu)this.menu).getAmount() != 0L ? Component.translatable("gui.industrialtech_ae.amount").withStyle(ChatFormatting.GRAY).append(Component.literal(": ").withStyle(ChatFormatting.GRAY)).append(AmountFormatter.formatItem(((BulkItemChestMenu)this.menu).getAmount(), true, ChatFormatting.AQUA)) : Component.translatable("gui.industrialtech_ae.empty").withStyle(ChatFormatting.DARK_RED), 11184810, false, TextAlignment.LEFT, () -> ((BulkItemChestMenu)this.menu).getItem() != null && ((BulkItemChestMenu)this.menu).getAmount() >= 0L ? Collections.singletonList(AmountFormatter.formatWithSpaces(((BulkItemChestMenu)this.menu).getAmount(), ChatFormatting.AQUA)) : null));
        this.elements.add(new TextElement(FILTER_LABEL_RECT, () -> Component.translatable("gui.industrialtech_ae.filter").append(":"), 11184810, false, TextAlignment.LEFT, (Supplier)null));
        List var10000 = this.elements;
        Rect var10003 = FILTER_SLOT_RECT;
        BulkItemChestMenu var10004 = (BulkItemChestMenu)this.menu;
        Objects.requireNonNull(var10004);
        var10000.add(new ItemSlotElement(var10003, var10004::getFilter, () -> ((BulkItemChestMenu)this.menu).getFilter() != null ? Collections.singletonList(((BulkItemChestMenu)this.menu).getFilter().getDisplayName().copy().withStyle(ChatFormatting.GREEN)) : Collections.singletonList(Component.translatable("gui.industrialtech_ae.no_filter").withStyle(ChatFormatting.RED)), 0, (button) -> AEModNetworking.sendToServer(new ItemFilterInteractPacket(((BulkItemChestMenu)this.menu).getBlockPos()))));
        this.elements.add(new ItemSlotElement(ITEM_SLOT_RECT, () -> ((BulkItemChestMenu)this.menu).getAmount() > 0L ? ((BulkItemChestMenu)this.menu).getItem() : null, () -> ((BulkItemChestMenu)this.menu).getItem() != null && ((BulkItemChestMenu)this.menu).getAmount() > 0L ? List.of(((BulkItemChestMenu)this.menu).getItem().getDisplayName().copy().withStyle(ChatFormatting.GREEN), AmountFormatter.formatItem(((BulkItemChestMenu)this.menu).getAmount(), true, ChatFormatting.AQUA)) : Collections.singletonList(Component.translatable("gui.industrialtech_ae.empty").withStyle(ChatFormatting.DARK_RED)), -1, (button) -> AEModNetworking.sendToServer(new ItemInteractPacket(((BulkItemChestMenu)this.menu).getBlockPos(), button == 1))));
    }

    private boolean isCustomTooltipArea(int mouseX, int mouseY) {
        return FILTER_SLOT_RECT.contains((double)mouseX, (double)mouseY, this.leftPos, this.topPos) || ITEM_SLOT_RECT.contains((double)mouseX, (double)mouseY, this.leftPos, this.topPos);
    }

    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for(GuiElement element : this.elements) {
            element.render(guiGraphics, this.font, this.leftPos, this.topPos, mouseX, mouseY, partialTick);
        }

        for(GuiElement element : this.elements) {
            element.renderTooltip(guiGraphics, this.font, this.leftPos, this.topPos, mouseX, mouseY);
        }

        if (!this.isCustomTooltipArea(mouseX, mouseY)) {
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }

    }

    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, BACKGROUND_SIZE.width, BACKGROUND_SIZE.height);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(GuiElement element : this.elements) {
            if (element.mouseClicked(mouseX, mouseY, button, this.leftPos, this.topPos)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}
