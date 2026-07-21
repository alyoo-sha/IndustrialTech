package org.mod.industrialtech_ae.mixin;

import appeng.core.definitions.ItemDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mod.industrialtech_ae.init.AEModItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {ItemDefinition.class}, remap = false)
public abstract class MixinItemDefinition {
    private static final ResourceLocation AE2_BLANK_PATTERN_ID = ResourceLocation.fromNamespaceAndPath("ae2", "blank_pattern");
    @Shadow
    @Final
    private ResourceLocation id;

    @Inject(
            method = {"isSameAs(Lnet/minecraft/world/item/ItemStack;)Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void ifpat$acceptInfinitePattern(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack != null && AE2_BLANK_PATTERN_ID.equals(this.id) && stack.is((Item) AEModItem.ITEM_INFINITE_EMPTY_PATTERN.get())) {
            cir.setReturnValue(true);
        }

    }
}
