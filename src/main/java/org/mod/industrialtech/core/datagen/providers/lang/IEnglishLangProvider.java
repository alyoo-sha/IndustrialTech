package org.mod.industrialtech.core.datagen.providers.lang;

import net.minecraft.data.PackOutput;
import org.mod.industrialtech.Industrialtech;

public class IEnglishLangProvider extends ILangProvider {
    public IEnglishLangProvider(PackOutput output) {
        super(output, Industrialtech.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlur("settings", "Blur Settings");
    }
}
