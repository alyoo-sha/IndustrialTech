package org.mod.industrialtech_crafting.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_crafting.Industrialtech_crafting;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Industrialtech_crafting.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ICraftModTab {

    // Регистр для креативных вкладок
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Industrialtech_crafting.MOD_ID);

    // Хелпер для создания вкладки
    private static RegistryObject<CreativeModeTab> registerTab(String name, Supplier<ItemStack> icon, String translationKey) {
        return CREATIVE_MODE_TABS.register(name, () -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable(translationKey))
                .build());
    }

    // Объявляем вкладки (для примера – одна общая, можно разбить на несколько)
    public static final RegistryObject<CreativeModeTab> MAIN_TAB =
            registerTab("main_tab", () -> new ItemStack(ICraftModBlock.NETHER_STAR_BLOCK.get()),  "creativetab.industrialtech_crafting.main");

    // Если хотите несколько вкладок – добавьте ещё, например:
    // public static final RegistryObject<CreativeModeTab> STORAGE_TAB = ...

    // Сопоставление: вкладка → список коллекций блоков, которые в неё попадут
    private static final Map<RegistryObject<CreativeModeTab>, List<Collection<RegistryObject<Block>>>> TAB_CONTENTS;

    static {
        TAB_CONTENTS = Map.of(
                MAIN_TAB, List.of(
                        // Все блоки из вашего класса ICraftModBlock
                        List.of(
                                ICraftModBlock.LUMINESSENCE_BLOCK,
                                ICraftModBlock.BLACK_IRON_BLOCK,
                                ICraftModBlock.REDSTONE_INGOT_BLOCK,
                                ICraftModBlock.ENHANCED_REDSTONE_INGOT_BLOCK,
                                ICraftModBlock.ENDER_INGOT_BLOCK,
                                ICraftModBlock.ENHANCED_ENDER_INGOT_BLOCK,
                                ICraftModBlock.CRYSTALTINE_BLOCK,
                                ICraftModBlock.NETHER_STAR_BLOCK,
                                ICraftModBlock.FLUX_STAR_BLOCK,
                                ICraftModBlock.ENDER_STAR_BLOCK
                        )
                )
        );
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        TAB_CONTENTS.forEach((tab, blockCollections) -> {
            if (event.getTab() == tab.get()) {
                blockCollections.forEach(col -> col.forEach(block -> event.accept(block.get())));
            }
        });
    }

    // Метод для вызова из главного класса мода (см. ниже)
    public static void init() {
        CREATIVE_MODE_TABS.register(Industrialtech_crafting.modEventBus);
    }
}