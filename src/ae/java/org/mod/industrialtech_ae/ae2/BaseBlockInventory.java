package org.mod.industrialtech_ae.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import net.minecraft.network.chat.Component;

public class BaseBlockInventory<T extends AEKey> implements MEStorage {
    protected final GenericAeKeyStorage<T> storage;

    public BaseBlockInventory(GenericAeKeyStorage<T> storage) {
        this.storage = storage;
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

    public Component getDescription() {
        T key = this.storage.getStoredKey();
        return (Component)(key != null ? key.getDisplayName() : Component.empty());
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

    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        T stored = this.storage.getStoredKey();
        T filter = this.storage.getFilterKey();
        return stored != null && stored.equals(what) || filter != null && filter.equals(what);
    }
}
