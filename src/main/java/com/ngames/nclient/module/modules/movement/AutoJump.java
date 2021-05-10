package com.ngames.nclient.module.modules.movement;

import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.RunTickKeyboardEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "Automatically jumping", 
		name = "AutoJump", 
		words = "AutoJump AntiAFK")
public class AutoJump extends Module
{
	public AutoJump()
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
		Baritone.setJumping(this.enabled);
	}
}
