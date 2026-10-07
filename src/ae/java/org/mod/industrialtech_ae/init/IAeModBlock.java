package org.mod.industrialtech_ae.init;

import appeng.block.AEBaseBlock;
import appeng.block.crafting.CraftingMonitorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.Industrialtech_ae;
import org.mod.industrialtech_ae.block.BulkFluidTankBlock;
import org.mod.industrialtech_ae.block.BulkItemChestBlock;
import org.mod.industrialtech_ae.block.IAeCraftingMonitorBlock;
import org.mod.industrialtech_ae.block.IAeCraftingUnitBlock;
import org.mod.industrialtech_ae.item.IAeCraftingBlockItem;
import org.mod.industrialtech_ae.me.IAeCraftingUnitType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class IAeModBlock {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Industrialtech_ae.MOD_ID);

    /** Все зарегистрированные блоки мода. */
    public static final List<RegistryObject<? extends Block>> ALL = new ArrayList<>();
    public static final List<RegistryObject<? extends IAeCraftingUnitBlock>> CRAFTING_STORAGES = new ArrayList<>();
    public static final List<RegistryObject<? extends CraftingMonitorBlock>> CRAFTING_MONITORS = new ArrayList<>();

    private IAeModBlock() {}

    public static void init() {
        BLOCKS.register(Industrialtech_ae.modEventBus);
        Industrialtech_ae.LOGGER.info("Initialised block");
    }

    // ---- Блоки автокрафта ----
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_UNIT_BLOCK =
            craftingUnit("common_crafting_unit_block", IAeCraftingUnitType.IAE_COMMON_UNIT);
    public static final RegistryObject<IAeCraftingMonitorBlock> COMMON_CRAFTING_MONITOR_BLOCK =
            craftingMonitor("common_crafting_monitor_block", IAeCraftingUnitType.IAE_COMMON_MONITOR);

    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_1K_BLOCK =
            craftingStorage("1k", IAeCraftingUnitType.IAE_COMMON_STORAGE_1K);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_4K_BLOCK =
            craftingStorage("4k", IAeCraftingUnitType.IAE_COMMON_STORAGE_4K);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_16K_BLOCK =
            craftingStorage("16k", IAeCraftingUnitType.IAE_COMMON_STORAGE_16K);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_64K_BLOCK =
            craftingStorage("64k", IAeCraftingUnitType.IAE_COMMON_STORAGE_64K);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_256K_BLOCK =
            craftingStorage("256k", IAeCraftingUnitType.IAE_COMMON_STORAGE_256K);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_1M_BLOCK =
            craftingStorage("1m", IAeCraftingUnitType.IAE_COMMON_STORAGE_1M);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_4M_BLOCK =
            craftingStorage("4m", IAeCraftingUnitType.IAE_COMMON_STORAGE_4M);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_16M_BLOCK =
            craftingStorage("16m", IAeCraftingUnitType.IAE_COMMON_STORAGE_16M);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_64M_BLOCK =
            craftingStorage("64m", IAeCraftingUnitType.IAE_COMMON_STORAGE_64M);
    public static final RegistryObject<IAeCraftingUnitBlock> COMMON_CRAFTING_STORAGE_256M_BLOCK =
            craftingStorage("256m", IAeCraftingUnitType.IAE_COMMON_STORAGE_256M);

    public static final RegistryObject<IAeCraftingUnitBlock> MEGA_CRAFTING_UNIT_BLOCK =
            craftingUnit("mega_crafting_unit_block", IAeCraftingUnitType.IAE_MEGA_UNIT);
    public static final RegistryObject<IAeCraftingUnitBlock> GIGA_CRAFTING_UNIT_BLOCK =
            craftingUnit("giga_crafting_unit_block", IAeCraftingUnitType.IAE_GIGA_UNIT);

    // ---- Прочие блоки ----
    public static final RegistryObject<Block> BULK_FLUID_TANK = registerBlock(
            "bulk_fluid_tank",
            () -> new BulkFluidTankBlock(BlockBehaviour.Properties.of().strength(1.0F).noOcclusion()),
            p -> p.stacksTo(1));

    public static final RegistryObject<Block> BULK_ITEM_CHEST = registerBlock(
            "bulk_item_chest",
            () -> new BulkItemChestBlock(BlockBehaviour.Properties.of().strength(1.0F).noOcclusion()),
            p -> p.stacksTo(1));

    // ---- Регистрация ----

    /**
     * Базовый метод: регистрирует блок и соответствующий предмет (ровно один раз)
     * и добавляет блок в {@link #ALL}.
     */
    private static <T extends Block> RegistryObject<T> register(
            String name,
            Supplier<T> block,
            Function<T, ? extends Item> itemFactory
    ) {
        RegistryObject<T> object = BLOCKS.register(name, block);
        IAeModItem.ITEM.register(name, () -> itemFactory.apply(object.get()));
        ALL.add(object);
        return object;
    }

    /** Блок с обычным BlockItem и настройкой свойств предмета. */
    private static <T extends Block> RegistryObject<T> registerBlock(
            String name,
            Supplier<T> block,
            Consumer<Item.Properties> itemConfig
    ) {
        return register(name, block, b -> {
            Item.Properties props = new Item.Properties();
            itemConfig.accept(props);
            return new BlockItem(b, props);
        });
    }

    private static RegistryObject<IAeCraftingUnitBlock> craftingUnit(String name, IAeCraftingUnitType type) {
        RegistryObject<IAeCraftingUnitBlock> object = register(
                name,
                () -> new IAeCraftingUnitBlock(AEBaseBlock.metalProps(), type),
                b -> new IAeCraftingBlockItem(b, new Item.Properties(), type.family));
        CRAFTING_STORAGES.add(object);
        return object;
    }

    private static RegistryObject<IAeCraftingUnitBlock> craftingStorage(String size, IAeCraftingUnitType type) {
        return craftingUnit("common_crafting_storage_" + size + "_block", type);
    }

    private static RegistryObject<IAeCraftingMonitorBlock> craftingMonitor(String name, IAeCraftingUnitType type) {
        RegistryObject<IAeCraftingMonitorBlock> object = register(
                name,
                () -> new IAeCraftingMonitorBlock(type),
                b -> new IAeCraftingBlockItem(b, new Item.Properties(), type.family));
        CRAFTING_MONITORS.add(object);
        return object;
    }
}