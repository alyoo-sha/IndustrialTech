package org.mod.industrialtech.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.mod.industrialtech.common.container.RecyclerMenu;
import org.mod.industrialtech.common.entity.RecyclerBlockEntity;
import org.mod.nova_core.ui.graphics.UiText;
import org.mod.nova_core.ui.minecraft.NovaTexts;

public class RecyclerScreen extends MachineScreen<RecyclerMenu> {

    public RecyclerScreen(RecyclerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected UiText timeLine() {
        return NovaTexts.tr("gui.industrialtech.recycler_time");
    }

    @Override
    protected String costLine() {
        return (RecyclerBlockEntity.FE_PER_TICK * RecyclerBlockEntity.TIME) + "FE";
    }

    @Override
    protected float progress() {
        return menu.progress() / (float) RecyclerBlockEntity.TIME;
    }

    @Override
    protected long capacity() {
        return RecyclerBlockEntity.CAPACITY;
    }
}