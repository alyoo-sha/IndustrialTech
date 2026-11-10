package org.mod.industrialtech.common.matter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.mod.industrialtech.Industrialtech;

public enum  MatterTier {
    BASIC("matter_generator", "scrap", "basic_matter"),
    NEUTRINO("neutrino_matter_generator", "basic_matter", "neutrino_matter"),
    BIONIC("bionic_matter_generator", "neutrino_matter", "bionic_matter"),
    HYPERCONDUCTIVE("hyperconductive_matter_generator", "bionic_matter", "hyperconductive_matter"),
    TIMELESS("timeless_matter_generator", "hyperconductive_matter", "timeless_matter"),
    MULTIPHASE("multiphase_matter_generator", "timeless_matter", "multiphase_matter"),
    QUANTUM("quantum_matter_generator", "multiphase_matter", "quantum_matter"),
    PROTON("proton_matter_generator", "quantum_matter", "proton_matter"),
    SINGULAR("singular_matter_generator", "proton_matter", "singular_matter"),
    PERFECT("perfect_matter_generator", "singular_matter", "perfect_matter");

    public static final String TEMPLATE_ID = "infinite_scrap_template";

    public final String blockId;
    public final String inputId;
    public final String outputId;
    /** Затраты FE за одну операцию. */

    MatterTier(String blockId, String inputId, String outputId) {
        this.blockId = blockId;
        this.inputId = inputId;
        this.outputId = outputId;
    }

    private static Item item(String id) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Industrialtech.MOD_ID, id));
    }

    public Item output() {
        return item(outputId);
    }

    /** Шаблон работает только в базовом генераторе и не расходуется. */
    public boolean isTemplate(ItemStack stack) {
        return this == BASIC && stack.is(item(TEMPLATE_ID));
    }

    public boolean accepts(ItemStack stack) {
        return !stack.isEmpty() && (stack.is(item(inputId)) || isTemplate(stack));
    }
}
