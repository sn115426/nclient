package com.ngames.nclient.module.modules.movement;

import static com.ngames.nclient.NClient.MC;

import com.ngames.nclient.NClient;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.RunTickKeyboardEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "Automatically toggle sprint", 
		name = "AutoSprint", 
		words = "AutoSprint Sprint Speed Strafe")
public class AutoSprint extends Module
{
	private SettingBoolean onlyForward = new SettingBoolean("OnlyForward", true);
	
	public AutoSprint()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(RunTickKeyboardEvent.class, this);
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		boolean fow = MC.gameSettings.keyBindForward.isKeyDown();
		boolean back = MC.gameSettings.keyBindBack.isKeyDown();
		boolean left = MC.gameSettings.keyBindLeft.isKeyDown();
		boolean right = MC.gameSettings.keyBindRight.isKeyDown();
		if ((fow || (!this.onlyForward.getValue() && (fow || back || left || right))) && !NClient.MC.player.isSprinting())
			NClient.MC.player.setSprinting(true);
	}
}
