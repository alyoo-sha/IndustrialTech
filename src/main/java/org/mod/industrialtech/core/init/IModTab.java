package org.mod.industrialtech.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.matter.MatterTier;

public final class IModTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Industrialtech.MOD_ID);
    public static void init() {
        CREATIVE_MODE_TABS.register(Industrialtech.modEventBus);
    }

    public static final RegistryObject<CreativeModeTab> TAB =
            CREATIVE_MODE_TABS.register("matter", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.industrialtech.matter"))
                    .icon(() -> new ItemStack(IModBlock.GENERATORS.get(MatterTier.BASIC).get()))
                    .displayItems((params, output) ->
                            IModItem.ITEM.getEntries().forEach(e -> output.accept(e.get()))
                    ).build());



//    public static final
}
