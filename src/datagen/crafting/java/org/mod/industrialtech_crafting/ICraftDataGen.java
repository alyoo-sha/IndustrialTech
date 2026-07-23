package org.mod.industrialtech_crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mod.industrialtech_crafting.providers.lang.ICraftEnglishLangProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Industrialtech_crafting.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ICraftDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Генерация клиентских данных
        if (event.includeClient()) {
            addClientProviders(gen, packOutput, existingFileHelper);
        }
        // Генерация серверных данных
        if (event.includeServer()) {
            addServerProviders(gen, packOutput, lookupProvider, existingFileHelper);
        }
    }

    private static void addClientProviders(DataGenerator gen, PackOutput packOutput,
                                           ExistingFileHelper existingFileHelper) {
//        gen.addProvider(
//                true,
//                new ModBlockStateProvider(packOutput, existingFileHelper)
//        );
//        gen.addProvider(
//                true,
//                new ModItemModelProvider(packOutput, existingFileHelper)
//        );
//
        gen.addProvider(
                true,
                new ICraftEnglishLangProvider(packOutput)
        );
//        gen.addProvider(
//                true,
//                new ModRussianLangProvider(packOutput)
//        );
    }


    private static void addServerProviders(DataGenerator gen, PackOutput packOutput,
                                           CompletableFuture<HolderLookup.Provider> lookupProvider,
                                           ExistingFileHelper existingFileHelper) {
//        gen.addProvider(
//                true,
//                new ModRecipeProvider(packOutput));
//        gen.addProvider(
//                true,
//                ModLootTableProvider.create(packOutput));
//
//        ModBlockTagGenerator blockTagGenerator = gen.addProvider(true,
//                new ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
//
//        gen.addProvider(false,
//                new ModItemTagGenerator(packOutput, lookupProvider,
//                        blockTagGenerator.contentsGetter(), existingFileHelper));
    }
}
