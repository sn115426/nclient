package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.COMBAT, 
		description = "Change your knockback power", 
		name = "Velocity", 
		words = "Velocity AntiKnockback")
public class Velocity extends Module
{
	public SettingValue verticalMin = new SettingValue("HorizontalMin", 0, 0, 10);
	public SettingValue horizontalMin = new SettingValue("HorizontalMax", 0.7f, 0, 10);
	public SettingValue verticalMax = new SettingValue("VerticalMin", 0, 0, 10);
	public SettingValue horizontalMax = new SettingValue("VerticalMax", 0.7f, 0, 10);
	
	public Velocity()
	{
		this.settings = Module.addSettings(this);
	}
}
