package com.ngames.nclient.module.modules.movement;

import java.lang.reflect.Field;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.OnPlayerUpdateEvent;
import com.ngames.nclient.event.NClientEvent.PlayerJoinWorldEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;

import net.minecraft.client.entity.EntityPlayerSP;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "Automatically press shift (sneaking)", 
		name = "Sneak", 
		words = "Sneak AutoSneak Shift")
public class Sneak extends Module
{
	public SettingBoolean onlyServer = new SettingBoolean("OnlyServer", true);
	
	public Sneak()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(OnPlayerUpdateEvent.class, this);
		new Listener(PlayerJoinWorldEvent.class, this);
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (event instanceof OnPlayerUpdateEvent && !this.onlyServer.getValue())
			Baritone.setSneaking(this.enabled);
		if (event instanceof PlayerJoinWorldEvent && this.enabled && this.onlyServer.getValue())
		{
			Field f = null;
			try {
				f = EntityPlayerSP.class.getDeclaredField("field_175170_bN");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			f.setAccessible(true);
			try {
				f.setBoolean(NClient.MC.player, false);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
