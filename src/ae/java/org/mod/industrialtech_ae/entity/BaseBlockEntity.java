package org.mod.industrialtech_ae.entity;

import appeng.api.networking.*;
import appeng.api.stacks.AEKey;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import org.mod.industrialtech_ae.ae2.AeKeyType;
import org.mod.industrialtech_ae.ae2.BaseBlockInventory;
import org.mod.industrialtech_ae.ae2.GenericAeKeyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

public abstract class BaseBlockEntity<T extends AEKey> extends BlockEntity implements IInWorldGridNodeHost, IStorageProvider, MenuProvider {
    protected final GenericAeKeyStorage<T> storage;
    protected final BaseBlockInventory<T> adapter;
    protected final IManagedGridNode gridNode;
    protected boolean online = false;
    private final LazyOptional<IInWorldGridNodeHost> gridNodeCap = LazyOptional.of(() -> this);
    public static final Capability<IInWorldGridNodeHost> GRID_NODE_HOST_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, AeKeyType<T> keyType) {
        super(type, pos, state);
        this.storage = new GenericAeKeyStorage<>(keyType, null, this::onStorageChanged);
        this.adapter = new BaseBlockInventory<>(this.storage);
        this.gridNode = GridHelper.createManagedNode(this, new IGridNodeListener<>() {
            public void onSaveChanges(org.mod.industrialtech_ae.entity.BaseBlockEntity<T> tBaseBlockEntity, IGridNode iGridNode) {
            }

            public void onStateChanged(org.mod.industrialtech_ae.entity.BaseBlockEntity<T> be, IGridNode node, IGridNodeListener.State state) {
                be.updateOnlineState();
            }
        }).setVisualRepresentation(state.getBlock()).setInWorldNode(true).setTagName("proxy_node").setFlags(GridFlags.REQUIRE_CHANNEL).setExposedOnSides(EnumSet.allOf(Direction.class)).setIdlePowerUsage(0.1);
        this.gridNode.addService(IStorageProvider.class, this);
    }

    public void updateOnlineState() {
        if (this.level != null && !this.level.isClientSide) {
            boolean currentState = this.gridNode.isReady() && this.gridNode.isOnline() && this.gridNode.isPowered();
            if (this.online != currentState) {
                this.online = currentState;
                this.onStorageChanged();
                this.setChanged();
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }

    }

    public void onStorageChanged() {
        this.setChanged();
        if (this.gridNode.isReady()) {
            (Objects.requireNonNull(this.gridNode.getGrid())).getStorageService().refreshNodeStorageProvider(this.gridNode.getNode());
        }

        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            this.syncToOpenPlayers();
        }

    }

    protected abstract void syncToOpenPlayers();

    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide) {
            this.gridNode.create(this.level, this.worldPosition);
        }

    }

    public void setRemoved() {
        super.setRemoved();
        if (this.gridNode != null) {
            this.gridNode.destroy();
        }

        this.gridNodeCap.invalidate();
    }

    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (this.gridNode != null) {
            this.gridNode.destroy();
        }

    }

    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("storage")) {
            this.storage.load(tag.getCompound("storage"));
        }

        this.online = tag.getBoolean("online");
        if (this.gridNode != null) {
            this.gridNode.loadFromNBT(tag);
        }

    }

    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag storageTag = new CompoundTag();
        this.storage.save(storageTag);
        tag.put("storage", storageTag);
        tag.putBoolean("online", this.online);
        if (this.gridNode != null) {
            this.gridNode.saveToNBT(tag);
        }

    }

    public void saveToItemStack(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        tag.putBoolean("online", false);
        stack.setTag(tag);
    }

    public void mountInventories(IStorageMounts iStorageMounts) {
        iStorageMounts.mount(this.adapter);
    }

    public @Nullable IGridNode getGridNode(Direction direction) {
        return this.gridNode.getNode();
    }

    public <C> @NotNull LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
        return cap == GRID_NODE_HOST_CAP ? this.gridNodeCap.cast() : super.getCapability(cap, side);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.saveAdditional(tag);
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.load(tag);
        }

        if (this.level != null && this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 8);
        }

    }

    public GenericAeKeyStorage<T> getStorage() {
        return this.storage;
    }

    public boolean isOnline() {
        return this.online;
    }
}
