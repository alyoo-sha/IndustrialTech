package org.mod.industrialtech.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.client.screen.InfiniteEnergyScreen;
import org.mod.industrialtech.client.screen.MatterGeneratorScreen;
import org.mod.industrialtech.client.screen.RecyclerScreen;
import org.mod.industrialtech.core.init.IModMenu;

@Mod.EventBusSubscriber(modid = Industrialtech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MatterClient {
    private MatterClient() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(IModMenu.GENERATOR_MENU.get(), MatterGeneratorScreen::new);
            MenuScreens.register(IModMenu.RECYCLER_MENU.get(), RecyclerScreen::new);
            MenuScreens.register(IModMenu.INFINITE_ENERGY_MENU.get(), InfiniteEnergyScreen::new);
        });
    }
}