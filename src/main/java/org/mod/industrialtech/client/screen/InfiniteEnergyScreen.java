package org.mod.industrialtech.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.mod.industrialtech.common.container.InfiniteEnergyMenu;
import org.mod.nova_core.ui.api.Panel;
import org.mod.nova_core.ui.api.TextWidget;
import org.mod.nova_core.ui.minecraft.NovaTexts;
import org.mod.nova_core.ui.minecraft.container.NovaMachineScreen;

/** Окно блока бесконечной энергии: описание и всегда полная красная полоса. */
public class InfiniteEnergyScreen extends NovaMachineScreen<InfiniteEnergyMenu> {

    public InfiniteEnergyScreen(InfiniteEnergyMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void buildMachine(Panel panel) {
        int w = layout().machineWidth();
        panel.add(new TextWidget(8, 19, w - 16, 9, NovaTexts.tr("gui.industrialtech.infinite_source"))
                .setColor(0xFFB8C4DC));
        panel.add(new EnergyBar(8, 34, w - 16, 14,
                () -> 1L, () -> 1L,
                () -> "∞ FE", () -> "∞ FE"));
    }
}