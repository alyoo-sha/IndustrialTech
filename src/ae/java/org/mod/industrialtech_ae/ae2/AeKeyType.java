package org.mod.industrialtech_ae.ae2;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.nbt.CompoundTag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class AeKeyType<T extends AEKey> {
    private static final Map<String, org.mod.industrialtech_ae.ae2.AeKeyType<?>> TYPES = new HashMap();
    public static final org.mod.industrialtech_ae.ae2.AeKeyType<AEItemKey> ITEM = register("item", (key) -> key instanceof AEItemKey, (tag) -> tag != null && !tag.isEmpty() ? AEItemKey.fromTag(tag) : null);
    public static final org.mod.industrialtech_ae.ae2.AeKeyType<AEFluidKey> FLUID = register("fluid", (key) -> key instanceof AEFluidKey, (tag) -> tag != null && !tag.isEmpty() ? AEFluidKey.fromTag(tag) : null);
    private final String storageTag;
    private final Predicate<AEKey> validator;
    private final Function<CompoundTag, T> reader;

    public static <T extends AEKey> org.mod.industrialtech_ae.ae2.AeKeyType<T> register(String id, Predicate<AEKey> validator, Function<CompoundTag, T> reader) {
        org.mod.industrialtech_ae.ae2.AeKeyType<T> type = new org.mod.industrialtech_ae.ae2.AeKeyType<T>(id, validator, reader);
        TYPES.put(id, type);
        return type;
    }

    public static <T extends AEKey> org.mod.industrialtech_ae.ae2.AeKeyType<T> get(String id) {
        return (org.mod.industrialtech_ae.ae2.AeKeyType)TYPES.get(id);
    }

    public static Collection<org.mod.industrialtech_ae.ae2.AeKeyType<?>> values() {
        return TYPES.values();
    }

    public AeKeyType(String storageTag, Predicate<AEKey> validator, Function<CompoundTag, T> reader) {
        this.storageTag = (String) Objects.requireNonNull(storageTag, "storageTag");
        this.validator = (Predicate)Objects.requireNonNull(validator, "validator");
        this.reader = (Function)Objects.requireNonNull(reader, "reader");
    }

    public String getStorageTag() {
        return this.storageTag;
    }

    public boolean isSupported(AEKey key) {
        return key != null && this.validator.test(key);
    }

    public T cast(AEKey key) {
        return (T)(this.isSupported(key) ? key : null);
    }

    public T read(CompoundTag tag) {
        return (T)(this.reader.apply(tag));
    }

    public CompoundTag write(T key) {
        return key != null ? key.toTagGeneric() : new CompoundTag();
    }
}
