package com.ngames.nclient.module.modules.combat;

import java.util.ArrayList;
import java.util.List;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.baritone.SafeThread;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.RunTickKeyboardEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.COMBAT, 
		description = "Automatically clicking when mouse button is down", 
		name = "AutoClicker", 
		words = "AutoClicker KillAura")
public class AutoClicker extends Module
{	
	private final SettingChoose DelayType = new SettingChoose("DelayType", (byte) 1, new String[] {
			"simple",
			"random",
			"advanced"
		});
	private final SettingValue CPSMin = new SettingValue("CPSMin", 17, 1, 100);
	private final SettingValue CPSMax = new SettingValue("CPSMax", 21, 1, 100);
	private final SettingChoose ClickType = new SettingChoose("ClickType", (byte) 1, new String[] {
			"legit",
			"AAC"
	});
	private final SettingBoolean TickSync = new SettingBoolean("TickSync", true);
	
	boolean waitClick;
	
	public AutoClicker()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(RunTickKeyboardEvent.class, this);
		new SafeThread(() -> {
				List<Integer> delays = new ArrayList<>();
				if (this.DelayType.getValue() == 0)
					delays = BUtils.genSimpleDelays(CPSMax.getValue());
				else if (this.DelayType.getValue() == 1)
					delays = BUtils.genDelayNoise(BUtils.randomInRange(Math.round(CPSMin.getValue()), Math.round(CPSMax.getValue())));
				else if (this.DelayType.getValue() == 2)
					delays = Modules.advClickerDelays.genAdvancedDelayNoise(CPSMin.getValue(), CPSMax.getValue());
				for (int delay : delays)
				{
					if (this.enabled)
					{
						if (NClient.MC.world != null && !Baritone.clickSync && NClient.MC.objectMouseOver.entityHit != null && NClient.isLeftPressed)
						{
							if (this.TickSync.getValue())
								waitClick = true;
							else
								attack();
						}
						BUtils.sleep(delay, BUtils.randomInRange(0, 999999));
					} else
						break;
				}
		}, this).start();
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (this.enabled)
		{
			if (event instanceof RunTickKeyboardEvent && this.waitClick)
			{
				attack();
				this.waitClick = false;
			}
		}
	}
	
	private void attack()
	{
		if (this.ClickType.getValue() == 0)
			Baritone.leftClickMouse();
		else if (this.ClickType.getValue() == 1)
			Baritone.attackEntity();
	}
}
