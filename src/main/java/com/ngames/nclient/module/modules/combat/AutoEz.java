package com.ngames.nclient.module.modules.combat;

import java.util.ArrayList;
import java.util.List;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingString;

@ModuleType(
		category = Category.CHAT, description = "Automatically say EZZ when you kill player", name = "AutoEz", words = "AutoEz KillAura Ez")
public class AutoEz extends Module
{
	public SettingString message = new SettingString("Message", "{PLAYER}, you just got EZZZZZ niggered by NClient!", 255);
	
	public List<Integer> targeted = new ArrayList<>();
	
	public AutoEz()
	{
		this.settings = Module.addSettings(this);
	}
}
