package org.mod.industrialtech.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mod.industrialtech.common.container.InfiniteEnergyMenu;
import org.mod.industrialtech.common.matter.LongEnergy;
import org.mod.industrialtech.core.init.IModBlockEntity;
import org.mod.industrialtech.core.init.MatterRegistry;

/**
 * Бесконечный источник:
 *  - сам толкает энергию во все соседние машины каждый тик (long-capability, если она есть, иначе обычный FE);
 *  - для труб и кабелей отдаёт энергию через ForgeCapabilities.ENERGY (extractEnergy возвращает запрошенное);
 *  - сам энергию не принимает.
 */
public class BlockEntityInfiniteEnergy extends BlockEntity implements MenuProvider {

    private final IEnergyStorage storage = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        /** Бесконечно: всегда отдаём ровно столько, сколько просят (до Integer.MAX_VALUE за вызов). */
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return Math.max(0, maxExtract);
        }

        @Override
        public int getEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getMaxEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    };

    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> storage);

    public BlockEntityInfiniteEnergy(BlockPos pos, BlockState state) {
        super(IModBlockEntity.INFINITE_ENERGY_BE.get(), pos, state);
    }

    /** Раз в тик пытаемся зарядить всех шести соседей. */
    public void serverTick() {
        Level lvl = this.level;
        if (lvl == null) {
            return;
        }

        for (Direction dir : Direction.values()) {
            BlockEntity neighbor = lvl.getBlockEntity(this.worldPosition.relative(dir));
            if (neighbor == null) {
                continue;
            }
            // Сторона соседа, которая смотрит на наш блок
            Direction side = dir.getOpposite();

            // 1) Long-энергия (Генераторы Материи): заливаем сколько влезет, не ограничиваясь int
            LazyOptional<LongEnergy.Handler> big = neighbor.getCapability(LongEnergy.CAP, side);
            if (big.isPresent()) {
                big.ifPresent(handler -> {
                    if (handler != null) {
                        handler.receive(Long.MAX_VALUE, false);
                    }
                });
                continue;
            }

            // 2) Обычный Forge Energy (Утилизатор и чужие моды)
            LazyOptional<IEnergyStorage> fe = neighbor.getCapability(ForgeCapabilities.ENERGY, side);
            fe.ifPresent(target -> {
                if (target != null && target.canReceive()) {
                    target.receiveEnergy(Integer.MAX_VALUE, false);
                }
            });
        }
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCap.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new InfiniteEnergyMenu(id, inv, this.worldPosition);
    }
}