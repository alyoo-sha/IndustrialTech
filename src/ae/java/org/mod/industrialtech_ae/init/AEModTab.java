package org.mod.industrialtech_ae.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech_ae.Industrialtech_ae;
import org.mod.industrialtech_ae.guidebook.IFPATGuide;

public class AEModTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Industrialtech_ae.MOD_ID);
    public static void init() {
        CREATIVE_MODE_TABS.register(Industrialtech_ae.modEventBus);
    }

    public static final RegistryObject<CreativeModeTab> AE2_BULK_TAB1 =
            CREATIVE_MODE_TABS.register("ae2_bulk_cells", () -> CreativeModeTab.builder()
                    .title(Component.literal("AE2 Bulk Cells"))
                    .icon(() -> new ItemStack((ItemLike) AEModItem.BULK_FLUID_CELL.get()))
                    .displayItems((params, output) -> {
                        output.accept((ItemLike)AEModItem.ITEM_INFINITE_EMPTY_PATTERN.get());
                        output.accept((ItemLike)AEModItem.BULK_COMPONENT.get());
                        output.accept((ItemLike)AEModItem.BULK_ITEM_CELL.get());
                        output.accept((ItemLike)AEModBlock.BULK_ITEM_CHEST.get());
                        output.accept((ItemLike)AEModItem.BULK_FLUID_CELL.get());
                        output.accept((ItemLike)AEModBlock.BULK_FLUID_TANK.get());
                        ItemStack guideItem = IFPATGuide.createGuideItem();
                        if (!guideItem.isEmpty()) {
                            output.accept(guideItem);
                        }
                    }).build());



//    public static final
}
