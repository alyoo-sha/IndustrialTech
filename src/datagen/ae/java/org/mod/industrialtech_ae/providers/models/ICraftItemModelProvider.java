package org.mod.industrialtech_ae.providers.models;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.mod.industrialtech_ae.Industrialtech_ae;

public class ICraftItemModelProvider extends ItemModelProvider {
    public ICraftItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Industrialtech_ae.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        //
    }
}
