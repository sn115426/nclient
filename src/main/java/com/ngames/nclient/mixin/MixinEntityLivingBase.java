package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerJumpEvent;
import com.ngames.nclient.module.Modules;

import net.minecraft.entity.EntityLivingBase;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase
{
	@Inject(method = "getJumpUpwardsMotion", at = @At("HEAD"), cancellable = true)
	private void getJumpUpwardsMotion (CallbackInfoReturnable<Float> info)
	{
		if (Modules.highJump.isEnabled())
		{
			info.setReturnValue(0.42f * Modules.highJump.height.getValue());
		}
	}
	
	@Inject(method = "jump", at = @At("HEAD"), cancellable = true)
	private void jump (CallbackInfo info)
	{
		if (NClientEvent.callEvent(new PlayerJumpEvent()))
			info.cancel();
		
	}
}
