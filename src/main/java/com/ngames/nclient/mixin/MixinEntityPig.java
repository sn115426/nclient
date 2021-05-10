package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ngames.nclient.module.Modules;

import net.minecraft.entity.passive.EntityPig;

@Mixin(EntityPig.class)
public class MixinEntityPig
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

    @Inject(method = "getSaddled", at = @At("HEAD"), cancellable = true)
    public void getSaddled(CallbackInfoReturnable<Boolean> info)
    {
    	if (Modules.entityControl.isEnabled())
        {
        	info.setReturnValue(true);
        	info.cancel();
        }
    }
}