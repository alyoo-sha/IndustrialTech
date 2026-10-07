# Архитектура IndustrialTech

## Общая схема

```text
Minecraft + Forge 1.20.1
        |
        +-- main: Industrialtech
        |     +-- общая Config
        |     +-- client events -> blur post shader
        |     `-- MixinScreen
        |
        +-- ae: Industrialtech_ae
        |     +-- DeferredRegister (blocks/items/BE/menus/network/tab)
        |     +-- AE key storage
        |     +-- block entities -> Forge capabilities
        |     +-- menus/screens <-> packets
        |     +-- AE2 mixins -> crafting CPU/service
        |     `-- guidebook + command
        |
        +-- crafting: Industrialtech_crafting
        |     `-- content registration + assets
        |
        `-- ore: Industrialtech_ore
              `-- отдельный mod entry point/config
```

## Жизненный цикл Forge

Каждый `@Mod` entry point получает `FMLJavaModLoadingContext`, сохраняет mod event bus и регистрирует свои deferred registers. Общие игровые события идут через `MinecraftForge.EVENT_BUS`, а клиентские подписчики ограничены `Dist.CLIENT`.

Для `industrialtech_ae` порядок инициализации принципиально важен:

1. регистрация блоков;
2. регистрация block entities;
3. регистрация предметов;
4. регистрация меню и networking;
5. creative tab и guidebook;
6. common setup с добавлением AE2 cell handlers через `StorageCells.addCellHandler`.

## AE2 bulk storage

`BulkItemChestBlockEntity` и `BulkFluidTankBlockEntity` наследуют `BaseBlockEntity<T>`, где `T` — `AEItemKey` или `AEFluidKey`. Объект storage является источником истины для количества и фильтра.

Внешние API адаптируются так:

- item storage → `ForgeCapabilities.ITEM_HANDLER`;
- fluid storage → `ForgeCapabilities.FLUID_HANDLER`;
- вставка/извлечение → AE2 `Actionable.SIMULATE` или `Actionable.MODULATE`;
- фильтр → `storage.getFilterKey()`;
- GUI → соответствующий `AbstractContainerMenu`;
- изменение на сервере → синхронизация открытых меню с игроками.

При изменении bulk-хранилища необходимо проверить все четыре слоя: storage, capability, menu/network и client GUI.

## AE2 crafting mixins

Конфигурация `src/ae/resources/industrialtech_ae.mixins.json` подключает mixins через `AEMixinPlugin`. Текущая интеграция затрагивает:

- encoding terminal;
- restricted input slot;
- `CraftingCPUCluster`;
- `CraftingService`.

`CraftingServiceMixin` после обновления CPU clusters добавляет к AE2 service кластеры, предоставленные `IAeCraftingBlockEntity`, и восстанавливает crafting link. Любое изменение этой логики требует проверки на сервере, нескольких CPU и перезагрузке мира.

## Клиентский blur

`ModClientForgeEvents` реагирует на:

- Right Shift → открытие `ModBlurSettingsScreen`;
- `ScreenEvent.Init.Post` → включение/выключение эффекта для текущего экрана;
- render tick → обновление uniform `Progress`;
- конфигурацию → uniform `Radius` и fade time.

Поскольку код использует reflection к внутреннему списку `PostChain`, изменение mappings или Minecraft/Forge может сломать blur даже при успешной компиляции. Проверять нужно визуально в клиенте, включая EMI и экраны из списка исключений.

## Конфигурация и данные

Настройки Forge зарегистрированы через `ModLoadingContext` и `Config.SPEC` в каждом mod entry point. Ресурсы сгенерированы/поддерживаются соответствующими datagen provider-классами; ручная правка generated-файлов может быть перезаписана.

## Границы изменения

- Общую механику и shader менять в `main`.
- AE2 API/storage/mixin менять только в `ae`.
- Новые crafting-блоки и предметы добавлять в `crafting`.
- Ore-контент не смешивать с `main` или `crafting`.
- Если общий тип нужен нескольким sourceSet-ам, сначала проверить, действительно ли он должен жить в `main`, а не дублироваться или передаваться через API.

