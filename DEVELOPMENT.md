# Разработка IndustrialTech

## Требования

- Windows PowerShell.
- JDK 17.
- Запускать Gradle через `gradlew.bat`.
- Для IDE использовать импорт Gradle-проекта, а не вручную собирать classpath.

## Основные команды

```powershell
# Список задач
.\gradlew.bat tasks --all

# Компиляция и стандартная сборка
.\gradlew.bat build

# Клиент Minecraft
.\gradlew.bat Client

# Dedicated server
.\gradlew.bat Server

# Datagen для всех sourceSet-ов
.\gradlew.bat DataGen

# GameTest server
.\gradlew.bat GameTest
```

Рабочая директория клиента/сервера — `run/`, а DataGen — `run-data/`. Эти директории локальные и не должны быть источником проектной документации.

## Проверка изменений

1. Выполнить `git status --short`.
2. Проверить компиляцию `.\gradlew.bat build`.
3. При изменении ресурсов или provider-классов выполнить `.\gradlew.bat DataGen`.
4. При изменении AE2 storage/menu/network запустить `.\gradlew.bat Client` и проверить вставку, извлечение, фильтр, перезаход в GUI и сохранение мира.
5. При изменении mixin-кода проверить клиент и dedicated server; искать `Mixin apply failed` в `run/logs/latest.log`.
6. При изменении blur проверить Right Shift, исключённые экраны, EMI, смену экранов и возврат исходного post-effect.

## Где искать файлы

| Что меняется | Каталог |
|---|---|
| Общая Forge-логика | `src/main/java/org/mod/industrialtech` |
| Общие ресурсы и shader | `src/main/resources` |
| AE2 | `src/ae/java/org/mod/industrialtech_ae` |
| AE2 ресурсы/mixins | `src/ae/resources` |
| Crafting content | `src/crafting/java` и `src/crafting/resources` |
| Ore content | `src/ore/java` и `src/ore/resources` |
| Datagen | `src/datagen/<set>/java` |
| Generated resources | `src/datagen/generated` |

## Соглашения

- Идентификаторы ресурсов используют соответствующий mod namespace.
- Регистрацию держать в `init`-классах и подключать через entry point.
- Сервер не должен загружать клиентские классы; client-only код ограничивать `Dist.CLIENT` и клиентскими пакетами.
- Для сетевых пакетов явно разделять обработку на сервере и клиенте и проверять владельца/позицию блока.
- Для storage сначала поддерживать `SIMULATE`, затем выполнять `MODULATE`.
- Не добавлять `catch (Exception)` без явной причины и логирования: ошибка mixin, networking или сохранения должна быть видна.

## Известные риски

- В `build.gradle` подключено много внешних AE2/Forge-модов; изменение версий может менять API и порядок загрузки.
- Reflection в blur зависит от obfuscated field `PostChain`.
- Mixin targets зависят от версии AE2 и mappings.
- В рабочем дереве есть текущие незакоммиченные изменения, поэтому результат `build` может отражать незавершённую разработку.
- Автотестов в исходниках не обнаружено; ручная проверка игрового поведения обязательна для stateful-кода.

