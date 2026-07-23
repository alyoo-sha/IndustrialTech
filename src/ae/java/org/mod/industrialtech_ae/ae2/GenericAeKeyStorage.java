package org.mod.industrialtech_ae.ae2;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class GenericAeKeyStorage<T extends AEKey> {
    protected T filterKey;
    protected T storedKey;
    protected long amount;
    protected Runnable onDirty;
    protected final AeKeyType<T> keyType;
    protected KeyCounter cachedStacks;
    protected boolean cacheDirty = true;

    public GenericAeKeyStorage(AeKeyType<T> keyType, T filterKey, Runnable onDirty) {
        this.keyType = Objects.requireNonNull(keyType, "keyType");
        this.filterKey = filterKey;
        this.onDirty = onDirty;
    }

    public void load(CompoundTag tag) {
        if (tag.contains(this.keyType.getStorageTag())) {
            this.storedKey = this.keyType.read(tag.getCompound(this.keyType.getStorageTag()));
        } else {
            this.storedKey = null;
        }

        this.amount = Math.max(0L, tag.getLong("amount"));
        if (this.amount == 0L) {
            this.storedKey = null;
        }

        if (tag.contains("filter")) {
            this.filterKey = this.keyType.read(tag.getCompound("filter"));
        } else {
            this.filterKey = null;
        }

        if (this.storedKey != null && this.amount > 0L && (this.filterKey == null || !this.filterKey.equals(this.storedKey))) {
            this.filterKey = this.storedKey;
        }

        this.cacheDirty = true;
    }

    public void save(CompoundTag tag) {
        if (this.storedKey != null && this.amount > 0L) {
            tag.put(this.keyType.getStorageTag(), this.keyType.write(this.storedKey));
        } else {
            tag.remove(this.keyType.getStorageTag());
        }

        if (this.filterKey != null) {
            tag.put("filter", this.keyType.write(this.filterKey));
        } else {
            tag.remove("filter");
        }

        tag.putLong("amount", this.amount);
    }

    public long insert(AEKey what, long amount, Actionable mode) {
        if (amount > 0L && this.keyType.isSupported(what)) {
            T key = this.keyType.cast(what);
            if (key == null) {
                return 0L;
            } else if (this.filterKey != null && this.filterKey.equals(key)) {
                if (this.storedKey != null && !this.storedKey.equals(key)) {
                    return 0L;
                } else {
                    long inserted = Math.min(amount, this.getCapacity() - this.amount);
                    if (inserted <= 0L) {
                        return 0L;
                    } else {
                        if (mode == Actionable.MODULATE) {
                            this.storedKey = key;
                            this.amount += inserted;
                            this.markDirty();
                        }

                        return inserted;
                    }
                }
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public long extract(AEKey what, long amount, Actionable mode) {
        if (amount > 0L && this.keyType.isSupported(what)) {
            T key = this.keyType.cast(what);
            if (this.storedKey != null && this.storedKey.equals(key)) {
                long extracted = Math.min(this.amount, amount);
                if (extracted <= 0L) {
                    return 0L;
                } else {
                    if (mode == Actionable.MODULATE) {
                        this.amount -= extracted;
                        if (this.amount <= 0L) {
                            this.amount = 0L;
                            this.storedKey = null;
                        }

                        this.markDirty();
                    }

                    return extracted;
                }
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public void getAvailableStacks(KeyCounter out) {
        if (this.cacheDirty || this.cachedStacks == null) {
            this.cachedStacks = new KeyCounter();
            if (this.storedKey != null && this.amount > 0L) {
                this.cachedStacks.add(this.storedKey, this.amount);
            }

            this.cacheDirty = false;
        }

        out.addAll(this.cachedStacks);
    }

    public void setFilter(AEKey newFilter) {
        if (!Objects.equals(this.filterKey, newFilter)) {
            if (this.storedKey == null || this.amount <= 0L) {
                this.filterKey = (T) newFilter;
                this.markDirty();
            }
        }
    }

    public boolean isEmpty() {
        return this.storedKey == null || this.amount <= 0L;
    }

    public T getStoredKey() {
        return this.storedKey;
    }

    public T getFilterKey() {
        return this.filterKey;
    }

    public long getAmount() {
        return this.amount;
    }

    public AeKeyType<T> getKeyType() {
        return this.keyType;
    }

    public void setOnDirty(Runnable onDirty) {
        this.onDirty = onDirty;
    }

    public void markDirty() {
        this.cacheDirty = true;
        if (this.onDirty != null) {
            this.onDirty.run();
        }

    }

    protected long getCapacity() {
        return Long.MAX_VALUE;
    }
}
