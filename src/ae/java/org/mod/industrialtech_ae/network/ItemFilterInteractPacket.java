package org.mod.industrialtech_ae.network;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;

import java.util.function.Supplier;

public class ItemFilterInteractPacket {
    private final BlockPos pos;

    public ItemFilterInteractPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(org.mod.industrialtech_ae.network.ItemFilterInteractPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static org.mod.industrialtech_ae.network.ItemFilterInteractPacket decode(FriendlyByteBuf buf) {
        return new org.mod.industrialtech_ae.network.ItemFilterInteractPacket(buf.readBlockPos());
    }

    public static void handle(org.mod.industrialtech_ae.network.ItemFilterInteractPacket msg, Supplier<NetworkEvent.Context> ctx) {
        (ctx.get()).enqueueWork(() -> {
            ServerPlayer player = (ctx.get()).getSender();
            if (player != null) {
                if (!(player.distanceToSqr((double)msg.pos.getX() + (double)0.5F, (double)msg.pos.getY() + (double)0.5F, (double)msg.pos.getZ() + (double)0.5F) > (double)64.0F)) {
                    BlockEntity patt1460$temp = player.level().getBlockEntity(msg.pos);
                    if (patt1460$temp instanceof BulkItemChestBlockEntity) {
                        BulkItemChestBlockEntity be = (BulkItemChestBlockEntity)patt1460$temp;
                        ItemStack carried = player.containerMenu.getCarried();
                        ItemStack held = carried.isEmpty() ? player.getItemInHand(InteractionHand.MAIN_HAND) : carried;
                        if (held.isEmpty()) {
                            be.getStorage().setFilter(null);
                            be.onStorageChanged();
                        } else {
                            be.getStorage().setFilter(AEItemKey.of(held));
                            be.onStorageChanged();
                        }
                    }
                }
            }
        });
        (ctx.get()).setPacketHandled(true);
    }
}
