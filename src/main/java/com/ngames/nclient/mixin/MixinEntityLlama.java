package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ngames.nclient.module.Modules;

import net.minecraft.entity.passive.EntityLlama;

@Mixin(EntityLlama.class)
public class MixinEntityLlama
{
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void canBeSteered(CallbackInfoReturnable<Boolean> info)
    {
    	if (Modules.entityControl.isEnabled())
        {
        	info.setReturnValue(true);
        	info.cancel();
        }
    }
}