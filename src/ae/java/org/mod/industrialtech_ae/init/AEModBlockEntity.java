package org.mod.industrialtech_ae.init;

import org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity;
import org.mod.industrialtech_ae.entity.BulkItemChestBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.Industrialtech_ae;

import java.util.Arrays;
import java.util.function.Supplier;

public class AEModBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Industrialtech_ae.MOD_ID);
    public static void init() {
        BLOCK_ENTITIES.register(Industrialtech_ae.modEventBus);
    }

    // ---- Регистрируемые типы ----
    public static final RegistryObject<BlockEntityType<BulkFluidTankBlockEntity>> BULK_FLUID_TANK_BE =
            registerEntity("bulk_fluid_tank_be", BulkFluidTankBlockEntity::new, AEModBlock.BULK_FLUID_TANK);

    public static final RegistryObject<BlockEntityType<BulkItemChestBlockEntity>> BULK_ITEM_CHEST_BE =
            registerEntity("bulk_item_chest_be", BulkItemChestBlockEntity::new, AEModBlock.BULK_ITEM_CHEST);

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
            return BlockEntityType.Builder.of(factory, blockArray).build(null);
        });
    }
}