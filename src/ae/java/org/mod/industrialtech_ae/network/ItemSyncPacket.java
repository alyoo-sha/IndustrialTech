package org.mod.industrialtech_ae.network;

import appeng.api.stacks.AEItemKey;
import org.mod.industrialtech_ae.menu.BulkItemChestMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemSyncPacket {
    private final BlockPos pos;
    private final CompoundTag itemTag;
    private final long amount;
    private final CompoundTag filterTag;
    private final boolean online;

    public ItemSyncPacket(BlockPos pos, AEItemKey item, long amount, AEItemKey filter, boolean online) {
        this.pos = pos;
        this.itemTag = item != null ? item.toTagGeneric() : new CompoundTag();
        this.amount = amount;
        this.filterTag = filter != null ? filter.toTagGeneric() : new CompoundTag();
        this.online = online;
    }

    private ItemSyncPacket(BlockPos pos, CompoundTag itemTag, long amount, CompoundTag filterTag, boolean online) {
        this.pos = pos;
        this.itemTag = itemTag;
        this.amount = amount;
        this.filterTag = filterTag;
        this.online = online;
    }

    public static void encode(org.mod.industrialtech_ae.network.ItemSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeNbt(msg.itemTag);
        buf.writeLong(msg.amount);
        buf.writeNbt(msg.filterTag);
        buf.writeBoolean(msg.online);
    }

    public static org.mod.industrialtech_ae.network.ItemSyncPacket decode(FriendlyByteBuf buf) {
        return new org.mod.industrialtech_ae.network.ItemSyncPacket(buf.readBlockPos(), buf.readNbt(), buf.readLong(), buf.readNbt(), buf.readBoolean());
    }

    public static void handle(org.mod.industrialtech_ae.network.ItemSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        (ctx.get()).enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                AbstractContainerMenu patt2104$temp = mc.player.containerMenu;
                if (patt2104$temp instanceof BulkItemChestMenu) {
                    BulkItemChestMenu menu = (BulkItemChestMenu)patt2104$temp;
                    AEItemKey item = msg.itemTag != null && !msg.itemTag.isEmpty() ? AEItemKey.fromTag(msg.itemTag) : null;
                    AEItemKey filter = msg.filterTag != null && !msg.filterTag.isEmpty() ? AEItemKey.fromTag(msg.filterTag) : null;
                    menu.updateClientData(msg.pos, item, msg.amount, filter, msg.online);
                }

            }
        });
        (ctx.get()).setPacketHandled(true);
    }
}
