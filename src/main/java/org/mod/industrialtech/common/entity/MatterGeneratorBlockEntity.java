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
import org.mod.industrialtech.common.container.MatterGeneratorMenu;
import org.mod.industrialtech.common.matter.LongEnergy;
import org.mod.industrialtech.common.matter.MatterTier;
import org.mod.industrialtech.common.matter.SidedItems;
import org.mod.industrialtech.common.recipe.MatterRecipe;
import org.mod.industrialtech.core.init.IModBlockEntity;

import javax.annotation.Nullable;


/** Общий BlockEntity для всех 10 генераторов. Тир определяется блоком. */
public class MatterGeneratorBlockEntity extends BlockEntity implements MenuProvider, MachineBE {
    public static final long CAPACITY = 5_000_000_000L;
    public static final int OUTPUT_STACK = 64;
    private int glow;


    private final ItemStackHandler items = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot == 0 && tier().accepts(stack);
        }
    };

    private final LongEnergy.Storage energy = new LongEnergy.Storage(CAPACITY, this::setChanged);

    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> new SidedItems(items));
    private final LazyOptional<LongEnergy.Handler> longCap = LazyOptional.of(() -> energy);
    private final LazyOptional<IEnergyStorage> feCap = LazyOptional.of(() -> new LongEnergy.IntAdapter(energy));

    /**
     * Энергия передаётся клиенту четырьмя 16-битными кусками: ContainerData уходит в пакете как short,
     * поэтому одно int/long значение исказилось бы.
     */
    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return (int) ((energy.getStored() >>> (16L * index)) & 0xFFFFL);
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public MatterGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(IModBlockEntity.GENERATOR_BE.get(), pos, state);
    }

    public MatterTier tier() {
        return getBlockState().getBlock() instanceof MachineBlock b && b.tier != null ? b.tier : MatterTier.BASIC;
    }

    @Override
    public ItemStackHandler items() {
        return items;
    }

    /** Мгновенная операция: 1 тик, энергия списывается один раз, на выход сразу стак 64. */

    @Override
    public void serverTick() {
        if (glow > 0 && --glow == 0) setActive(false);

        ItemStack in = items.getStackInSlot(0);
        if (level == null || in.isEmpty() || !items.getStackInSlot(1).isEmpty()) return;

        MatterRecipe recipe = MatterRecipe.find(level, tier().blockId, in);
        if (recipe == null) return;                      // одного утильсырья мало: нужна пачка
        if (!energy.extractInternal(recipe.energy())) return;

        if (recipe.consume()) items.extractItem(0, recipe.count(), false);
        items.setStackInSlot(1, recipe.result().copy());

        glow = 10;
        setActive(true);
    }

    private void setActive(boolean active) {
        if (level == null) return;
        BlockState state = getBlockState();
        if (state.hasProperty(MachineBlock.LIT) && state.getValue(MachineBlock.LIT) != active) {
            level.setBlock(worldPosition, state.setValue(MachineBlock.LIT, active), 3);
        }
    }

    // ---- capabilities

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemCap.cast();
        if (cap == LongEnergy.CAP) return longCap.cast();
        if (cap == ForgeCapabilities.ENERGY) return feCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemCap.invalidate();
        longCap.invalidate();
        feCap.invalidate();
    }

    // ---- save / load

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Items", items.serializeNBT());
        tag.putLong("Energy", energy.getStored());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.deserializeNBT(tag.getCompound("Items"));
        energy.setStored(tag.getLong("Energy"));
    }

    // ---- menu

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new MatterGeneratorMenu(id, inv, this);
    }
}