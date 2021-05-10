package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.Baritone;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketPlayer;

@Mixin(CPacketPlayer.PositionRotation.class)
public class MixinCPacketPositionRotation
{	
	@Inject(method = "writePacketData", at = @At("HEAD"), cancellable = true)
	private void writePacketData(PacketBuffer buf, CallbackInfo info)
	{
		if (Baritone.overrideRotation)
		{
			buf.writeDouble(NClient.MC.player.posX);
            buf.writeDouble(NClient.MC.player.getEntityBoundingBox().minY);
            buf.writeDouble(NClient.MC.player.posZ);
			buf.writeFloat(Baritone.yaw);
            buf.writeFloat(Baritone.pitch);
            buf.writeByte(NClient.MC.player.onGround ? 1 : 0);
			info.cancel();
		}
	}
}
