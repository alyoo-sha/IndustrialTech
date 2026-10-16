package org.mod.industrialtech_crafting.core.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_crafting.Industrialtech_crafting;
import org.mod.industrialtech_crafting.common.item.LuminessenceItem;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ICraftModItem {
    public static final DeferredRegister<Item> ITEM =
            DeferredRegister.create(ForgeRegistries.ITEMS, Industrialtech_crafting.MOD_ID);

    public static void init() {
        ITEM.register(Industrialtech_crafting.modEventBus);
    }

    public static final RegistryObject<LuminessenceItem> LUMINESSENCE = item("luminessence", LuminessenceItem::new, ICraftRarity.CUSTOM_COMMON.rarity);

    // ====== Вспомогательные методы (перегрузки) ======

    // 1. Обычный предмет без конфига, только имя + редкость
    private static RegistryObject<Item> item(String name, Rarity rarity) {
        return item(name, Item::new, rarity, p -> {});
    }

    // 1.1 Обычный предмет с редкостью по умолчанию (COMMON)
    private static RegistryObject<Item> item(String name) {
        return item(name, Rarity.COMMON);
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

//    // 2.2 Предмет с кастомной фабрикой и редкостью, без доп. конфига
    private static <T extends Item> RegistryObject<T> item(
            String name,
            Function<Item.Properties, T> factory,
            Rarity rarity
    ) {
        return item(name, factory, rarity, p -> {});
    }

    // Универсальный регистратор
    private static <T extends Item> RegistryObject<T> register(
            DeferredRegister<Item> items,
            String name,
            Supplier<T> supplier
    ) {
        return items.register(name, supplier);
    }
}
