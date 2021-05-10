package com.ngames.nclient.mixin;

import static com.ngames.nclient.NClient.MC;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Modules;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketDestroyEntities;

@Mixin(SPacketDestroyEntities.class)
public class MixinSPacketDestroyEntities
{
	@Shadow
	private int[] entityIDs;
	
	@Inject(method = "processPacket", at = @At("HEAD"), cancellable = true)
	private void processPacket(INetHandlerPlayClient handler, CallbackInfo info)
	{
		for (int id : entityIDs)
		{
			Entity e = NClient.MC.world.getEntityByID(id);
			if (e instanceof EntityOtherPlayerMP && Modules.autoEz.isEnabled() && Modules.autoEz.targeted.contains(id))
			{
				MC.player.sendChatMessage(Modules.autoEz.message.getValue().replace("{PLAYER}", e.getName()));
				Modules.autoEz.targeted.remove(Integer.valueOf(id));
			}
		}
	}
}
