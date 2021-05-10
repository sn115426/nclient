package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ngames.nclient.module.Modules;

import net.minecraft.entity.passive.AbstractHorse;

@Mixin(AbstractHorse.class)
public class MixinAbstractHorse
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

    @Inject(method = "isHorseSaddled", at = @At("HEAD"), cancellable = true)
    public void isHorseSaddled(CallbackInfoReturnable<Boolean> info)
    {
    	if (Modules.entityControl.isEnabled())
        {
        	info.setReturnValue(true);
        	info.cancel();
        }
    }
}