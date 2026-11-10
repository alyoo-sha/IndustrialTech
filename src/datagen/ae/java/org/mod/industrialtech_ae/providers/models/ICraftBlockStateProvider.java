package org.mod.industrialtech_ae.providers.models;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.mod.industrialtech_ae.Industrialtech_ae;

public class ICraftBlockStateProvider extends BlockModelProvider {
    public ICraftBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Industrialtech_ae.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
