package org.mod.industrialtech.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.mod.industrialtech.common.container.MachineMenu;
import org.mod.nova_core.ui.api.ArrowWidget;
import org.mod.nova_core.ui.api.Panel;
import org.mod.nova_core.ui.api.TextWidget;
import org.mod.nova_core.ui.graphics.UiText;
import org.mod.nova_core.ui.minecraft.container.NovaMachineScreen;

public abstract class MachineScreen<M extends MachineMenu> extends NovaMachineScreen<M> {
    private static final int DIM_TEXT = 0xFFB8C4DC;

    protected MachineScreen(M menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    protected abstract UiText timeLine();

    /** Надпись на полосе энергии, например цена операции. */
    protected abstract String costLine();

    /** Прогресс стрелки 0..1. */
    protected abstract float progress();

    protected abstract long capacity();

    @Override
    protected void buildMachine(Panel panel) {
        int w = layout().machineWidth();

        panel.add(new TextWidget(8, 19, w - 16, 9, timeLine()).setColor(DIM_TEXT));

        // Стрелка по центру между рамкой входа и рамкой выхода
        int left = MachineMenu.INPUT_X - 1 + 18;
        int right = MachineMenu.OUTPUT_X - 5;
        int arrowX = (left + right) / 2 - ArrowWidget.WIDTH / 2;
        int arrowY = MachineMenu.SLOT_Y + 8 - ArrowWidget.HEIGHT / 2;
        ArrowWidget arrow = new ArrowWidget(arrowX, arrowY);
        arrow.setProgressSource(this::progress);
        panel.add(arrow);

        panel.add(new EnergyBar(8, 62, w - 16, 14,
                () -> menu.energy(), this::capacity, this::costLine,
                () -> EnergyBar.format(menu.energy()) + " / " + EnergyBar.format(capacity()) + " FE"));
    }
}