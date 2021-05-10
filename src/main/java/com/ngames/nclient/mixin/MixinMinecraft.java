package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.RunTickKeyboardEvent;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
	@Inject(method = "runTickKeyboard", at = @At("TAIL"), cancellable = true)
	private void runTickKeyboard(CallbackInfo info)
	{
		if (NClientEvent.callEvent(new RunTickKeyboardEvent()))
			info.cancel();
	}
}
