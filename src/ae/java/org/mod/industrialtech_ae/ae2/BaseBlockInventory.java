package org.mod.industrialtech_ae.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import net.minecraft.network.chat.Component;

public record BaseBlockInventory<T extends AEKey>(GenericAeKeyStorage<T> storage) implements MEStorage {

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
        return key != null ? key.getDisplayName() : Component.empty();
    }

    public T getStoredKey() {
        return this.storage.getStoredKey();
    }

    public T getFilterKey() {
        return this.storage.getFilterKey();
    }

    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        T stored = this.storage.getStoredKey();
        T filter = this.storage.getFilterKey();
        return stored != null && stored.equals(what) || filter != null && filter.equals(what);
    }
}
