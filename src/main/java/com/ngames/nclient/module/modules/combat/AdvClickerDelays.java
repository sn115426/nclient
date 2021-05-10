package com.ngames.nclient.module.modules.combat;

import java.util.List;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.COMBAT, 
		description = "Generate click delays for hacks that use advanced click delays", 
		name = "AdvClickerDelays", 
		words = "AdvClickerDelays AutoClicker KillAura18 KillAura19 KillAura")
public class AdvClickerDelays extends Module
{
	public final SettingValue ADSpeed = new SettingValue("ADSpeed", 700, 1, 10000);
	public final SettingValue ADValueRange = new SettingValue("ADValueRange", 0.2f, 0.1f, 10);
	public final SettingValue ADMinPhaseSize = new SettingValue("ADMinPhaseSize", 3, 1, 1000000);
	public final SettingValue ADMaxPhaseSize = new SettingValue("ADMaxPhaseSize", 15, 1, 1000000);
	public final SettingValue ADStressHP = new SettingValue("ADStressHP", 4, 1, 20);
	public final SettingValue ADDelayMlMin = new SettingValue("ADDelayMlMin", 1.5f, 0.1f, 100f);
	public final SettingValue ADDelayMlMax = new SettingValue("ADDelayMlMax", 2.5f, 0.1f, 100f);
	public final SettingValue ADLDelayChanceMin = new SettingValue("ADLDelayChanceMin", 40, 1, 100);
	public final SettingValue ADLDelayChanceMax = new SettingValue("ADLDelayChanceMax", 60, 1, 100);
	
	private int phase;
	private boolean powered;
	
	public AdvClickerDelays()
	{
		this.settings = Module.addSettings(this);
	}
	
	public List<Integer> genAdvancedDelayNoise (float CPSMin, float CPSMax)
	{
		phase++;
		powered = NClient.MC.player.getHealth() <= this.ADStressHP.getValue();
		return BUtils.genAdvancedDelayNoise(BUtils.randomInRange(Math.round(CPSMin), Math.round(CPSMax)), phase, powered, ADSpeed.getValue()
				, ADValueRange.getValue(), 
				ADMinPhaseSize.getValue(), ADMaxPhaseSize.getValue(), ADDelayMlMin.getValue(), ADDelayMlMax.getValue(), (short) ADLDelayChanceMin.getValue(), (short) ADLDelayChanceMax.getValue());
	}
	
	@Override
	public void onEnable()
	{
		
	}
}
