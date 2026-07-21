package org.mod.industrialtech_ae.network;

import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidInteractPacket {
    private final BlockPos pos;

    public FluidInteractPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(org.mod.industrialtech_ae.network.FluidInteractPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static org.mod.industrialtech_ae.network.FluidInteractPacket decode(FriendlyByteBuf buf) {
        return new org.mod.industrialtech_ae.network.FluidInteractPacket(buf.readBlockPos());
    }

    public static void handle(org.mod.industrialtech_ae.network.FluidInteractPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            ServerPlayer player = ((NetworkEvent.Context)ctx.get()).getSender();
            if (player != null) {
                if (!(player.distanceToSqr((double)msg.pos.getX() + (double)0.5F, (double)msg.pos.getY() + (double)0.5F, (double)msg.pos.getZ() + (double)0.5F) > (double)64.0F)) {
                    ItemStack carriedStack = player.containerMenu.getCarried();
                    boolean isCarried = !carriedStack.isEmpty();
                    if (carriedStack.isEmpty()) {
                        carriedStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                    }

                    if (!carriedStack.isEmpty()) {
                        if (carriedStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
                            BlockEntity patt2100$temp = player.level().getBlockEntity(msg.pos);
                            if (patt2100$temp instanceof BulkFluidTankBlockEntity) {
                                BulkFluidTankBlockEntity be = (BulkFluidTankBlockEntity)patt2100$temp;
                                ItemStack finalCarriedStack = carriedStack;
                                be.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent((handler) -> {
                                    ItemStack singleItem = finalCarriedStack.copy();
                                    singleItem.setCount(1);
                                    boolean changed = false;
                                    FluidActionResult emptyResult = FluidUtil.tryEmptyContainer(singleItem, handler, Integer.MAX_VALUE, player, true);
                                    if (emptyResult.isSuccess()) {
                                        processStack(player, finalCarriedStack, emptyResult.getResult(), isCarried);
                                        changed = true;
                                    } else {
                                        FluidActionResult fillResult = FluidUtil.tryFillContainer(singleItem, handler, Integer.MAX_VALUE, player, true);
                                        if (fillResult.isSuccess()) {
                                            processStack(player, finalCarriedStack, fillResult.getResult(), isCarried);
                                            changed = true;
                                        }
                                    }

                                    if (changed) {
                                        be.onStorageChanged();
                                        player.containerMenu.broadcastChanges();
                                    }

                                });
                            }
                        }
                    }
                }
            }
        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }

    private static void processStack(ServerPlayer player, ItemStack originalStack, ItemStack resultItem, boolean wasCarried) {
        if (originalStack.getCount() == 1) {
            if (wasCarried) {
                player.containerMenu.setCarried(resultItem);
            } else {
                player.setItemInHand(InteractionHand.MAIN_HAND, resultItem);
            }

        } else {
            originalStack.shrink(1);
            if (!resultItem.isEmpty()) {
                ItemHandlerHelper.giveItemToPlayer(player, resultItem);
            }

            if (!wasCarried) {
                player.setItemInHand(InteractionHand.MAIN_HAND, originalStack);
            }

        }
    }
}
