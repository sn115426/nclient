package com.ngames.nclient.module.modules.chat;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingString;

@ModuleType(
		category = Category.CHAT, 
		description = "Add postfix in your messages", 
		name = "ChatPostfix", 
		words = "ChatPostfix Postfix ChatModdifications Chat")
public class ChatPostfix extends Module
{
	public SettingString postfix = new SettingString("Postfix", "\u0020\u00bb\u0020\u0274\u002d\u1d04\u029f\u026a\u1d07\u0274\u1d1b", 255);
	
	public ChatPostfix()
	{
		this.settings = Module.addSettings(this);
	}
}
