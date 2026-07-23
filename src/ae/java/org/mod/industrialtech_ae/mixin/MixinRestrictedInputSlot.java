package org.mod.industrialtech_ae.mixin;

import appeng.menu.slot.RestrictedInputSlot;
import net.minecraft.world.item.ItemStack;
import org.mod.industrialtech_ae.init.AEModItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {RestrictedInputSlot.class}, remap = false)
public abstract class MixinRestrictedInputSlot {

 @Inject(method = {"mayPlace"}, at = {@At("HEAD")}, cancellable = true)
 private void mayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
  System.out.println("RestrictedInputSlot.mayPlace: " + stack);

  if (stack.is(AEModItem.INFINITE_PATTERN.get())) {
   System.out.println("MATCH!");
   cir.setReturnValue(true);
  }

 }
}
