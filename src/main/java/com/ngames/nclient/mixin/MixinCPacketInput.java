package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.module.Modules;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketInput;

@Mixin(CPacketInput.class)
public class MixinCPacketInput
{
	@Shadow
	private boolean sneaking;
	
	@Inject(method = "writePacketData", at = @At("HEAD"))
	private void writePacketData(PacketBuffer buf, CallbackInfo info)
	{
		if (Modules.sneak.isEnabled() && Modules.sneak.onlyServer.getValue())
			this.sneaking = true;
	}
}
