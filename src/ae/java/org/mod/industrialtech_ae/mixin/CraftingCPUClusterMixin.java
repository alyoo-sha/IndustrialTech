package org.mod.industrialtech_ae.mixin;

import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import org.mod.industrialtech_ae.me.IAcceleratorThreadsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CraftingCPUCluster.class, remap = false, priority = 100)
public class CraftingCPUClusterMixin {
    @Shadow
    private int accelerator;

    // 这里已经尽可能保证兼容性，无特定注入点，且只做加法逻辑，要是这能冲突，那一定是别人的问题！
    @Inject(method = "addBlockEntity(Lappeng/blockentity/crafting/CraftingBlockEntity;)V",
            at = @At("HEAD"), require = 0)
    private void industrialtech_ae$addBlockEntity(CraftingBlockEntity te, CallbackInfo ci)
    {
        // 加上我们的自定义线程，绕过ae限制
        if (te instanceof IAcceleratorThreadsProvider provider)
            accelerator += provider.getIAeCustomAcceleratorThreads();
    }
}
