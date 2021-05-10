package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.module.modules.combat.Velocity;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketEntityVelocity;

@Mixin(SPacketEntityVelocity.class)
public class MixinSPacketEntityVelocity
{	
	@Shadow
	private int entityID;
	@Shadow
	private int motionX;
	@Shadow
	private int motionY;
	@Shadow
	private int motionZ;
	
	@Inject(method = "readPacketData", at = @At("TAIL"), cancellable = true)
	private void readPacketData(PacketBuffer buf, CallbackInfo info)
    {
		Velocity hack = Modules.velocity;
		if (hack.isEnabled() && this.entityID == NClient.MC.player.getEntityId())
		{
			this.motionX = (int) (this.motionX * BUtils.randomInRange(hack.horizontalMin.getValue(), hack.horizontalMax.getValue()));
        	this.motionY = (int) (this.motionY * BUtils.randomInRange(hack.verticalMin.getValue(), hack.verticalMax.getValue()));
        	this.motionZ = (int) (this.motionZ * BUtils.randomInRange(hack.horizontalMin.getValue(), hack.horizontalMax.getValue()));
		}
    }
}
