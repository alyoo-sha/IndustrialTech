package org.mod.industrialtech.core.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.matter.MatterTier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class IModItem {
    public static final DeferredRegister<Item> ITEM =
            DeferredRegister.create(ForgeRegistries.ITEMS, Industrialtech.MOD_ID);

    public static void init() {
        ITEM.register(Industrialtech.modEventBus);
    }


    private static final List<RegistryObject<? extends Item>> ALL = new ArrayList<>();
    private static final List<RegistryObject<Item>> OTHERS = new ArrayList<>();

    // ====== Регистрируемые предметы ======
//    public static final RegistryObject<Item> BULK_FLUID_CELL =
//            item("bulk_fluid_cell", BulkFluidCellItem::new, IAeRarity.CUSTOM_COMMON.rarity, p -> p.stacksTo(1));
//    public static final RegistryObject<Item> BULK_ITEM_CELL =
//            item("bulk_item_cell", BulkItemCellItem::new, IAeRarity.CUSTOM_COMMON.rarity, p -> p.stacksTo(1));


    public static final RegistryObject<Item> SCRAP =
            item("scrap", IRarity.CUSTOM_EPIC.rarity);
    public static final RegistryObject<Item> INFINITE_SCRAP_TEMPLATE =
            item(MatterTier.TEMPLATE_ID, IRarity.CUSTOM_MYTHIC.rarity, p -> p.stacksTo(1));




    // ====== Вспомогательные методы (перегрузки) ======

    // 1. Обычный предмет без конфига, только имя + редкость
    private static RegistryObject<Item> item(String name, Rarity rarity) {
        return item(name, Item::new, rarity, p -> {});
    }

    // 1.1 Обычный предмет с редкостью по умолчанию (COMMON)
    private static RegistryObject<Item> item(String name) {
        return item(name, IRarity.CUSTOM_COMMON.rarity);
    }

    // 1.2 Обычный предмет с редкостью и конфигом (использует Item::new)
    private static RegistryObject<Item> item(String name, Rarity rarity, Consumer<Item.Properties> config) {
        return item(name, Item::new, rarity, config);
    }

    // 2. Предмет с кастомной фабрикой (Properties) -> T + редкость + конфиг
    private static <T extends Item> RegistryObject<T> item(
            String name,
            Function<Item.Properties, T> factory,
            Rarity rarity,
            Consumer<Item.Properties> config
    ) {
        Item.Properties props = new Item.Properties().rarity(rarity);
        config.accept(props);
        return register(ITEM, name, () -> factory.apply(props));
    }

    // 2.1 Без редкости – подставляем COMMON (с фабрикой и конфигом)
    private static <T extends Item> RegistryObject<T> item(
            String name,
            Function<Item.Properties, T> factory,
            Consumer<Item.Properties> config
    ) {
        return item(name, factory, Rarity.COMMON, config);
    }

    // Универсальный регистратор
    private static <T extends Item> RegistryObject<T> register(
            DeferredRegister<Item> items,
            String name,
            Supplier<T> supplier
    ) {
        return items.register(name, supplier);
    }

    //<--
    private static RegistryObject<Item> registerComponent(String name, Rarity rarity)
    {
        RegistryObject<Item> obj = registerOtherItem(name, () -> new Item(new Item.Properties().rarity(rarity)));
        return obj;
    }

    //
    private static RegistryObject<Item> registerOtherItem(String name, Supplier<Item> sup)
    {
        RegistryObject<Item> obj = registerItem(name, sup);
        OTHERS.add(obj);
        return obj;
    }

    //
    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier) {
        RegistryObject<T> obj = ITEM.register(name, () -> {
            T t = supplier.get();
            return t instanceof Item ? t : (T) new Item(new Item.Properties());
        });
        ALL.add(obj);
        return obj;
    }
}
