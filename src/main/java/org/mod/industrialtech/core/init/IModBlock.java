package org.mod.industrialtech.core.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.block.InfiniteEnergyBlock;
import org.mod.industrialtech.common.block.MachineBlock;
import org.mod.industrialtech.common.entity.MatterGeneratorBlockEntity;
import org.mod.industrialtech.common.entity.RecyclerBlockEntity;
import org.mod.industrialtech.common.matter.MatterTier;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class IModBlock {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Industrialtech.MOD_ID);

    /** Все зарегистрированные блоки мода. */
    public static final List<RegistryObject<? extends Block>> ALL = new ArrayList<>();

    public static void init() {
        BLOCKS.register(Industrialtech.modEventBus);
        Industrialtech.LOGGER.info("Initialised block");
    }


    public static final RegistryObject<Block> INFINITE_ENERGY =
            registerBlock("infinite_energy_block", InfiniteEnergyBlock::new,
                    p -> p.stacksTo(64));
    // ---- утилизатор
    public static final RegistryObject<Block> RECYCLER = registerBlock("recycler", () ->
            new MachineBlock(null, RecyclerBlockEntity::new,
                    () -> IModBlockEntity.RECYCLER_BE.get()), p -> p.stacksTo(64));



    // ---- 10 блоков генераторов + 10 материй
    public static final Map<MatterTier, RegistryObject<Block>> GENERATORS = new EnumMap<>(MatterTier.class);
    public static final Map<MatterTier, RegistryObject<Item>> MATTER = new EnumMap<>(MatterTier.class);

    static {
        for (MatterTier tier : MatterTier.values()) {
            RegistryObject<Block> block = BLOCKS.register(tier.blockId, () ->
                    new MachineBlock(tier, MatterGeneratorBlockEntity::new, () -> IModBlockEntity.GENERATOR_BE.get()));
            GENERATORS.put(tier, block);
            IModItem.ITEM.register(tier.blockId, () -> new BlockItem(block.get(), new Item.Properties()));
            MATTER.put(tier, IModItem.ITEM.register(tier.outputId, () -> new Item(new Item.Properties())));
        }
    }

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
        IModItem.ITEM.register(name, () -> itemFactory.apply(object.get()));
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
}
