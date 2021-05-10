package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.LivingUpdatedEvent;
import com.ngames.nclient.event.NClientEvent.OnPlayerUpdateEvent;
import com.ngames.nclient.event.NClientEvent.OnPlayerUpdatedEvent;
import com.ngames.nclient.event.NClientEvent.PlayerSwingArmEvent;
import com.ngames.nclient.event.NClientEvent.PotionEffectRemovedEvent;
import com.ngames.nclient.module.Modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP
{
	@Inject(method = "swingArm", at = @At("TAIL"), cancellable = true)
	private void swingArm(EnumHand hand, CallbackInfo info)
	{
		if (hand != EnumHand.OFF_HAND && Modules.criticals.isEnabled() && Modules.criticals.type.getValue() == 2)
		{
			NClient.MC.player.swingArm(EnumHand.OFF_HAND);
			info.cancel();
		}
		if (NClientEvent.callEvent(new PlayerSwingArmEvent()))
			info.cancel();
	}
	
	@Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
	private void onUpdate(CallbackInfo info)
	{
		if (NClientEvent.callEvent(new OnPlayerUpdateEvent()))
			info.cancel();
	}
	
	@Inject(method = "onUpdate", at = @At("TAIL"), cancellable = true)
	private void onUpdated(CallbackInfo info)
	{
		if (NClientEvent.callEvent(new OnPlayerUpdatedEvent()))
			info.cancel();
	}
	
	@Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
	private void isSneaking (CallbackInfoReturnable<Boolean> info)
	{
		if (Modules.sneak.isEnabled() && Modules.sneak.onlyServer.getValue())
		{
			info.setReturnValue(true);
			info.cancel();
		}
	}
	
	@Inject(method = "onLivingUpdate", at = @At("HEAD"), cancellable = true)
	private void onLivingUpdate (CallbackInfo info)
	{
		if (NClientEvent.callEvent(new LivingUpdatedEvent()))
			info.cancel();
	}
	
	@Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
	private void onUpdateWalkingPlayer (CallbackInfo info)
	{
		if (Baritone.serverSprintingState != Baritone.serverSprinting && Baritone.overrideSprinting)
		{
			if (Baritone.serverSprinting)
            {
                NClient.MC.getConnection().sendPacket(new CPacketEntityAction(NClient.MC.player, CPacketEntityAction.Action.START_SPRINTING));
            }
            else
            {
            	NClient.MC.getConnection().sendPacket(new CPacketEntityAction(NClient.MC.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
			Baritone.serverSprintingState = Baritone.serverSprinting;
		}
	}
	
	@Inject(method = "removeActivePotionEffect", at = @At("HEAD"), cancellable = true)
	private void removeActivePotionEffect (CallbackInfoReturnable<PotionEffect> info)
	{
		if (NClientEvent.callEvent(new PotionEffectRemovedEvent()))
			info.cancel();
	}
}
