package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Modules;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@Mixin(SPacketPlayerPosLook.class)
public class MixinSPacketPlayerPosLook
{
	@Shadow
	private float yaw;
	@Shadow
	private float pitch;
	
	@Inject(method = "readPacketData", at = @At("TAIL"), cancellable = true)
	private void readPacketData(PacketBuffer buf, CallbackInfo info)
	{
		if (Modules.noRotate.isEnabled())
		{
			this.yaw = NClient.MC.player.rotationYaw;
			this.pitch = NClient.MC.player.rotationPitch;
		}
	}
}
