package com.ngames.nclient.module.modules.movement;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "Change your jumps height", 
		name = "HighJump", 
		words = "HighJump SlimeJump JumpHeight")
public class HighJump extends Module
{
	public SettingValue height = new SettingValue("Height", 2f, 0f, 100f);
	
	public HighJump()
	{
		this.settings = Module.addSettings(this);
	}
}
