package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerFoodStatsChangeEvent;

import net.minecraft.util.FoodStats;

@Mixin(FoodStats.class)
public class MixinFoodStats
{
	@Inject(method = "setFoodLevel", at = @At("TAIL"))
	private void setFoodLevel(CallbackInfo info)
	{
		if (NClientEvent.callEvent(new PlayerFoodStatsChangeEvent()))
			info.cancel();
	}
}
