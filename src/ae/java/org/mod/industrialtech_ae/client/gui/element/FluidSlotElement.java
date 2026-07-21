package org.mod.industrialtech_ae.client.gui.element;

import appeng.api.stacks.AEFluidKey;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FluidSlotElement implements GuiElement {
    private final Rect rect;
    private final Supplier<AEFluidKey> fluidSupplier;
    private final Supplier<List<Component>> tooltipSupplier;
    private final int acceptedButton;
    private final Runnable onClick;

    public FluidSlotElement(Rect rect, Supplier<AEFluidKey> fluidSupplier, Supplier<List<Component>> tooltipSupplier, int acceptedButton, Runnable onClick) {
        this.rect = rect;
        this.fluidSupplier = fluidSupplier;
        this.tooltipSupplier = tooltipSupplier;
        this.acceptedButton = acceptedButton;
        this.onClick = onClick;
    }

    public void render(GuiGraphics guiGraphics, Font font, int leftPos, int topPos, int mouseX, int mouseY, float partialTick) {
        AEFluidKey fluid = (AEFluidKey)this.fluidSupplier.get();
        if (fluid != null) {
            FluidStack fluidStack = new FluidStack(fluid.getFluid(), 1);
            TextureAtlasSprite sprite = (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getStillTexture(fluidStack));
            int tint = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            guiGraphics.setColor((float)(tint >> 16 & 255) / 255.0F, (float)(tint >> 8 & 255) / 255.0F, (float)(tint & 255) / 255.0F, (float)(tint >> 24 & 255) / 255.0F);
            guiGraphics.blit(this.rect.screenX(leftPos), this.rect.screenY(topPos), 0, this.rect.width, this.rect.height, sprite);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void renderTooltip(GuiGraphics guiGraphics, Font font, int leftPos, int topPos, int mouseX, int mouseY) {
        if (this.tooltipSupplier != null && this.rect.contains((double)mouseX, (double)mouseY, leftPos, topPos)) {
            List<Component> tooltip = (List)this.tooltipSupplier.get();
            if (tooltip != null && !tooltip.isEmpty()) {
                guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY);
            }

        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, int leftPos, int topPos) {
        if (button == this.acceptedButton && this.rect.contains(mouseX, mouseY, leftPos, topPos)) {
            if (this.onClick != null) {
                this.onClick.run();
            }

            return true;
        } else {
            return false;
        }
    }
}
