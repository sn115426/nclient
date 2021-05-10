package com.ngames.nclient.module.modules.movement;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "Allows you to control entities without saddle", 
		name = "EntityControl", 
		words = "EntityControl")
public class EntityControl extends Module
{
	public EntityControl()
	{
		this.settings = Module.addSettings(this);
	}
}
