package com.ngames.nclient.module.modules.chat;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingString;

@ModuleType(
		category = Category.CHAT, 
		description = "Add prefix in your messages", 
		name = "ChatPrefix", 
		words = "ChatPrefix prefix ChatModdifications Chat")
public class ChatPrefix extends Module
{
	public SettingString prefix = new SettingString("Prefix", " > ", 255);
	
	public ChatPrefix()
	{
		this.settings = Module.addSettings(this);
	}
}
