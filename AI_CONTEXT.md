# AI-контекст IndustrialTech

## Назначение

IndustrialTech — мод для Minecraft 1.20.1 на Forge, объединяющий базовые клиентские функции, AE2-хранилища и автоматизацию крафта, а также отдельные контентные наборы для crafting и ore.

Приоритеты подсистем равные: задачу нельзя автоматически считать AE2-задачей только потому, что этот sourceSet сейчас самый большой.

## Предпочтения владельца

- Документы и объяснения: русский язык; имена API, классов, методов и задач оставлять на английском.
- Рабочий стиль AI: сначала краткий план, затем самостоятельное редактирование и запуск релевантных проверок.
- При неоднозначных игровых или архитектурных решениях задавать вопрос до изменения поведения.

## Технический стек

- Minecraft `1.20.1`
- Forge `47.4.10`, loader range `[47,)`
- Java `17`
- Gradle Wrapper, Gradle `8.4`
- Parchment mappings `2023.09.03-1.20.1`
- Mixin `0.8+` через SpongePowered Mixingradle
- AE2 `15.4.10` и набор совместимых модов из [`build.gradle`](./build.gradle)
- Лицензия, указанная в свойствах: MIT
- Основной пакет: `org.mod`

## SourceSet-модули

| SourceSet | Mod ID | Назначение | Точка входа |
|---|---|---|---|
| `main` | `industrialtech` | Общий мод, конфигурация, клиентские события и blur shader | `org.mod.industrialtech.Industrialtech` |
| `ae` | `industrialtech_ae` | AE2 bulk storage, crafting CPU/Service mixin, GUI, networking и guidebook | `org.mod.industrialtech_ae.Industrialtech_ae` |
| `crafting` | `industrialtech_crafting` | Блоки/предметы и creative tab для crafting-контента | `org.mod.industrialtech_crafting.Industrialtech_crafting` |
| `ore` | `industrialtech_ore` | Отдельный будущий/минимальный набор ore-контента | `org.mod.industrialtech_ore.Industrialtech_ore` |

SourceSet-ы `ae`, `crafting` и `ore` получают `main` в compile/runtime classpath. Datagen разделён на `datagenMain`, `datagenAe`, `datagenCrafting`, `datagenOre`.

## Ключевые подсистемы

### Общий мод (`main`)

- Forge entry point и общая `Config`.
- Клиентские события в `client/ModClientForgeEvents`.
- Экран настроек blur в `client/screen`.
- `MixinScreen`.
- Post shader `fade_in_blur` в `src/main/resources/assets/minecraft/shaders/post` и `.../program`.

Blur активируется клиентскими событиями, может быть настроен через экран на Right Shift и исключает экраны через клиентскую конфигурацию.

### AE2 (`ae`)

- `init/IAeMod*` содержит deferred-регистрацию блоков, block entities, предметов, меню, networking и creative tab.
- `ae2/` содержит базовые AE2 inventory/key/storage-абстракции.
- `entity/` содержит `BaseBlockEntity`, `BulkItemChestBlockEntity` и `BulkFluidTankBlockEntity`.
- Block entity хранит один тип AE key с большим количеством, предоставляет Forge item/fluid capability и создаёт меню.
- `menu/`, `client/gui/` и `network/` обеспечивают серверное состояние, клиентскую визуализацию и синхронизацию.
- `mixin/` подключает `MixinPatternEncodingTermMenu`, `MixinRestrictedInputSlot`, `CraftingCPUClusterMixin` и `CraftingServiceMixin`.
- `IAeCraftingBlockEntity` и связанные интерфейсы `me/` расширяют представление AE2 crafting units/CPU.
- `guidebook/IFPATGuide` регистрирует внутриигровую документацию.

### Crafting (`crafting`)

Регистрация разделена по `ICraftModBlock`, `ICraftModItem`, `ICraftModTab` и утилитам базовых блоков/предметов. Контентные blockstate/model/lang/recipe-ресурсы лежат под namespace `industrialtech_crafting`.

### Datagen

Datagen-классы находятся в `src/datagen/*/java`. Результаты генерируются в `src/datagen/generated/<mod-id>` и подключаются как resource directories в `build.gradle`.

## Ресурсы и namespace

- `src/main/resources`: `industrialtech` и общие Minecraft shader assets.
- `src/ae/resources`: `industrialtech_ae`, AE mixin config и guidebook.
- `src/crafting/resources`: `industrialtech_crafting`.
- `src/ore/resources`: `industrialtech_ore`.
- Каждый мод имеет собственный `META-INF/mods.toml`.

## Состояние репозитория

На момент подготовки этого контекста рабочее дерево содержит незакоммиченные изменения пользователя, включая переименования `AEMod*` → `IAeMod*`, новые AE crafting-интерфейсы/mixins и клиентский blur-код. Эти изменения не следует автоматически откатывать или трактовать как завершённую стабильную версию.

В репозитории нет обнаруженных unit/integration test-классов. Проверка в основном выполняется Gradle-компиляцией, запуском клиента/сервера, DataGen и GameTest.
