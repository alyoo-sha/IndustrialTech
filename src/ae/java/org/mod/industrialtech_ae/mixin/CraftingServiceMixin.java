package org.mod.industrialtech_ae.mixin;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.crafting.CraftingLink;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.service.CraftingService;
import org.mod.industrialtech_ae.common.block.entities.IAeCraftingBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = CraftingService.class, remap = false)
public abstract class CraftingServiceMixin {
    @Shadow
    @Final
    private IGrid grid;

    @Shadow
    @Final
    private Set<CraftingCPUCluster> craftingCPUClusters;

    @Shadow
    public abstract void addLink(CraftingLink link);

    @Inject(method = "updateCPUClusters()V", at = @At("RETURN"), require = 0)
    private void industrialtech_ae$updateCPUClusters(CallbackInfo ci)
    {
        for (var blockEntity : this.grid.getMachines(IAeCraftingBlockEntity.class))
        {
            final CraftingCPUCluster cluster = blockEntity.getCluster();
            if (cluster != null)
            {
                this.craftingCPUClusters.add(cluster);

                ICraftingLink maybeLink = cluster.craftingLogic.getLastLink();
                if (maybeLink != null)
                {
                    this.addLink((CraftingLink) maybeLink);
                }
            }
        }
    }
}
