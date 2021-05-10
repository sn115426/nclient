package com.ngames.nclient.module.modules.movement;

import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.baritone.SafeThread;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "Automatically spin around", 
		name = "AutoSpin", 
		words = "AutoSpin Spin SpinAround")
public class AutoSpin extends Module
{
	private final SettingValue speed = new SettingValue("Speed", 3, 1, 10);
	private final SettingBoolean onlyServer = new SettingBoolean("OnlyServer", true);
	
	public AutoSpin()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		Baritone.refreshRotation();
		Baritone.overrideRotation = true;
		new SafeThread (() -> {
				Baritone.addRotationPlayer(0.1f*11-speed.getValue(), 0, onlyServer.getValue());
				BUtils.sleep((long) (11-speed.getValue()));
				Baritone.overrideRotation = true;
		}, this).start();
	}
	
	@Override
	public void onDisable()
	{
		Baritone.overrideRotation = false;
		Baritone.refreshRotation();
		super.onDisable();
	}
}
