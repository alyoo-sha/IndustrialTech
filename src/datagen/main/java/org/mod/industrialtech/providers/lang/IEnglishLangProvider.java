package org.mod.industrialtech.providers.lang;

import net.minecraft.data.PackOutput;
import org.mod.industrialtech.Industrialtech;

public class IEnglishLangProvider extends ILangProvider {
    public IEnglishLangProvider(PackOutput output) {
        super(output, Industrialtech.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addCreativeTab("matter", "Matter Generators");

        addGui("generator_time", "Time: 1 tick");
        addGui("recycler_time", "Time: 20 ticks");
        addGui("energy_tooltip", "%s / %s FE");
        addGui("infinite_source", "Источник: бесконечный");
        addGui("infinite_output", "∞ FE");

        addBlur("settings", "Blur Settings");
        addBlur("dark_color", "Darkening color");
        addBlur("clear_color", "Clear color");
        addBlur("success", "Success");
        addBlur("radius", "Radius: %s");
        addBlur("blur", "Blur");
        addBlur("on", "On");
        addBlur("off", "Off");
        addBlur("blur_toggle", "Blur");

        addBlock("matter_generator", "Matter Generator");
        addBlock("neutrino_matter_generator", "Neutrino Matter Generator");
        addBlock("bionic_matter_generator", "Bionic Matter Generator");
        addBlock("hyperconductive_matter_generator", "Hyperconductive Matter Generator");
        addBlock("timeless_matter_generator", "Timeless Matter Generator");
        addBlock("multiphase_matter_generator", "Multiphase Matter Generator");
        addBlock("quantum_matter_generator", "Quantum Matter Generator");
        addBlock("proton_matter_generator", "Proton Matter Generator");
        addBlock("singular_matter_generator", "Singular Matter Generator");
        addBlock("perfect_matter_generator", "Perfect Matter Generator");
        addBlock("infinite_energy_block", "Infinite Energy Block");
        addBlock("recycler", "Recycler");

        addItem("scrap", "Scrap");
        addItem("infinite_scrap_template", "Infinite Scrap Template");
        addItem("basic_matter", "Matter");
        addItem("neutrino_matter", "Neutrino Matter");
        addItem("bionic_matter", "Bionic Matter");
        addItem("hyperconductive", "Hyperconductive Matter");
        addItem("timeless_matter", "Timeless Matter");
        addItem("multiphase_matter", "Multiphase Matter");
        addItem("quantum_matter", "Quantum Matter");
        addItem("proton_matter", "Proton Matter");
        addItem("singular_matter", "Singular Matter");
        addItem("perfect_matter", "Perfect Matter");
    }
}
