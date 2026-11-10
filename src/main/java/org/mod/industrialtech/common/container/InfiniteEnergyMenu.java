package org.mod.industrialtech.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.mod.industrialtech.core.init.IModMenu;
import org.mod.nova_core.menu.MachineLayout;
import org.mod.nova_core.menu.NovaMachineMenu;

public class InfiniteEnergyMenu extends NovaMachineMenu {
    public static final MachineLayout LAYOUT = new MachineLayout(176, 60);

    private final BlockPos pos;

    /** Клиент. */
    public InfiniteEnergyMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos());
    }

    /** Сервер. */
    public InfiniteEnergyMenu(int id, Inventory inv, BlockPos pos) {
        super(IModMenu.INFINITE_ENERGY_MENU.get(), id, LAYOUT);
        this.pos = pos;
        addPlayerInventory(inv);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(Vec3.atCenterOf(pos)) <= 64.0;
    }
}