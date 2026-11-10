package org.mod.industrialtech_ae;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mod.industrialtech_ae.providers.models.ICraftBlockStateProvider;
import org.mod.industrialtech_ae.providers.models.ICraftItemModelProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Industrialtech_ae.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IAEDataGen {

    /**
     * Обработчик события для генерации данных (Json файлов для ресурса мода).
     *
     * @param event события GatherDataEvent, содержащие параметры генерации.
     */
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //DataGen client
        if (event.includeClient()) {
            addClientProviders(gen, packOutput, existingFileHelper);
        }

        // DataGen server
        if (event.includeServer()) {
            addServerProviders(gen, packOutput, lookupProvider, existingFileHelper);
        }
    }

    private static void addClientProviders(DataGenerator generator, PackOutput packOutput,
                                           ExistingFileHelper existingFileHelper) {
        //Block & Item Providers
        generator.addProvider(false, new ICraftBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(false, new ICraftItemModelProvider(packOutput, existingFileHelper));

        //Languages Providers
    }

    private static void addServerProviders(DataGenerator generator, PackOutput packOutput,
                                           CompletableFuture<HolderLookup.Provider> lookupProvider,
                                           ExistingFileHelper existingFileHelper) {
        //Recipe & LootTable Providers

        //Tag Providers

        //GlobalLootModifications Providers

        //
    }
}
