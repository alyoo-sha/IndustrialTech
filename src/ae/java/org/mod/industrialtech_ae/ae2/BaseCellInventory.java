package org.mod.industrialtech_ae.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.util.ConfigInventory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class BaseCellInventory<T extends AEKey> implements StorageCell {
    protected final ItemStack stack;
    protected final ISaveProvider saveProvider;
    protected final GenericAeKeyStorage<T> storage;

    public BaseCellInventory(ItemStack stack, ISaveProvider saveProvider, GenericAeKeyStorage<T> storage) {
        this.stack = stack;
        this.saveProvider = saveProvider;
        this.storage = storage;
        this.storage.load(stack.getOrCreateTag());
        Item var5 = stack.getItem();
        if (var5 instanceof ICellWorkbenchItem workbenchItem) {
            ConfigInventory config = workbenchItem.getConfigInventory(stack);
            T workbenchFilter = storage.getKeyType().cast(config.getKey(0));
            this.storage.setFilter(workbenchFilter);
            if (!Objects.equals(this.storage.getFilterKey(), workbenchFilter)) {
                T actualFilter = this.storage.getFilterKey();
                if (actualFilter != null) {
                    config.setStack(0, new GenericStack(actualFilter, 0L));
                } else {
                    config.setStack(0, (GenericStack)null);
                }
            }
        }

        this.storage.setOnDirty(this::persist);
    }

    public void persist() {
        this.storage.save(this.stack.getOrCreateTag());
        if (this.saveProvider != null) {
            this.saveProvider.saveChanges();
        }

    }

    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        return this.storage.insert(what, amount, mode);
    }

    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        return this.storage.extract(what, amount, mode);
    }

    public void getAvailableStacks(KeyCounter out) {
        this.storage.getAvailableStacks(out);
    }

    public CellState getStatus() {
        T stored = this.storage.getStoredKey();
        T filter = this.storage.getFilterKey();
        long amount = this.storage.getAmount();
        if (stored != null && amount > 0L) {
            return filter == null ? CellState.FULL : CellState.NOT_EMPTY;
        } else {
            return CellState.EMPTY;
        }
    }

    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        T stored = this.storage.getStoredKey();
        T filter = this.storage.getFilterKey();
        return stored != null && stored.equals(what) || filter != null && filter.equals(what);
    }

    public Component getDescription() {
        return this.stack.getHoverName();
    }

    public double getIdleDrain() {
        return 0.1;
    }

    public GenericAeKeyStorage<T> getStorage() {
        return this.storage;
    }

    public T getStoredKey() {
        return (T)this.storage.getStoredKey();
    }

    public T getFilterKey() {
        return (T)this.storage.getFilterKey();
    }

    public long getStoredAmount() {
        return this.storage.getAmount();
    }

    public boolean isEmpty() {
        return this.storage.isEmpty();
    }
}
