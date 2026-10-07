package org.mod.industrialtech_ae.init;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.ClientTickingBlockEntity;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.blockentity.crafting.CraftingMonitorBlockEntity;
import com.google.common.base.Preconditions;
import com.wintercogs.ae2omnicells.common.blocks.entities.OmniCraftingBlockEntity;
import com.wintercogs.ae2omnicells.common.init.OCBlockEntities;
import com.wintercogs.ae2omnicells.common.init.OCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import org.mod.industrialtech_ae.block.entities.IAeCraftingBlockEntity;
import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.Industrialtech_ae;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class IAeModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Industrialtech_ae.MOD_ID);
    public static void init() {
        BLOCK_ENTITIES.register(Industrialtech_ae.modEventBus);
    }

    // Поддерживать список, предоставляя функцию поиска, аналогичную AE (по родительскому классу/интерфейсу)
    private static final List<IAeModBlockEntity.DeferredBlockEntityType<?>> ALL = new ArrayList<>();

    // ---- Регистрируемые типы ----
    public static final RegistryObject<BlockEntityType<BulkFluidTankBlockEntity>> BULK_FLUID_TANK_BE =
            registerEntity("bulk_fluid_tank_be", BulkFluidTankBlockEntity::new, IAeModBlock.BULK_FLUID_TANK);

    public static final RegistryObject<BlockEntityType<BulkItemChestBlockEntity>> BULK_ITEM_CHEST_BE =
            registerEntity("bulk_item_chest_be", BulkItemChestBlockEntity::new, IAeModBlock.BULK_ITEM_CHEST);

    // OMNI合成存储器共用此实体类型
    @SuppressWarnings("unchecked")
    public static final IAeModBlockEntity.DeferredBlockEntityType<IAeCraftingBlockEntity> OMNI_CRAFTING_BLOCK_ENTITY = create(
            "omni_crafting_block_entity",
            IAeCraftingBlockEntity.class,
            IAeCraftingBlockEntity::new,
            IAeModBlock.CRAFTING_STORAGES.toArray(new Supplier[0]) // 这里我们自己清楚很安全
    );

    @SuppressWarnings("unchecked")
    public static final IAeModBlockEntity.DeferredBlockEntityType<CraftingMonitorBlockEntity> OMNI_CRAFTING_MONITOR_BLOCK_ENTITY = create(
            "omni_crating_monitor_block_entity",
            CraftingMonitorBlockEntity.class,
            CraftingMonitorBlockEntity::new,
            IAeModBlock.CRAFTING_MONITORS.toArray(new Supplier[0]) // 此处安全
    );

    // ---- Вспомогательный метод регистрации ----
    @SafeVarargs
    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerEntity(
            String name,
            BlockEntityType.BlockEntitySupplier<T> factory,
            Supplier<? extends Block>... blocks
    ) {
        return BLOCK_ENTITIES.register(name, () -> {
            Block[] blockArray = Arrays.stream(blocks)
                    .map(Supplier::get)
                    .toArray(Block[]::new);
            return BlockEntityType.Builder.of(factory, blockArray).build( null);
        });
    }


    /**
     * Используется для регистрации AEBaseBlockEntity: автоматически добавляет тикер и привязывает тип предмета, чтобы задействовать некоторые механизмы AE
     */
    @SafeVarargs
    public static <T extends AEBaseBlockEntity> IAeModBlockEntity.DeferredBlockEntityType<T> create(
            String id,
            Class<T> entityClass,
            IAeModBlockEntity.BlockEntityFactory<T> factory,
            Supplier<? extends AEBaseEntityBlock<?>>... blockSuppliers
    )
    {
        Preconditions.checkArgument(blockSuppliers.length > 0, "At least one block is required");

        var deferred = BLOCK_ENTITIES.register(id, () -> {
            // Сохраняем BlockEntityType для передачи в конструктор BE
            AtomicReference<BlockEntityType<T>> typeRef = new AtomicReference<>();

            BlockEntityType.BlockEntitySupplier<T> supplier = (pos, state) ->
                    factory.create(typeRef.get(), pos, state);

            // Распаковываем блоки
            AEBaseEntityBlock<?>[] blocks = Arrays.stream(blockSuppliers)
                    .map(Supplier::get)
                    .toArray(AEBaseEntityBlock[]::new);

            // Создаём тип BE
            @SuppressWarnings("unchecked")
            Block[] vanillaBlocks = Arrays.stream(blocks).toArray(Block[]::new);

            BlockEntityType<T> type = BlockEntityType.Builder.of(supplier, vanillaBlocks).build(null);
            // Чтобы supplier выше мог получить type
            typeRef.set(type);

            // Привязываем «форму предмета» к этому типу BE (AE использует для карточек памяти/настройки импорта-экспорта/подсказок и т.д.)
            try
            {
                var item = blocks[0].asItem();
                AEBaseBlockEntity.registerBlockEntityItem(type, item);
            }
            catch (Throwable ignored)
            {
            }

            // Автоматически создаём тикеры (если реализованы маркерные интерфейсы AE)
            BlockEntityTicker<T> serverTicker = null;
            if (ServerTickingBlockEntity.class.isAssignableFrom(entityClass))
            {
                serverTicker = (lvl, p, st, be) -> ((ServerTickingBlockEntity) be).serverTick();
            }
            BlockEntityTicker<T> clientTicker = null;
            if (ClientTickingBlockEntity.class.isAssignableFrom(entityClass))
            {
                clientTicker = (lvl, p, st, be) -> ((ClientTickingBlockEntity) be).clientTick();
            }

            // Привязываем «type + tickers + конкретный класс BE» к каждому блоку
            for (var b : blocks)
            {
                @SuppressWarnings("unchecked")
                AEBaseEntityBlock<T> base = (AEBaseEntityBlock<T>) b;
                base.setBlockEntity(entityClass, type, clientTicker, serverTicker);
            }

            return type;
        });

        var wrapped = new IAeModBlockEntity.DeferredBlockEntityType<>(entityClass, deferred);
        ALL.add(wrapped);
        return wrapped;
    }

    /**
     * Удобная перегрузка: использовать, если конструктор BE принимает (BlockPos, BlockState).
     * Внутри игнорирует type и оборачивает двухпараметрический конструктор в фабрику с тремя параметрами.
     */
    @SafeVarargs
    public static <T extends AEBaseBlockEntity> IAeModBlockEntity.DeferredBlockEntityType<T> create(
            String id,
            Class<T> entityClass,
            BiFunction<BlockPos, BlockState, T> ctorPosState,
            Supplier<? extends AEBaseEntityBlock<?>>... blockSuppliers
    )
    {
        return create(id, entityClass, (type, pos, state) -> ctorPosState.apply(pos, state), blockSuppliers);
    }

    /**
     * Возвращает все BlockEntityType, чьи классы-реализации наследуются от baseClass
     */
    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> List<BlockEntityType<? extends T>> getSubclassesOf(Class<T> baseClass)
    {
        var result = new ArrayList<BlockEntityType<? extends T>>();
        for (var t : ALL)
        {
            if (baseClass.isAssignableFrom(t.getBlockEntityClass()))
            {
                result.add((BlockEntityType<? extends T>) t.get());
            }
        }
        return result;
    }

    /**
     * Возвращает все реализации BlockEntityType, которые реализуют указанный интерфейс.
     * В AE используют похожий подход с общим списком ALL для единой регистрации,
     * но здесь предпочтительнее ручное управление.
     */

    public static List<BlockEntityType<?>> getImplementorsOf(Class<?> iface)
    {
        var result = new ArrayList<BlockEntityType<?>>();
        for (var t : ALL)
        {
            if (iface.isAssignableFrom(t.getBlockEntityClass()))
            {
                result.add(t.get());
            }
        }
        return result;
    }


    /**
     * Wrapper type
     */
    public static final class DeferredBlockEntityType<T extends BlockEntity> implements Supplier<BlockEntityType<T>>
    {
        private final Class<T> entityClass;
        private final Supplier<BlockEntityType<T>> delegate;

        private DeferredBlockEntityType(Class<T> cls, Supplier<BlockEntityType<T>> delegate)
        {
            this.entityClass = cls;
            this.delegate = delegate;
        }

        public BlockEntityType<T> get() {return delegate.get();}

        public Class<T> getBlockEntityClass()
        {
            return entityClass;
        }
    }

    /**
     * Интерфейс с конструктором из трёх параметров
     */
    @FunctionalInterface
    public interface BlockEntityFactory<T extends AEBaseBlockEntity>
    {
        T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
    }
}
