package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.COMBAT, 
		description = "Set your reach distance", 
		name = "Reach", 
		words = "Reach")
public class Reach extends Module
{
	public final SettingValue distance = new SettingValue("Distance", 6.0F, 1, 6);
	
	public Reach()
	{
		this.settings = Module.addSettings(this);
	}
}
