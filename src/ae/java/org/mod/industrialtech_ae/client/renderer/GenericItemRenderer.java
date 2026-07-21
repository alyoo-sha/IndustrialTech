package org.mod.industrialtech_ae.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import org.mod.industrialtech_ae.block.BaseBlock;
import org.mod.industrialtech_ae.entity.BaseBlockEntity;
import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import org.mod.industrialtech_ae.init.AEModBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class GenericItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final BulkFluidTankBlockEntity fluidDummy;
    private final BulkItemChestBlockEntity itemDummy;

    public GenericItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.fluidDummy = new BulkFluidTankBlockEntity(BlockPos.ZERO, ((Block)AEModBlock.BULK_FLUID_TANK.get()).defaultBlockState());
        this.itemDummy = new BulkItemChestBlockEntity(BlockPos.ZERO, ((Block)AEModBlock.BULK_ITEM_CHEST.get()).defaultBlockState());
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Item var8 = stack.getItem();
        if (var8 instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            if (state.hasProperty(BaseBlock.FACING)) {
                state = state.setValue(BaseBlock.FACING, Direction.NORTH);
            }

            BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
            poseStack.pushPose();
            poseStack.translate((double)0.5F, (double)0.5F, (double)0.5F);
            poseStack.translate((double)-0.5F, (double)-0.5F, (double)-0.5F);
            blockRenderer.renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay, ModelData.EMPTY, (RenderType)null);
            poseStack.popPose();
            if (blockItem.getBlock() == AEModBlock.BULK_FLUID_TANK.get()) {
                this.renderInternal(this.fluidDummy, stack, poseStack, buffer, packedLight, packedOverlay);
            } else if (blockItem.getBlock() == AEModBlock.BULK_ITEM_CHEST.get()) {
                this.renderInternal(this.itemDummy, stack, poseStack, buffer, packedLight, packedOverlay);
            }

        }
    }

    private void renderInternal(BaseBlockEntity<?> dummyBe, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (Minecraft.getInstance().level != null) {
            dummyBe.setLevel(Minecraft.getInstance().level);
        }

        if (stack.hasTag()) {
            dummyBe.load(stack.getTag());
        } else {
            dummyBe.getStorage().load(new CompoundTag());
        }

        BlockEntityRenderer<? extends BaseBlockEntity<?>> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(dummyBe);
        if (renderer != null) {
            poseStack.pushPose();
            renderHelper(renderer, dummyBe, poseStack, buffer, packedLight, packedOverlay);
            poseStack.popPose();
        }

    }

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> void renderHelper(BlockEntityRenderer<?> renderer, T dummyBe, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ((BlockEntityRenderer<T>)renderer).render(dummyBe, 0.0F, poseStack, buffer, packedLight, packedOverlay);
    }
}
