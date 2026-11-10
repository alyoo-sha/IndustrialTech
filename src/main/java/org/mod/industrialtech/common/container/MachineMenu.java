package org.mod.industrialtech.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.mod.nova_core.menu.MachineLayout;
import org.mod.nova_core.menu.NovaMachineMenu;

/** Общая база меню машин: слот 0 - вход, слот 1 - выход (только забирать). */
public abstract class MachineMenu extends NovaMachineMenu {
    public static final MachineLayout LAYOUT = new MachineLayout(176, 86);
    public static final int SLOT_Y = 36;
    public static final int INPUT_X = 41;
    public static final int OUTPUT_X = 115;

    protected final BlockPos pos;
    protected final ContainerData data;

    protected MachineMenu(MenuType<?> type, int id, Inventory inv, BlockPos pos,
                          IItemHandler handler, ContainerData data) {
        super(type, id, LAYOUT);
        this.pos = pos;
        this.data = data;

        addSlot(new SlotItemHandler(handler, 0, LAYOUT.machineX(INPUT_X), LAYOUT.machineY(SLOT_Y)));
        addOutputSlot(new SlotItemHandler(handler, 1, LAYOUT.machineX(OUTPUT_X), LAYOUT.machineY(SLOT_Y)) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        addPlayerInventory(inv);
        addDataSlots(data);
    }

    /** Накопленная энергия (для отображения). */
    public abstract long energy();

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(Vec3.atCenterOf(pos)) <= 64.0;
    }
}