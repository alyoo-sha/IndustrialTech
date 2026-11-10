package org.mod.industrialtech;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mod.industrialtech.providers.lang.IEnglishLangProvider;
import org.mod.industrialtech.providers.lang.IRussianLangProvider;
import org.mod.industrialtech.providers.models.IBlockStateProvider;
import org.mod.industrialtech.providers.models.IItemModelProvider;
import org.mod.industrialtech.providers.models.MatterItemModels;
import org.mod.industrialtech.providers.recipe.MatterRecipes;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Industrialtech.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    private DataGen() {}

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
//        generator.addProvider(false, new IItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(true, new MatterItemModels(packOutput, existingFileHelper));

        //Languages Providers
        generator.addProvider(true, new IEnglishLangProvider(packOutput));
        generator.addProvider(true, new IRussianLangProvider(packOutput));
    }

    private static void addServerProviders(DataGenerator generator, PackOutput packOutput,
                                           CompletableFuture<HolderLookup.Provider> lookupProvider,
                                           ExistingFileHelper existingFileHelper) {
        //Recipe & LootTable Providers
        generator.addProvider(true, new MatterRecipes(packOutput));

        //Tag Providers

        //GlobalLootModifications Providers

        //
    }
}
