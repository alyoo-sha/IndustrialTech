package org.mod.industrialtech_ae.network;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkEvent;
import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;

import java.util.function.Supplier;

public class FilterInteractPacket {
    private final BlockPos pos;

    public FilterInteractPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(org.mod.industrialtech_ae.network.FilterInteractPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static org.mod.industrialtech_ae.network.FilterInteractPacket decode(FriendlyByteBuf buf) {
        return new org.mod.industrialtech_ae.network.FilterInteractPacket(buf.readBlockPos());
    }

    public static void handle(org.mod.industrialtech_ae.network.FilterInteractPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            ServerPlayer player = ((NetworkEvent.Context)ctx.get()).getSender();
            if (player != null) {
                if (!(player.distanceToSqr((double)msg.pos.getX() + (double)0.5F, (double)msg.pos.getY() + (double)0.5F, (double)msg.pos.getZ() + (double)0.5F) > (double)64.0F)) {
                    BlockEntity patt1482$temp = player.level().getBlockEntity(msg.pos);
                    if (patt1482$temp instanceof BulkFluidTankBlockEntity) {
                        BulkFluidTankBlockEntity be = (BulkFluidTankBlockEntity)patt1482$temp;
                        ItemStack carried = player.containerMenu.getCarried();
                        ItemStack held = carried.isEmpty() ? player.getItemInHand(InteractionHand.MAIN_HAND) : carried;
                        if (held.isEmpty()) {
                            be.getStorage().setFilter((AEKey)null);
                            be.onStorageChanged();
                        } else {
                            FluidUtil.getFluidContained(held).ifPresent((fluidStack) -> {
                                if (!fluidStack.isEmpty()) {
                                    be.getStorage().setFilter(AEFluidKey.of(fluidStack.getFluid()));
                                    be.onStorageChanged();
                                }

                            });
                        }
                    }
                }
            }
        });
        ((NetworkEvent.Context)ctx.get()).setPacketHandled(true);
    }
}
