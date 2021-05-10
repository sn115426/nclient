package com.ngames.nclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.gui.Gui;

import net.minecraft.client.gui.GuiMainMenu;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu
{
	@Inject(method = "rotateAndBlurSkybox", at = @At("TAIL"))
	private void rotateAndBlurSkybox (CallbackInfo info)
	{
		Gui.drawBackground();
	}
}
