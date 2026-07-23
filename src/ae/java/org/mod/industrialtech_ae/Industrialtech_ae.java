package org.mod.industrialtech_ae;

import appeng.api.storage.StorageCells;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.mod.industrialtech_ae.guidebook.IFPATGuide;
import org.mod.industrialtech_ae.init.*;
import org.mod.industrialtech_ae.item.BulkFluidCellItem;
import org.mod.industrialtech_ae.item.BulkItemCellItem;
import org.mod.industrialtech_ae.util.SDCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Industrialtech_ae.MOD_ID)
public class Industrialtech_ae {

    public static final String MOD_ID = "industrialtech_ae";
    public static final String MOD_NAME = "IndustrialTech: AE";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static IEventBus modEventBus;

    public Industrialtech_ae(FMLJavaModLoadingContext context) {
        modEventBus = context.getModEventBus();
        AEModBlock.init();
        AEModBlockEntity.init();
        AEModItem.init();
        AEModMenu.init();
        AEModNetworking.init();
        AEModTab.init();
        IFPATGuide.init();

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation makeId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StorageCells.addCellHandler(BulkFluidCellItem.HANDLER);
            StorageCells.addCellHandler(BulkItemCellItem.HANDLER);
        });
    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        SDCommand.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
