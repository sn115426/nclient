package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.SafeThread;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.InHUDValue;
import com.ngames.nclient.module.ModuleType;

@ModuleType(
		category = Category.COMBAT,
		description = "Display your current CPS", 
		name = "CPSCount", 
		words = "CPS CPSCount KillAura AutoClicker ClickerDelays")
public class CPSCount extends Module
{
	public int clicks;
	public float cps;
	
	public CPSCount()
	{
		this.settings = Module.addSettings(this);
		this.inHud = new InHUDValue(0.0f);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new SafeThread(() -> {
			BUtils.sleep(500);
			this.cps = clicks*2;
			inHud.set(cps);
			this.clicks = 0;
		}, this).start();
	}
}
