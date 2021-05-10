package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingString;
import com.ngames.nclient.module.settings.SettingValue;

import static com.ngames.nclient.NClient.MC;

import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerJoinWorldEvent;

@ModuleType(
		category = Category.PLAYER, 
		description = "Automatically relogin on server", 
		name = "AutoRelog", 
		words = "AutoRelog relogin")
public class AutoRelog extends Module
{
	private final SettingString command = new SettingString("Command", "/skypvp", 255);
	private final SettingValue attemps = new SettingValue("Attemps", 100, 1, 100);
	private final SettingValue delay = new SettingValue("Delay", 200, 0, 10000);
	private final SettingValue radius = new SettingValue("SpawnRadius", 1, 0, 30000);
	private final SettingValue x = new SettingValue("X", -5, -30000000, 30000000);
	private final SettingValue y = new SettingValue("Y", 82, -30000000, 30000000);
	private final SettingValue z = new SettingValue("Z", 43, -30000000, 30000000);
	
	private boolean isRun;
	
	public AutoRelog()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(PlayerJoinWorldEvent.class, this);
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (!this.isRun)
		new Thread(() -> {
			this.isRun = true;
			for (int i = 0; i < attemps.getValue(); i++)
			{
				if (!this.isEnabled())
					break;
				BUtils.sleep((int) delay.getValue());
				double x = MC.player.lastTickPosX;
				double y = MC.player.lastTickPosY;
				double z = MC.player.lastTickPosZ;
				if (x > this.x.getValue()-radius.getValue() && x < this.x.getValue()+radius.getValue() && y > this.y.getValue()-radius.getValue() &&
						y < this.y.getValue()+radius.getValue() && z > this.z.getValue()-radius.getValue() && z < this.z.getValue()+radius.getValue())
					MC.player.sendChatMessage(command.getValue());
			}
			this.isRun = false;
		}).start();
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		this.onInvoke(new PlayerJoinWorldEvent());
	}
}
