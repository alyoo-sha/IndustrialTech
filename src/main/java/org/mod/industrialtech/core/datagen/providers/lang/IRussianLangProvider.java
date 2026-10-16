package org.mod.industrialtech.core.datagen.providers.lang;

import net.minecraft.data.PackOutput;
import org.mod.industrialtech.Industrialtech;

public class IRussianLangProvider extends ILangProvider {
    public IRussianLangProvider(PackOutput output) {
        super(output, Industrialtech.MOD_ID, "ru_ru");
    }

    @Override
    protected void addTranslations() {
        addBlur("settings", "Настройки размытия");
    }
}
