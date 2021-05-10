package com.ngames.nclient.module.modules.render;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;

@ModuleType(
		category = Category.RENDER, 
		description = "Disable rending something that you will choose", 
		name = "NoRender", 
		words = "NoRender overlay antioverlay hide")
public class NoRender extends Module
{
	public final SettingBoolean item = new SettingBoolean("Item", true);
	public final SettingBoolean entity = new SettingBoolean("Entity", true);
	public final SettingBoolean other = new SettingBoolean("Other", true);
	
	public NoRender()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Thread(() -> {
			for (Entity e : NClient.MC.world.getLoadedEntityList())
			{
				if (this.item.getValue() && e instanceof EntityItem)
					e.onKillCommand();
				if (this.entity.getValue() && e instanceof EntityLiving && !(e instanceof EntityOtherPlayerMP))
					e.onKillCommand();
				if (this.other.getValue() && !(e instanceof EntityOtherPlayerMP) && !(e instanceof EntityLiving) && !(e instanceof EntityItem))
					e.onKillCommand();
			}
		}).start();
	}
}
