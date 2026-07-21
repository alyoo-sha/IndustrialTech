package org.mod.industrialtech_ae.init;

import org.mod.industrialtech_ae.block.BulkFluidTankBlock;
import org.mod.industrialtech_ae.block.BulkItemChestBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.Industrialtech_ae;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AEModBlock {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Industrialtech_ae.MOD_ID);

    public static void init() {
        BLOCKS.register(Industrialtech_ae.modEventBus);
    }

    // ---- Регистрируемые блоки ----
    public static final RegistryObject<Block> BULK_FLUID_TANK = registerBlock(
            "bulk_fluid_tank",
            () -> new BulkFluidTankBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F)
                    .noOcclusion()),
            p -> p.stacksTo(1)   // кастомизация свойств предмета
    );

    public static final RegistryObject<Block> BULK_ITEM_CHEST = registerBlock(
            "bulk_item_chest",
            () -> new BulkItemChestBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F)
                    .noOcclusion()),
            p -> p.stacksTo(1)
    );

    // ---- Вспомогательный метод регистрации блока с автоматическим BlockItem ----
    private static <T extends Block> RegistryObject<T> registerBlock(
            String name,
            Supplier<T> blockSupplier,
            Consumer<Item.Properties> config
    ) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        Item.Properties props = new Item.Properties();
        config.accept(props);
        // Регистрируем BlockItem в реестре предметов
        AEModItem.ITEM.register(name, () -> new BlockItem(block.get(), props));
        return block;
    }

    // Перегрузка без настройки свойств (по умолчанию)
    private static <T extends Block> RegistryObject<T> registerBlock(
            String name,
            Supplier<T> blockSupplier
    ) {
        return registerBlock(name, blockSupplier, p -> {});
    }
}