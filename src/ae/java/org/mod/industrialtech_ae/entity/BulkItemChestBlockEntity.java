package org.mod.industrialtech_ae.entity;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;
import org.mod.industrialtech_ae.ae2.AeKeyType;
import org.mod.industrialtech_ae.init.AEModBlockEntity;
import org.mod.industrialtech_ae.menu.BulkItemChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BulkItemChestBlockEntity extends BaseBlockEntity<AEItemKey> {
    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(this::createItemHandler);

    public BulkItemChestBlockEntity(BlockPos pos, BlockState state) {
        super(AEModBlockEntity.BULK_ITEM_CHEST_BE.get(), pos, state, AeKeyType.ITEM);
    }

    protected void syncToOpenPlayers() {
        if (this.level != null && !this.level.isClientSide) {
            for(ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                AbstractContainerMenu var4 = serverPlayer.containerMenu;
                if (var4 instanceof BulkItemChestMenu) {
                    BulkItemChestMenu menu = (BulkItemChestMenu)var4;
                    if (menu.getBlockPos().equals(this.worldPosition)) {
                        menu.syncToClient(serverPlayer);
                    }
                }
            }

        }
    }

    public <C> @NotNull LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.itemHandlerCap.cast() : super.getCapability(cap, side);
    }

    public void setRemoved() {
        super.setRemoved();
        this.itemHandlerCap.invalidate();
    }

    private IItemHandler createItemHandler() {
        return new IItemHandler() {
            public int getSlots() {
                return 1;
            }

            public @NotNull ItemStack getStackInSlot(int slot) {
                AEItemKey key = BulkItemChestBlockEntity.this.storage.getStoredKey();
                if (key == null) {
                    return ItemStack.EMPTY;
                } else {
                    ItemStack stack = key.toStack();
                    stack.setCount((int)Math.min(BulkItemChestBlockEntity.this.storage.getAmount(), 2147483647L));
                    return stack;
                }
            }

            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (!stack.isEmpty() && this.isItemValid(slot, stack)) {
                    Actionable mode = simulate ? Actionable.SIMULATE : Actionable.MODULATE;
                    long inserted = BulkItemChestBlockEntity.this.storage.insert(AEItemKey.of(stack), stack.getCount(), mode);
                    if (inserted >= (long)stack.getCount()) {
                        return ItemStack.EMPTY;
                    } else {
                        ItemStack remainder = stack.copy();
                        remainder.shrink((int)inserted);
                        return remainder;
                    }
                } else {
                    return stack;
                }
            }

            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                AEItemKey key = BulkItemChestBlockEntity.this.storage.getStoredKey();
                if (key == null) {
                    return ItemStack.EMPTY;
                } else {
                    Actionable mode = simulate ? Actionable.SIMULATE : Actionable.MODULATE;
                    long extracted = BulkItemChestBlockEntity.this.storage.extract(key, amount, mode);
                    if (extracted <= 0L) {
                        return ItemStack.EMPTY;
                    } else {
                        ItemStack result = key.toStack();
                        result.setCount((int)extracted);
                        return result;
                    }
                }
            }

            public int getSlotLimit(int slot) {
                return Integer.MAX_VALUE;
            }

            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                AEItemKey filter = BulkItemChestBlockEntity.this.storage.getFilterKey();
                return filter == null || filter.matches(stack);
            }
        };
    }

    public @NotNull Component getDisplayName() {
        return Component.translatable("block.industrialtech_ae.bulk_item_chest");
    }

    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new BulkItemChestMenu(i, inventory, this);
    }
}
