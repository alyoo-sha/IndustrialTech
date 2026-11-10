package org.mod.industrialtech.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.mod.industrialtech.common.container.MatterGeneratorMenu;
import org.mod.industrialtech.common.entity.MatterGeneratorBlockEntity;
import org.mod.industrialtech.common.recipe.MatterRecipe;
import org.mod.nova_core.ui.graphics.UiText;
import org.mod.nova_core.ui.minecraft.NovaTexts;

public class MatterGeneratorScreen extends MachineScreen<MatterGeneratorMenu> {

    public MatterGeneratorScreen(MatterGeneratorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected UiText timeLine() {
        return NovaTexts.tr("gui.industrialtech.generator_time");
    }

    @Override
    protected String costLine() {
        ItemStack in = menu.getSlot(0).getItem();
        MatterRecipe r = MatterRecipe.find(Minecraft.getInstance().level, menu.tier().blockId, in);
        return r == null ? "-" : EnergyBar.format(r.energy()) + "FE";
    }

    @Override
    protected float progress() {
        return menu.getSlot(0).hasItem() ? 1f : 0f;
    }

    @Override
    protected long capacity() {
        return MatterGeneratorBlockEntity.CAPACITY;
    }
}