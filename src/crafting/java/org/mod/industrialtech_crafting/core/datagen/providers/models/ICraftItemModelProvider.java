package org.mod.industrialtech_crafting.core.datagen.providers.models;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.mod.industrialtech_crafting.Industrialtech_crafting;

public class ICraftItemModelProvider extends ItemModelProvider {
    public ICraftItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Industrialtech_crafting.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        //
    }
}
