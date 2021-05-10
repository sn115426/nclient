package com.ngames.nclient.module.modules.chat;

import org.apache.commons.lang3.RandomStringUtils;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.SafeThread;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingString;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.CHAT, 
		description = "Sending messages in chat", 
		name = "Spammer", 
		words = "Spammer spam chat")
public class Spammer extends Module
{
	private final SettingString message = new SettingString("Message", "NClient on top!", 255);
	private final SettingValue delay = new SettingValue("Delay", 3100, 0, 86400000);
	private final SettingBoolean random = new SettingBoolean("Random", false);
	
	public Spammer()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		this.onUpdate();
		this.enabled = true;
		new SafeThread (() ->
		{
				NClient.MC.player.sendChatMessage(message.getValue() + (random.getValue() ? " [" + RandomStringUtils.random(10, true, true) + "]" : ""));
				BUtils.sleep((int) delay.getValue());
		}, this).start();
	}
}
