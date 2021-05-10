package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerHealthChangeEvent;

import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateHealth;

@Mixin(SPacketUpdateHealth.class)
public class SPacketUpdateHealthMixin
{
	@Inject(method = "processPacket", at = @At("TAIL"), cancellable = true)
	private void processPacket(INetHandlerPlayClient handler, CallbackInfo info)
	{
		if (NClientEvent.callEvent(new PlayerHealthChangeEvent()))
			info.cancel();
	}
}
