package org.mod.industrialtech.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.mod.industrialtech.common.block.MachineBlock;
import org.mod.industrialtech.common.container.RecyclerMenu;
import org.mod.industrialtech.common.matter.LongEnergy;
import org.mod.industrialtech.common.matter.SidedItems;
import org.mod.industrialtech.core.init.IModBlockEntity;
import org.mod.industrialtech.core.init.IModItem;

import javax.annotation.Nullable;

/** Утилизатор: любой предмет -> Утильсырьё с шансом 15%. 20 тиков, 100 FE за операцию (5 FE/тик). */
public class RecyclerBlockEntity extends BlockEntity implements MenuProvider, MachineBE {
    public static final long CAPACITY = 50_000L;
    public static final int TIME = 20;
    public static final int FE_PER_TICK = 5; // 5 * 20 = 100 FE
    public static final float CHANCE = 0.75F;

    private final ItemStackHandler items = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot == 0;
        }
    };

    private final LongEnergy.Storage energy = new LongEnergy.Storage(CAPACITY, this::setChanged);
    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> new SidedItems(items));
    private final LazyOptional<IEnergyStorage> feCap = LazyOptional.of(() -> new LongEnergy.IntAdapter(energy));

    private int progress;

    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> (int) (energy.getStored() & 0xFFFFL);
                case 2 -> (int) ((energy.getStored() >>> 16) & 0xFFFFL);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public RecyclerBlockEntity(BlockPos pos, BlockState state) {
        super(IModBlockEntity.RECYCLER_BE.get(), pos, state);
    }

    @Override
    public ItemStackHandler items() {
        return items;
    }

    @Override
    public void serverTick() {
        ItemStack in = items.getStackInSlot(0);
        ItemStack out = items.getStackInSlot(1);
        boolean hasRoom = out.isEmpty()
                || (out.is(IModItem.SCRAP.get()) && out.getCount() < out.getMaxStackSize());

        if (in.isEmpty() || !hasRoom) {
            progress = 0;
            setActive(false);
            return;
        }
        if (!energy.extractInternal(FE_PER_TICK)) {
            setActive(false);
            return;
        }
        setActive(true);

        if (++progress < TIME) return;
        progress = 0;

        items.extractItem(0, 1, false);
        if (level != null && level.random.nextFloat() < CHANCE) {
            ItemStack current = items.getStackInSlot(1);
            if (current.isEmpty()) {
                items.setStackInSlot(1, new ItemStack(IModItem.SCRAP.get()));
            } else {
                current.grow(1);
                items.setStackInSlot(1, current);
            }
        }
    }

    private void setActive(boolean active) {
        if (level == null) return;
        BlockState state = getBlockState();
        if (state.hasProperty(MachineBlock.LIT) && state.getValue(MachineBlock.LIT) != active) {
            level.setBlock(worldPosition, state.setValue(MachineBlock.LIT, active), 3);
        }
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemCap.cast();
        if (cap == ForgeCapabilities.ENERGY) return feCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemCap.invalidate();
        feCap.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", items.serializeNBT());
        tag.putLong("Energy", energy.getStored());
        tag.putInt("Progress", progress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.deserializeNBT(tag.getCompound("Items"));
        energy.setStored(tag.getLong("Energy"));
        progress = tag.getInt("Progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new RecyclerMenu(id, inv, this);
    }
}