package org.mod.industrialtech.providers.lang;

import net.minecraft.data.PackOutput;
import org.mod.industrialtech.Industrialtech;

public class IRussianLangProvider extends ILangProvider {
    public IRussianLangProvider(PackOutput output) {
        super(output, Industrialtech.MOD_ID, "ru_ru");
    }

    @Override
    protected void addTranslations() {
        addCreativeTab("matter", "Генераторы материи");

        addGui("generator_time", "Время: 1 тик");
        addGui("recycler_time", "Время: 20 тиков");
        addGui("energy_tooltip", "%s / %s FE");
        addGui("infinite_source", "Source: infinite");
        addGui("infinite_output", "∞ FE");

        addBlur("settings", "Настройки размытия");
        addBlur("dark_color", "Цвет затемнения");
        addBlur("clear_color", "Сброс цвета");
        addBlur("success", "Готово");
        addBlur("radius", "Радиус: %s");
        addBlur("blur", "Размытие");
        addBlur("on", "Вкл");
        addBlur("off", "Выкл");
        addBlur("blur_toggle", "Размытие");

        addBlock("matter_generator", "Генератор материи");
        addBlock("neutrino_matter_generator", "Нейтринный генератор материи");
        addBlock("bionic_matter_generator", "Бионический генератор материи");
        addBlock("hyperconductive_matter_generator", "Гиперпроводящий генератор материи");
        addBlock("timeless_matter_generator", "Вневременной генератор материи");
        addBlock("multiphase_matter_generator", "Мультифазный генератор материи");
        addBlock("quantum_matter_generator", "Квантовый генератор материи");
        addBlock("proton_matter_generator", "Протонный генератор материи");
        addBlock("singular_matter_generator", "Сингулярный генератор материи");
        addBlock("perfect_matter_generator", "Совершенный генератор материи");
        addBlock("infinite_energy_block", "Блок Бесконечной Энергии");
        addBlock("recycler", "Утилизатор");

        addItem("scrap", "Утильсырьё");
        addItem("infinite_scrap_template", "Шаблон бесконечного утильсырья");
        addItem("basic_matter", "Материя");
        addItem("neutrino_matter", "Нейтринная материя");
        addItem("bionic_matter", "Бионическая материя");
        addItem("hyperconductive", "Гиперпроводящая материя");
        addItem("timeless_matter", "Вневременная материя");
        addItem("multiphase_matter", "Мультифазная материя");
        addItem("quantum_matter", "Квантовая материя");
        addItem("proton_matter", "Протонная материя");
        addItem("singular_matter", "Сингулярная материя");
        addItem("perfect_matter", "Совершенная материя");
    }
}
