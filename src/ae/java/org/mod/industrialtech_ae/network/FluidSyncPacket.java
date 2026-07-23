package org.mod.industrialtech_ae.network;

import appeng.api.stacks.AEFluidKey;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import org.mod.industrialtech_ae.menu.BulkFluidTankMenu;

import java.util.function.Supplier;

public class FluidSyncPacket {
    private final BlockPos pos;
    private final CompoundTag fluidTag;
    private final long amount;
    private final CompoundTag filterTag;
    private final boolean online;

    public FluidSyncPacket(BlockPos pos, AEFluidKey fluid, long amount, AEFluidKey filter, boolean online) {
        this.pos = pos;
        this.fluidTag = fluid != null ? fluid.toTagGeneric() : new CompoundTag();
        this.amount = amount;
        this.filterTag = filter != null ? filter.toTagGeneric() : new CompoundTag();
        this.online = online;
    }

    private FluidSyncPacket(BlockPos pos, CompoundTag fluidTag, long amount, CompoundTag filterTag, boolean online) {
        this.pos = pos;
        this.fluidTag = fluidTag;
        this.amount = amount;
        this.filterTag = filterTag;
        this.online = online;
    }

    public static void encode(org.mod.industrialtech_ae.network.FluidSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeNbt(msg.fluidTag);
        buf.writeLong(msg.amount);
        buf.writeNbt(msg.filterTag);
        buf.writeBoolean(msg.online);
    }

    public static org.mod.industrialtech_ae.network.FluidSyncPacket decode(FriendlyByteBuf buf) {
        return new org.mod.industrialtech_ae.network.FluidSyncPacket(buf.readBlockPos(), buf.readNbt(), buf.readLong(), buf.readNbt(), buf.readBoolean());
    }

    public static void handle(org.mod.industrialtech_ae.network.FluidSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        (ctx.get()).enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                AbstractContainerMenu patt2123$temp = mc.player.containerMenu;
                if (patt2123$temp instanceof BulkFluidTankMenu) {
                    BulkFluidTankMenu menu = (BulkFluidTankMenu)patt2123$temp;
                    AEFluidKey fluid = msg.fluidTag != null && !msg.fluidTag.isEmpty() ? AEFluidKey.fromTag(msg.fluidTag) : null;
                    AEFluidKey filter = msg.filterTag != null && !msg.filterTag.isEmpty() ? AEFluidKey.fromTag(msg.filterTag) : null;
                    menu.updateClientData(msg.pos, fluid, msg.amount, filter, msg.online);
                }

            }
        });
        (ctx.get()).setPacketHandled(true);
    }
}
