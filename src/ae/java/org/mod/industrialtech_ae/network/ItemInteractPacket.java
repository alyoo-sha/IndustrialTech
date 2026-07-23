package org.mod.industrialtech_ae.network;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import org.mod.industrialtech_ae.ae2.GenericAeKeyStorage;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemInteractPacket {
    private final BlockPos pos;
    private final boolean rightClick;

    public ItemInteractPacket(BlockPos pos, boolean rightClick) {
        this.pos = pos;
        this.rightClick = rightClick;
    }

    public static void encode(org.mod.industrialtech_ae.network.ItemInteractPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeBoolean(msg.rightClick);
    }

    public static org.mod.industrialtech_ae.network.ItemInteractPacket decode(FriendlyByteBuf buf) {
        return new org.mod.industrialtech_ae.network.ItemInteractPacket(buf.readBlockPos(), buf.readBoolean());
    }

    public static void handle(org.mod.industrialtech_ae.network.ItemInteractPacket msg, Supplier<NetworkEvent.Context> ctx) {
        (ctx.get()).enqueueWork(() -> {
            ServerPlayer player = (ctx.get()).getSender();
            if (player != null) {
                if (!(player.distanceToSqr((double)msg.pos.getX() + (double)0.5F, (double)msg.pos.getY() + (double)0.5F, (double)msg.pos.getZ() + (double)0.5F) > (double)64.0F)) {
                    BlockEntity patt1577$temp = player.level().getBlockEntity(msg.pos);
                    if (patt1577$temp instanceof BulkItemChestBlockEntity) {
                        BulkItemChestBlockEntity be = (BulkItemChestBlockEntity)patt1577$temp;
                        ItemStack carriedStack = player.containerMenu.getCarried();
                        if (!carriedStack.isEmpty()) {
                            processInsert(player, be, carriedStack, msg.rightClick);
                        } else {
                            processExtract(player, be, msg.rightClick);
                        }
                    }
                }
            }
        });
        (ctx.get()).setPacketHandled(true);
    }

    private static void processInsert(ServerPlayer player, BulkItemChestBlockEntity be, ItemStack sourceStack, boolean rightClick) {
        if (!sourceStack.isEmpty()) {
            GenericAeKeyStorage<AEItemKey> storage = be.getStorage();
            AEItemKey key = AEItemKey.of(sourceStack);
            if (key != null) {
                long toInsert = rightClick ? 1L : (long)sourceStack.getCount();
                long inserted = storage.insert(key, toInsert, Actionable.MODULATE);
                if (inserted > 0L) {
                    if (inserted >= (long)sourceStack.getCount()) {
                        player.containerMenu.setCarried(ItemStack.EMPTY);
                    } else {
                        ItemStack newCarried = sourceStack.copy();
                        newCarried.shrink((int)inserted);
                        player.containerMenu.setCarried(newCarried);
                    }

                    be.onStorageChanged();
                    player.containerMenu.broadcastChanges();
                }
            }
        }
    }

    private static void processExtract(ServerPlayer player, BulkItemChestBlockEntity be, boolean rightClick) {
        GenericAeKeyStorage<AEItemKey> storage = be.getStorage();
        AEItemKey key = storage.getStoredKey();
        long amount = storage.getAmount();
        if (key != null && amount > 0L) {
            ItemStack template = key.toStack();
            if (!template.isEmpty()) {
                int toExtract = rightClick ? 1 : template.getMaxStackSize();
                long extracted = storage.extract(key, Math.min(toExtract, amount), Actionable.MODULATE);
                if (extracted > 0L) {
                    ItemStack result = key.toStack();
                    result.setCount((int)extracted);
                    player.containerMenu.setCarried(result);
                    be.onStorageChanged();
                    player.containerMenu.broadcastChanges();
                }
            }
        }
    }
}
