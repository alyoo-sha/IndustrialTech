package org.mod.industrialtech_ae.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.mod.industrialtech_ae.Industrialtech_ae;
import org.mod.industrialtech_ae.client.gui.element.*;
import org.mod.industrialtech_ae.init.AEModNetworking;
import org.mod.industrialtech_ae.menu.BulkFluidTankMenu;
import org.mod.industrialtech_ae.network.FilterInteractPacket;
import org.mod.industrialtech_ae.network.FluidInteractPacket;
import org.mod.industrialtech_ae.util.AmountFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class BulkFluidTankScreen extends AbstractContainerScreen<BulkFluidTankMenu> {
    private static final ResourceLocation BACKGROUND = Industrialtech_ae.makeId("textures/gui/fluid_tank.png");
    private static final Size BACKGROUND_SIZE = new Size(176, 186);
    private static final int GRAY = 11184810;
    private static final Rect TITLE_RECT = new Rect(14, 9, 152, 11);
    private static final Rect STATUS_RECT = new Rect(14, 25, 152, 11);
    private static final Rect FILTER_RECT = new Rect(14, 39, 152, 11);
    private static final Rect AMOUNT_RECT = new Rect(14, 53, 152, 11);
    private static final Rect FILTER_LABEL_RECT = new Rect(14, 72, 54, 11);
    private static final Rect FILTER_SLOT_RECT = new Rect(70, 72, 16, 16);
    private static final Rect FLUID_SLOT_RECT = new Rect(145, 72, 16, 16);
    private final List<GuiElement> elements = new ArrayList();

    public BulkFluidTankScreen(BulkFluidTankMenu menu, Inventory inventory, Component title) {
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
            if (((BulkFluidTankMenu)this.menu).isOnline()) {
                res.append(Component.translatable("gui.industrialtech_ae.online").withStyle(ChatFormatting.GREEN));
            } else {
                res.append(Component.translatable("gui.industrialtech_ae.offline").withStyle(ChatFormatting.RED));
            }

            return res;
        }, 11184810, false, TextAlignment.LEFT, (Supplier)null));
        this.elements.add(new TextElement(FILTER_RECT, () -> ((BulkFluidTankMenu)this.menu).getFilter() == null ? Component.translatable("gui.industrialtech_ae.no_filter").withStyle(ChatFormatting.RED) : Component.translatable("gui.industrialtech_ae.contains").withStyle(ChatFormatting.GRAY).append(Component.literal(": ").withStyle(ChatFormatting.GRAY)).append(((BulkFluidTankMenu)this.menu).getFilter().getDisplayName().copy().withStyle(ChatFormatting.GOLD)), 11184810, false, TextAlignment.LEFT, () -> ((BulkFluidTankMenu)this.menu).getFilter() != null ? Collections.singletonList(((BulkFluidTankMenu)this.menu).getFilter().getDisplayName().copy().withStyle(ChatFormatting.GOLD)) : null));
        this.elements.add(new TextElement(AMOUNT_RECT, () -> ((BulkFluidTankMenu)this.menu).getFluid() != null && ((BulkFluidTankMenu)this.menu).getAmount() != 0L ? Component.translatable("gui.industrialtech_ae.amount").withStyle(ChatFormatting.GRAY).append(Component.literal(": ").withStyle(ChatFormatting.GRAY)).append(AmountFormatter.formatFluid(((BulkFluidTankMenu)this.menu).getAmount(), true, ChatFormatting.LIGHT_PURPLE)) : Component.translatable("gui.industrialtech_ae.empty").withStyle(ChatFormatting.DARK_RED), 11184810, false, TextAlignment.LEFT, () -> ((BulkFluidTankMenu)this.menu).getFluid() != null && ((BulkFluidTankMenu)this.menu).getAmount() >= 0L ? Collections.singletonList(AmountFormatter.formatWithSpaces(((BulkFluidTankMenu)this.menu).getAmount(), ChatFormatting.LIGHT_PURPLE).append(" mb")) : null));
        this.elements.add(new TextElement(FILTER_LABEL_RECT, () -> Component.translatable("gui.industrialtech_ae.filter").append(":"), 11184810, false, TextAlignment.LEFT, (Supplier)null));
        List var10000 = this.elements;
        Rect var10003 = FILTER_SLOT_RECT;
        BulkFluidTankMenu var10004 = (BulkFluidTankMenu)this.menu;
        Objects.requireNonNull(var10004);
        var10000.add(new FluidSlotElement(var10003, var10004::getFilter, () -> ((BulkFluidTankMenu)this.menu).getFilter() != null ? Collections.singletonList(((BulkFluidTankMenu)this.menu).getFilter().getDisplayName().copy().withStyle(ChatFormatting.GOLD)) : Collections.singletonList(Component.translatable("gui.industrialtech_ae.no_filter").withStyle(ChatFormatting.RED)), 0, () -> AEModNetworking.sendToServer(new FilterInteractPacket(((BulkFluidTankMenu)this.menu).getBlockPos()))));
        this.elements.add(new FluidSlotElement(FLUID_SLOT_RECT, () -> ((BulkFluidTankMenu)this.menu).getAmount() > 0L ? ((BulkFluidTankMenu)this.menu).getFluid() : null, () -> ((BulkFluidTankMenu)this.menu).getFluid() != null && ((BulkFluidTankMenu)this.menu).getAmount() > 0L ? List.of(((BulkFluidTankMenu)this.menu).getFluid().getDisplayName().copy().withStyle(ChatFormatting.GOLD), AmountFormatter.formatFluid(((BulkFluidTankMenu)this.menu).getAmount(), true, ChatFormatting.LIGHT_PURPLE)) : Collections.singletonList(Component.translatable("gui.industrialtech_ae.empty").withStyle(ChatFormatting.DARK_RED)), 0, () -> AEModNetworking.sendToServer(new FluidInteractPacket(((BulkFluidTankMenu)this.menu).getBlockPos()))));
    }

    private boolean isCustomTooltipArea(int mouseX, int mouseY) {
        return FILTER_SLOT_RECT.contains((double)mouseX, (double)mouseY, this.leftPos, this.topPos) || FLUID_SLOT_RECT.contains((double)mouseX, (double)mouseY, this.leftPos, this.topPos);
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
