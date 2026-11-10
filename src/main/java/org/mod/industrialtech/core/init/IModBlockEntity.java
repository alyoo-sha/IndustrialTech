package org.mod.industrialtech.core.init;

import appeng.blockentity.AEBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.entity.BlockEntityInfiniteEnergy;
import org.mod.industrialtech.common.entity.MatterGeneratorBlockEntity;
import org.mod.industrialtech.common.entity.RecyclerBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.mod.industrialtech.core.init.IModBlock.*;

public final class IModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Industrialtech.MOD_ID);
    public static void init() {
        BLOCK_ENTITIES.register(Industrialtech.modEventBus);
    }

    private static final List<IModBlockEntity.DeferredBlockEntityType<?>> ALL = new ArrayList<>();

    // ---- BlockEntity
    public static final RegistryObject<BlockEntityType<BlockEntityInfiniteEnergy>> INFINITE_ENERGY_BE =
            registerEntity("infinite_energy", BlockEntityInfiniteEnergy::new, INFINITE_ENERGY);

    public static final RegistryObject<BlockEntityType<RecyclerBlockEntity>> RECYCLER_BE =
            registerEntity("recycler", RecyclerBlockEntity::new, RECYCLER);

    // 10 блоков в Map, поэтому помощник с varargs здесь не подходит: регистрируем напрямую
    public static final RegistryObject<BlockEntityType<MatterGeneratorBlockEntity>> GENERATOR_BE =
            BLOCK_ENTITIES.register("matter_generator", () -> BlockEntityType.Builder.of(
                    MatterGeneratorBlockEntity::new,
                    GENERATORS.values().stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));




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
