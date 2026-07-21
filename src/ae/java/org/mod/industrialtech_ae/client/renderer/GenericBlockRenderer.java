package org.mod.industrialtech_ae.client.renderer;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.mod.industrialtech_ae.Industrialtech_ae;
import org.mod.industrialtech_ae.entity.BaseBlockEntity;
import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import org.mod.industrialtech_ae.util.AmountFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;

public class GenericBlockRenderer implements BlockEntityRenderer<BaseBlockEntity<?>> {
    private static final ResourceLocation ACTIVE_OVERLAY = Industrialtech_ae.makeId("block/block_frame");
    private static final ResourceLocation OVERLAY = Industrialtech_ae.makeId("block/block_frame_base");
    private static final ResourceLocation BLOCK_GLASS = Industrialtech_ae.makeId("block/block_glass");
    private static final int FULL_BRIGHT = 15728880;

    public GenericBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(BaseBlockEntity<?> be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction facing = (Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            AEKey filterKey = be.getStorage().getFilterKey();
            poseStack.pushPose();
            poseStack.translate((double)0.5F, (double)0.5F, (double)0.5F);
            float angle = 180.0F - facing.toYRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
            poseStack.translate((double)-0.5F, (double)-0.5F, (double)-0.5F);
            if (filterKey != null) {
                if (be instanceof BulkFluidTankBlockEntity && filterKey instanceof AEFluidKey) {
                    AEFluidKey fluidKey = (AEFluidKey)filterKey;
                    this.renderFluid(fluidKey.getFluid(), poseStack, buffer);
                } else if (be instanceof BulkItemChestBlockEntity && filterKey instanceof AEItemKey) {
                    AEItemKey itemKey = (AEItemKey)filterKey;
                    this.renderItem(be, itemKey, partialTick, poseStack, buffer);
                }
            }

            this.renderQuad(poseStack, buffer, OVERLAY, 0.0625F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, packedLight);
            this.renderQuad(poseStack, buffer, BLOCK_GLASS, 0.061875F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 15728880);
            if (be.isOnline()) {
                this.renderQuad(poseStack, buffer, ACTIVE_OVERLAY, 0.06125F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 15728880);
            }

            if (filterKey != null) {
                this.renderText(be, poseStack, buffer);
            }

            poseStack.popPose();
        }
    }

    private void renderFluid(Fluid fluid, PoseStack poseStack, MultiBufferSource buffer) {
        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(fluid);
        TextureAtlasSprite sprite = this.getSprite(fluidExt.getStillTexture());
        int tint = fluidExt.getTintColor();
        float r = (float)(tint >> 16 & 255) / 255.0F;
        float g = (float)(tint >> 8 & 255) / 255.0F;
        float b = (float)(tint & 255) / 255.0F;
        this.renderQuad(poseStack, buffer, sprite, 0.063125F, 0.93125F, 0.06875F, r, g, b, 1.0F, 15728880);
    }

    private void renderItem(BaseBlockEntity<?> be, AEItemKey filterKey, float partialTick, PoseStack poseStack, MultiBufferSource buffer) {
        AEKey storedKey = be.getStorage().getStoredKey();
        ItemStack var10000;
        if (storedKey instanceof AEItemKey item) {
            var10000 = item.toStack();
        } else {
            var10000 = filterKey.toStack();
        }

        ItemStack stack = var10000;
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate((double)0.5F, (double)0.5F, (double)0.5F);
            poseStack.scale(0.7F, 0.7F, 0.7F);
            long timeBase = be.getLevel() != null ? be.getLevel().getGameTime() : Minecraft.getInstance().level.getGameTime();
            float rotation = ((float)timeBase + partialTick) * 2.0F;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, poseStack, buffer, be.getLevel(), 0);
            poseStack.popPose();
        }
    }

    private void renderText(BaseBlockEntity<?> be, PoseStack poseStack, MultiBufferSource buffer) {
        Font font = Minecraft.getInstance().font;
        long amount = be.getStorage().getAmount();
        Component text;
        if (be instanceof BulkFluidTankBlockEntity) {
            text = AmountFormatter.formatFluid(amount, false, ChatFormatting.WHITE);
        } else {
            text = AmountFormatter.formatItem(amount, false, ChatFormatting.WHITE);
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.05F);
        float scale = 0.015F;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        float xOffset = (float)(-font.width(text) / 2);
        font.drawInBatch(text, xOffset, -4.0F, -1, false, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        poseStack.popPose();
    }

    private void renderQuad(PoseStack poseStack, MultiBufferSource buffer, Object texture, float z, float size, float offset, float r, float g, float b, float a, int brg) {
        TextureAtlasSprite var10000;
        if (texture instanceof ResourceLocation loc) {
            var10000 = this.getSprite(loc);
        } else {
            var10000 = (TextureAtlasSprite)texture;
        }

        TextureAtlasSprite sprite = var10000;
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
        Matrix4f pose = poseStack.last().pose();
        float maxPos = 1.0F - offset;
        vc.vertex(pose, maxPos, offset, z).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(brg).normal(0.0F, 1.0F, 0.0F).endVertex();
        vc.vertex(pose, offset, offset, z).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(brg).normal(0.0F, 1.0F, 0.0F).endVertex();
        vc.vertex(pose, offset, maxPos, z).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(brg).normal(0.0F, 1.0F, 0.0F).endVertex();
        vc.vertex(pose, maxPos, maxPos, z).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(brg).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private TextureAtlasSprite getSprite(ResourceLocation loc) {
        return (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(loc);
    }
}
