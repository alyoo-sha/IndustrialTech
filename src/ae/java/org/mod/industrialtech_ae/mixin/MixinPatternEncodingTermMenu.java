package org.mod.industrialtech_ae.mixin;

import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.mod.industrialtech_ae.init.IAeModItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {PatternEncodingTermMenu.class})
public abstract class MixinPatternEncodingTermMenu {
    @Shadow(
            aliases = {"blankPatternSlot"},
            remap = false
    )
    private RestrictedInputSlot blankPatternSlot;
    @Unique
    private boolean infinitypattern$wasInfinitePattern;

    public MixinPatternEncodingTermMenu() {
    }

    @Unique
    private void infinitypattern$onEncodePatternHead(CallbackInfo ci) {
        ItemStack blank = this.blankPatternSlot.getItem();
        this.infinitypattern$wasInfinitePattern = !blank.isEmpty() && blank.getItem() == IAeModItem.INFINITE_PATTERN.get();
    }

    @Inject(
            method = {"isPattern"},
            at = {@At("HEAD")},
            cancellable = true,
            remap = false
    )
    private void onIsPattern(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.isEmpty() && stack.getItem() == IAeModItem.INFINITE_PATTERN.get()) {
            cir.setReturnValue(true);
        }

    }

    @Inject(
            method = {"encode"},
            at = {@At("HEAD")},
            remap = false
    )
    private void onEncodePatternHead(CallbackInfo ci) {
        this.infinitypattern$onEncodePatternHead(ci);
    }

    @Inject(
            method = {"encode"},
            at = {@At("RETURN")},
            remap = false
    )
    private void onEncodePatternReturn(CallbackInfo ci) {
        if (this.infinitypattern$wasInfinitePattern) {
            ItemStack blank = this.blankPatternSlot.getItem();
            if (blank.isEmpty()) {
                this.blankPatternSlot.set(new ItemStack((ItemLike)IAeModItem.INFINITE_PATTERN.get()));
            } else if (blank.getItem() == IAeModItem.INFINITE_PATTERN.get()) {
                blank.setCount(1);
            }

            this.infinitypattern$wasInfinitePattern = false;
        }
    }
}
