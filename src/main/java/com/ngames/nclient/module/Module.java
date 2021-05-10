package com.ngames.nclient.module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.ngames.nclient.FileUtil;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.gui.Hud;
import com.ngames.nclient.module.settings.Setting;
import com.ngames.nclient.module.settings.SettingBoolean;

public abstract class Module
{
	protected boolean enabled;
	public String displayName = getInfo().name();
	public String description = getInfo().description();
	public List<Setting> settings;
	public InHUDValue inHud;
	public final SettingBoolean hidden = new SettingBoolean("Hidden", false);
	
	public Module()
	{
		this.settings = Module.addSettings(this);
	}
	
	public void onToggle()
	{
		if (enabled)
			onDisable();
		else
			onEnable();
		FileUtil.saveAll();
		Hud.isHacksChanged = true;
	}
	
	public void onDisable()
	{
		this.enabled = false;
	}
	
	public void onEnable()
	{
		this.onUpdate();
		this.enabled = true;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void onUpdate()
	{
		FileUtil.saveAll();
	}
	
	public void onInvoke(NClientEvent event)
	{
		
	}
	
	public static List<Setting> addSettings (Module module)
	{
		List<Setting> ret = new ArrayList<>();
		for (Field f : module.getClass().getDeclaredFields())
		{
			if (f.getType().getSuperclass() == Setting.class)
			{
				f.setAccessible(true);
				try {
					ret.add((Setting) f.get(module));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		ret.add(module.hidden);
		return ret;
	}

	public ModuleType getInfo() {
		if (this.getClass().isAnnotationPresent(ModuleType.class)) {
			return this.getClass().getAnnotation(ModuleType.class);
		}
		throw new IllegalStateException("Module info not present?");
	}
}
