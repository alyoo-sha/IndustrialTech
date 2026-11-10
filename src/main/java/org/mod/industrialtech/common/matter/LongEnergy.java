package org.mod.industrialtech.common.matter;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.energy.IEnergyStorage;

public final class LongEnergy {
    private LongEnergy() {}

    public static final Capability<Handler> CAP = CapabilityManager.get(new CapabilityToken<>() {});

    /** Внешний интерфейс: энергию можно только принимать (extract только внутри машины). */
    public interface Handler {
        long receive(long max, boolean simulate);
        long getStored();
        long getCapacity();
    }

    public static class Storage implements Handler {
        private final long capacity;
        private final Runnable onChange;
        private long energy;

        public Storage(long capacity, Runnable onChange) {
            this.capacity = capacity;
            this.onChange = onChange;
        }

        @Override
        public long receive(long max, boolean simulate) {
            if (max <= 0) return 0;
            long accepted = Math.min(capacity - energy, max);
            if (!simulate && accepted > 0) {
                energy += accepted;
                onChange.run();
            }
            return accepted;
        }

        /** Списать энергию внутри машины. false, если не хватает. */
        public boolean extractInternal(long amount) {
            if (energy < amount) return false;
            energy -= amount;
            onChange.run();
            return true;
        }

        public void setStored(long value) {
            this.energy = Math.max(0, Math.min(capacity, value));
        }

        @Override
        public long getStored() {
            return energy;
        }

        @Override
        public long getCapacity() {
            return capacity;
        }
    }

    /** Для кабелей и других модов, которые знают только int-версию Forge Energy. */
    public record IntAdapter(Handler handler) implements IEnergyStorage {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return (int) handler.receive(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return (int) Math.min(handler.getStored(), Integer.MAX_VALUE);
        }

        @Override
        public int getMaxEnergyStored() {
            return (int) Math.min(handler.getCapacity(), Integer.MAX_VALUE);
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    }
}
