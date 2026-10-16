package org.mod.industrialtech_ae.me;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.crafting.ICraftingUnitType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.config.IAeCoProcessorConfig;
import org.mod.industrialtech_ae.core.init.IAeModBlock;

import java.util.function.Supplier;

public enum IAeCraftingUnitType implements ICraftingUnitType {
    IAE_COMMON_UNIT(0, () -> 0, IAeCraftingFamily.COMMON, IAeCraftingStorageType.UNIT),
    IAE_COMMON_MONITOR(0, () -> 0, IAeCraftingFamily.COMMON, IAeCraftingStorageType.MONITOR),
    IAE_COMMON_STORAGE_1K(1, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_1k, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_1K),
    IAE_COMMON_STORAGE_4K(4, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_4k, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_4K),
    IAE_COMMON_STORAGE_16K(16, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_16k, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_16K),
    IAE_COMMON_STORAGE_64K(64, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_64k, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_64K),
    IAE_COMMON_STORAGE_256K(256, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_256k, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_256K),
    IAE_COMMON_STORAGE_1M(1024, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_1m, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_1M),
    IAE_COMMON_STORAGE_4M(4096, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_4m, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_4M),
    IAE_COMMON_STORAGE_16M(16384, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_16m, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_16M),
    IAE_COMMON_STORAGE_64M(65536, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_64m, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_64M),
    IAE_COMMON_STORAGE_256M(262144, () -> IAeCoProcessorConfig.CRAFTING_STORAGE_256m, IAeCraftingFamily.COMMON, IAeCraftingStorageType.STORAGE_256M),

    IAE_MEGA_UNIT(0, () -> 0, IAeCraftingFamily.MEGA, IAeCraftingStorageType.UNIT),

    IAE_GIGA_UNIT(0, () -> 0, IAeCraftingFamily.GIGA, IAeCraftingStorageType.UNIT);

    public final long storageKb;
    public final Supplier<Integer> processors;
    public final IAeCraftingFamily family;
    public final IAeCraftingStorageType storageType;

    IAeCraftingUnitType(long storageKb, Supplier<Integer> processors, IAeCraftingFamily family, IAeCraftingStorageType storageType)
    {
        this.storageKb = storageKb;
        this.processors = processors;
        this.family = family;
        this.storageType = storageType;
    }

    @Override
    public long getStorageBytes() {
        return 1024L * this.storageKb;
    }

    @Override
    public int getAcceleratorThreads() {
        return this.processors.get();
    }

    @Override
    public Item getItemFromType() {
        RegistryObject<? extends AbstractCraftingUnitBlock<?>> definition = switch (this) {
            case IAE_COMMON_UNIT -> IAeModBlock.COMMON_CRAFTING_UNIT_BLOCK;
            case IAE_COMMON_MONITOR -> IAeModBlock.COMMON_CRAFTING_MONITOR_BLOCK;
            case IAE_COMMON_STORAGE_1K -> IAeModBlock.COMMON_CRAFTING_STORAGE_1K_BLOCK;
            case IAE_COMMON_STORAGE_4K -> IAeModBlock.COMMON_CRAFTING_STORAGE_4K_BLOCK;
            case IAE_COMMON_STORAGE_16K -> IAeModBlock.COMMON_CRAFTING_STORAGE_16K_BLOCK;
            case IAE_COMMON_STORAGE_64K -> IAeModBlock.COMMON_CRAFTING_STORAGE_64K_BLOCK;
            case IAE_COMMON_STORAGE_256K -> IAeModBlock.COMMON_CRAFTING_STORAGE_256K_BLOCK;
            case IAE_COMMON_STORAGE_1M -> IAeModBlock.COMMON_CRAFTING_STORAGE_1M_BLOCK;
            case IAE_COMMON_STORAGE_4M -> IAeModBlock.COMMON_CRAFTING_STORAGE_4M_BLOCK;
            case IAE_COMMON_STORAGE_16M -> IAeModBlock.COMMON_CRAFTING_STORAGE_16M_BLOCK;
            case IAE_COMMON_STORAGE_64M -> IAeModBlock.COMMON_CRAFTING_STORAGE_64M_BLOCK;
            case IAE_COMMON_STORAGE_256M -> IAeModBlock.COMMON_CRAFTING_STORAGE_256M_BLOCK;
            case IAE_MEGA_UNIT -> IAeModBlock.MEGA_CRAFTING_UNIT_BLOCK;
            case IAE_GIGA_UNIT -> IAeModBlock.GIGA_CRAFTING_UNIT_BLOCK;
        };
        return definition.get().asItem();
    }
}
