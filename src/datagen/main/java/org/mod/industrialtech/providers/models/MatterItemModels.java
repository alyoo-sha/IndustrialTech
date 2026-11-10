package org.mod.industrialtech.providers.models;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.mod.industrialtech.Industrialtech;
import org.mod.industrialtech.common.matter.MatterTier;

public final class MatterItemModels extends ItemModelProvider {

    public MatterItemModels(PackOutput out, ExistingFileHelper helper) {
        super(out, Industrialtech.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        flat("scrap");
        flat(MatterTier.TEMPLATE_ID);
        for (MatterTier tier : MatterTier.values()) {
            flat(tier.outputId);
        }
    }

    private void flat(String id) {
        withExistingParent(id, "item/generated").texture("layer0", modLoc("item/" + id));
    }
}
