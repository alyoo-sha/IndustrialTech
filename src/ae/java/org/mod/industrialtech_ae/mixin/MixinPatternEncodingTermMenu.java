package org.mod.industrialtech_ae.mixin;

import appeng.api.storage.ITerminalHost;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.parts.encoding.PatternEncodingLogic;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mod.industrialtech_ae.init.AEModItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {PatternEncodingTermMenu.class}, remap = false)
public abstract class MixinPatternEncodingTermMenu extends MEStorageMenu {
    @Final
    @Shadow
    private RestrictedInputSlot encodedPatternSlot;
    @Shadow
    private RestrictedInputSlot blankPatternSlot;
    private boolean wasInfinitePattern = false;

    @Inject(
            method = {"encode"},
            at = {@At("HEAD")}
    )
    private void onEncodePatternHead(CallbackInfo ci) {
        ItemStack blank = this.blankPatternSlot.getItem();
        this.wasInfinitePattern = blank.is((Item) AEModItem.ITEM_INFINITE_EMPTY_PATTERN.get());
    }

    @Inject(
            method = {"isPattern"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onIsPattern(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is((Item)AEModItem.ITEM_INFINITE_EMPTY_PATTERN.get())) {
            cir.setReturnValue(true);
        }

    }

    @Inject(
            method = {"encode"},
            at = {@At("RETURN")}
    )
    private void onEncodePatternReturn(CallbackInfo ci) {
        if (this.wasInfinitePattern) {
            ItemStack blank = this.blankPatternSlot.getItem();
            if (blank.isEmpty()) {
                this.blankPatternSlot.set(((Item)AEModItem.ITEM_INFINITE_EMPTY_PATTERN.get()).getDefaultInstance());
            } else if (blank.is((Item)AEModItem.ITEM_INFINITE_EMPTY_PATTERN.get())) {
                blank.setCount(1);
            }

            this.wasInfinitePattern = false;
        }

    }

    public MixinPatternEncodingTermMenu(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host, PatternEncodingLogic encodingLogic) {
        super(menuType, id, ip, host);
    }
}
