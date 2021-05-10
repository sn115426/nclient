package com.ngames.nclient.module.modules.movement;

import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.RunTickKeyboardEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "looking in direction that you set", 
		name = "LookingForward", 
		words = "LookingForward Rotate")
public class LookingForward extends Module
{
	private final SettingValue yaw = new SettingValue("Yaw", -90, -180, 180);
	private final SettingValue pitch = new SettingValue("Pitch", 0, -90, 90);
	
	public LookingForward()
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
		Baritone.rotatePlayer(yaw.getValue(), pitch.getValue());
	}
}
