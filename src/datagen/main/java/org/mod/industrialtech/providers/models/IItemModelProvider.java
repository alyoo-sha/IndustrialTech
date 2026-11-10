package org.mod.industrialtech.providers.models;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.mod.industrialtech.Industrialtech;

public class IItemModelProvider extends ItemModelProvider {
    public IItemModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Industrialtech.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        //
    }
}
