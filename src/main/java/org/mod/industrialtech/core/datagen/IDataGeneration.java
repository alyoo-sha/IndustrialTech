package org.mod.industrialtech.core.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.core.datagen.providers.lang.IEnglishLangProvider;
import org.mod.industrialtech.core.datagen.providers.lang.IRussianLangProvider;
import org.mod.industrialtech.core.datagen.providers.models.IBlockStateProvider;
import org.mod.industrialtech.core.datagen.providers.models.IItemModelProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Industrialtech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IDataGeneration {

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
        generator.addProvider(false, new IBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(false, new IItemModelProvider(packOutput, existingFileHelper));

        //Languages Providers
        generator.addProvider(true, new IEnglishLangProvider(packOutput));
        generator.addProvider(true, new IRussianLangProvider(packOutput));
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
