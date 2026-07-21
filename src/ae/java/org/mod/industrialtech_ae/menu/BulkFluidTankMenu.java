package org.mod.industrialtech_ae.menu;

import appeng.api.stacks.AEFluidKey;
import org.mod.industrialtech_ae.ae2.GenericAeKeyStorage;
import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import org.mod.industrialtech_ae.init.AEModBlock;
import org.mod.industrialtech_ae.init.AEModMenu;
import org.mod.industrialtech_ae.init.AEModNetworking;
import org.mod.industrialtech_ae.network.FluidSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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

public class BulkFluidTankMenu extends AbstractContainerMenu {
    private final BulkFluidTankBlockEntity blockEntity;
    private final Level level;
    private final BlockPos pos;
    private AEFluidKey clientFluid;
    private long clientAmount;
    private AEFluidKey clientFilter;
    private boolean clientOnline;

    public BulkFluidTankMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, getBlockEntity(inv, extraData));
    }

    public BulkFluidTankMenu(int containerId, Inventory inv, BulkFluidTankBlockEntity blockEntity) {
        super((MenuType) AEModMenu.BULK_FLUID_TANK_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.level = inv.player.level();
        this.pos = blockEntity.getBlockPos();
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        Player var5 = inv.player;
        if (var5 instanceof ServerPlayer serverPlayer) {
            this.syncToClient(serverPlayer);
        } else {
            GenericAeKeyStorage<AEFluidKey> storage = blockEntity.getStorage();
            this.clientFluid = (AEFluidKey)storage.getStoredKey();
            this.clientAmount = storage.getAmount();
            this.clientFilter = (AEFluidKey)storage.getFilterKey();
            this.clientOnline = blockEntity.isOnline();
        }

    }

    private static BulkFluidTankBlockEntity getBlockEntity(Inventory inv, FriendlyByteBuf extraData) {
        if (extraData == null) {
            throw new IllegalStateException("Missing menu extra data for BulkFluidTankMenu");
        } else {
            BlockPos pos = extraData.readBlockPos();
            BlockEntity be = inv.player.level().getBlockEntity(pos);
            if (be instanceof BulkFluidTankBlockEntity) {
                BulkFluidTankBlockEntity tank = (BulkFluidTankBlockEntity)be;
                return tank;
            } else {
                String var10002 = String.valueOf(pos);
                throw new IllegalStateException("Block entity at " + var10002 + " is not BulkFluidTankBlockEntity: " + String.valueOf(be));
            }
        }
    }

    public void syncToClient(ServerPlayer serverPlayer) {
        GenericAeKeyStorage<AEFluidKey> storage = this.blockEntity.getStorage();
        AEModNetworking.sendToPlayer(new FluidSyncPacket(this.pos, (AEFluidKey)storage.getStoredKey(), storage.getAmount(), (AEFluidKey)storage.getFilterKey(), this.blockEntity.isOnline()), serverPlayer);
    }

    public void updateClientData(BlockPos pos, AEFluidKey fluid, long amount, AEFluidKey filter, boolean online) {
        if (this.pos.equals(pos)) {
            this.clientFluid = fluid;
            this.clientAmount = amount;
            this.clientFilter = filter;
            this.clientOnline = online;
        }
    }

    public BlockPos getBlockPos() {
        return this.pos;
    }

    public AEFluidKey getFluid() {
        return this.clientFluid;
    }

    public long getAmount() {
        return this.clientAmount;
    }

    public AEFluidKey getFilter() {
        return this.clientFilter;
    }

    public boolean isOnline() {
        return this.clientOnline;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stackInSlot = slot.getItem();
            result = stackInSlot.copy();
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
            return result;
        }
    }

    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(this.level, this.pos), player, (Block) AEModBlock.BULK_FLUID_TANK.get());
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
