package org.mod.industrialtech.core.datagen.providers.models;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.mod.industrialtech.Industrialtech;

public class IBlockStateProvider extends BlockModelProvider {
    public IBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Industrialtech.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
