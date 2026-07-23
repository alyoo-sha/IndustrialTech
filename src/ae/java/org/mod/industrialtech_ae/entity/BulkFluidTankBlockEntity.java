package org.mod.industrialtech_ae.entity;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mod.industrialtech_ae.ae2.AeKeyType;
import org.mod.industrialtech_ae.init.AEModBlockEntity;
import org.mod.industrialtech_ae.menu.BulkFluidTankMenu;

public class BulkFluidTankBlockEntity extends BaseBlockEntity<AEFluidKey> {
    private final LazyOptional<IFluidHandler> fluidHandlerCap = LazyOptional.of(this::createFluidHandler);

    public BulkFluidTankBlockEntity(BlockPos pos, BlockState state) {
        super(AEModBlockEntity.BULK_FLUID_TANK_BE.get(), pos, state, AeKeyType.FLUID);
    }

    protected void syncToOpenPlayers() {
        if (this.level != null && !this.level.isClientSide) {
            for(ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                AbstractContainerMenu var4 = serverPlayer.containerMenu;
                if (var4 instanceof BulkFluidTankMenu) {
                    BulkFluidTankMenu menu = (BulkFluidTankMenu)var4;
                    if (menu.getBlockPos().equals(this.worldPosition)) {
                        menu.syncToClient(serverPlayer);
                    }
                }
            }

        }
    }

    public <C> @NotNull LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.FLUID_HANDLER ? this.fluidHandlerCap.cast() : super.getCapability(cap, side);
    }

    public void setRemoved() {
        super.setRemoved();
        this.fluidHandlerCap.invalidate();
    }

    private IFluidHandler createFluidHandler() {
        return new IFluidHandler() {
            public int getTanks() {
                return 1;
            }

            public @NotNull FluidStack getFluidInTank(int tank) {
                AEFluidKey key = BulkFluidTankBlockEntity.this.storage.getStoredKey();
                return key == null ? FluidStack.EMPTY : new FluidStack(key.getFluid(), (int)Math.min(2147483647L, org.mod.industrialtech_ae.entity.BulkFluidTankBlockEntity.this.storage.getAmount()));
            }

            public int getTankCapacity(int tank) {
                return Integer.MAX_VALUE;
            }

            public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
                AEFluidKey filter = BulkFluidTankBlockEntity.this.storage.getFilterKey();
                return filter == null || filter.getFluid() == stack.getFluid();
            }

            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                if (!resource.isEmpty() && this.isFluidValid(0, resource)) {
                    Actionable mode = action.simulate() ? Actionable.SIMULATE : Actionable.MODULATE;
                    return (int) BulkFluidTankBlockEntity.this.storage.insert(AEFluidKey.of(resource.getFluid()), resource.getAmount(), mode);
                } else {
                    return 0;
                }
            }

            public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                AEFluidKey key = BulkFluidTankBlockEntity.this.storage.getStoredKey();
                if (key == null) {
                    return FluidStack.EMPTY;
                } else {
                    Actionable mode = action.simulate() ? Actionable.SIMULATE : Actionable.MODULATE;
                    long extracted = BulkFluidTankBlockEntity.this.storage.extract(key, maxDrain, mode);
                    return extracted <= 0L ? FluidStack.EMPTY : new FluidStack(key.getFluid(), (int)extracted);
                }
            }

            public @NotNull FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                return resource.isEmpty() ? FluidStack.EMPTY : this.drain(resource.getAmount(), action);
            }
        };
    }

    public @NotNull Component getDisplayName() {
        return Component.translatable("block.industrialtech_ae.bulk_fluid_tank");
    }

    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        return new BulkFluidTankMenu(id, inv, this);
    }
}
