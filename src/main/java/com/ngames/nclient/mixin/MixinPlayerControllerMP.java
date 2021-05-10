package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerAttackedEntityEvent;
import com.ngames.nclient.module.Modules;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP
{
	@Inject(method = "getBlockReachDistance", at = @At("TAIL"), cancellable = true)
	private void getBlockReachDistance(CallbackInfoReturnable<Float> info)
	{
		if (Modules.reach.isEnabled())
			info.setReturnValue(Modules.reach.distance.getValue());
	}
	
	@Inject(method = "extendedReach", at = @At("TAIL"), cancellable = true)
	private void extendedReach(CallbackInfoReturnable<Boolean> info)
    {
		if (Modules.reach.isEnabled())
			info.setReturnValue(true);
    }
	
	@Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
	private void attackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo info)
	{
		if (NClientEvent.callEvent(new PlayerAttackedEntityEvent ()))
			info.cancel();
	}
	
	@Inject(method = "windowClick", at = @At("HEAD"), cancellable = true)
	private void windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player, CallbackInfoReturnable<ItemStack> info)
	{
		if (Modules.invActionLogger.isEnabled())
			Baritone.displayMessage("windowId: " + windowId + " slotId: " + slotId + " mouseButton: " + mouseButton + " ClickType: " + type.name() + " Player: " + player.getName());
	}
}
