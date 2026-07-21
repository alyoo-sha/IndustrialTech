package org.mod.industrialtech_ae.menu;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import org.mod.industrialtech_ae.ae2.GenericAeKeyStorage;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import org.mod.industrialtech_ae.init.AEModBlock;
import org.mod.industrialtech_ae.init.AEModMenu;
import org.mod.industrialtech_ae.init.AEModNetworking;
import org.mod.industrialtech_ae.network.ItemSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class BulkItemChestMenu extends AbstractContainerMenu {
    private final Container tankContainer;
    private final BulkItemChestBlockEntity blockEntity;
    private final Level level;
    private final BlockPos pos;
    private AEItemKey clientItem;
    private long clientAmount;
    private AEItemKey clientFilter;
    private boolean clientOnline;

    public BulkItemChestMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, getBlockEntity(inv, extraData));
    }

    public BulkItemChestMenu(int containerId, Inventory inv, BulkItemChestBlockEntity blockEntity) {
        super((MenuType) AEModMenu.BULK_ITEM_CHEST_MENU.get(), containerId);
        this.tankContainer = new SimpleContainer(1);
        this.blockEntity = blockEntity;
        this.level = inv.player.level();
        this.pos = blockEntity.getBlockPos();
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        Player var5 = inv.player;
        if (var5 instanceof ServerPlayer serverPlayer) {
            this.syncToClient(serverPlayer);
        } else {
            GenericAeKeyStorage<AEItemKey> storage = blockEntity.getStorage();
            this.clientItem = (AEItemKey)storage.getStoredKey();
            this.clientAmount = storage.getAmount();
            this.clientFilter = (AEItemKey)storage.getFilterKey();
            this.clientOnline = blockEntity.isOnline();
        }

    }

    private static BulkItemChestBlockEntity getBlockEntity(Inventory inv, FriendlyByteBuf extraData) {
        if (extraData == null) {
            throw new IllegalStateException("Missing menu extra data for BulkItemTankMenu");
        } else {
            BlockPos pos = extraData.readBlockPos();
            BlockEntity be = inv.player.level().getBlockEntity(pos);
            if (be instanceof BulkItemChestBlockEntity) {
                BulkItemChestBlockEntity tank = (BulkItemChestBlockEntity)be;
                return tank;
            } else {
                String var10002 = String.valueOf(pos);
                throw new IllegalStateException("Block entity at " + var10002 + " is not BulkItemTankBlockEntity: " + String.valueOf(be));
            }
        }
    }

    public void syncToClient(ServerPlayer serverPlayer) {
        GenericAeKeyStorage<AEItemKey> storage = this.blockEntity.getStorage();
        AEModNetworking.sendToPlayer(new ItemSyncPacket(this.pos, (AEItemKey)storage.getStoredKey(), storage.getAmount(), (AEItemKey)storage.getFilterKey(), this.blockEntity.isOnline()), serverPlayer);
    }

    public void updateClientData(BlockPos pos, AEItemKey item, long amount, AEItemKey filter, boolean online) {
        if (this.pos.equals(pos)) {
            this.clientItem = item;
            this.clientAmount = amount;
            this.clientFilter = filter;
            this.clientOnline = online;
        }
    }

    public BlockPos getBlockPos() {
        return this.pos;
    }

    public AEItemKey getItem() {
        return this.clientItem;
    }

    public long getAmount() {
        return this.clientAmount;
    }

    public AEItemKey getFilter() {
        return this.clientFilter;
    }

    public boolean isOnline() {
        return this.clientOnline;
    }

    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = (Slot)this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stackInSlot = slot.getItem();
            ItemStack copy = stackInSlot.copy();
            int playerInvStart = 0;
            int playerInvEnd = 27;
            int hotbarStart = 27;
            int hotbarEnd = 36;
            if (index < playerInvEnd) {
                if (!this.moveItemStackTo(stackInSlot, hotbarStart, hotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (index >= hotbarEnd) {
                    return ItemStack.EMPTY;
                }

                if (!this.moveItemStackTo(stackInSlot, playerInvStart, playerInvEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(player, stackInSlot);
            return copy;
        }
    }

    private ItemStack insertIntoTank(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            GenericAeKeyStorage<AEItemKey> storage = this.blockEntity.getStorage();
            AEItemKey key = AEItemKey.of(stack);
            long inserted = storage.insert(key, (long)stack.getCount(), Actionable.MODULATE);
            if (inserted <= 0L) {
                return stack;
            } else {
                ItemStack remainder = stack.copy();
                remainder.shrink((int)inserted);
                this.blockEntity.setChanged();
                return remainder;
            }
        }
    }

    private ItemStack extractFromTankForSlot(int amount) {
        GenericAeKeyStorage<AEItemKey> storage = this.blockEntity.getStorage();
        AEItemKey key = (AEItemKey)storage.getStoredKey();
        if (key != null && storage.getAmount() > 0L) {
            long extracted = Math.min((long)amount, storage.getAmount());
            long actuallyExtracted = storage.extract(key, extracted, Actionable.MODULATE);
            if (actuallyExtracted <= 0L) {
                return ItemStack.EMPTY;
            } else {
                ItemStack stack = key.toStack();
                stack.setCount((int)actuallyExtracted);
                this.blockEntity.setChanged();
                return stack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, this.pos), player, (Block) AEModBlock.BULK_ITEM_CHEST.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for(int row = 0; row < 3; ++row) {
            for(int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 104 + row * 18));
            }
        }

    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for(int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 162));
        }

    }
}
