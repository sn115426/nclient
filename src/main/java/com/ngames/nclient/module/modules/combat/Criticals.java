package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingValue;

import net.minecraft.entity.Entity;

@ModuleType(
		category = Category.COMBAT, 
		description = "Make your punches critical",
		name = "Criticals", 
		words = "Criticals damage")
public class Criticals extends Module
{
	public SettingChoose type = new SettingChoose("Type", (byte) 1, new String[] {
		"Packet",
		"MiniJump",
		"OffHand"
	});
	private SettingValue miniJumpHeigth = new SettingValue("MiniJumpHeight", 0.15f, 0f, 2f);
	
	public Criticals()
	{
		this.settings = Module.addSettings(this);
	}
	
	public static boolean miniJump (Entity target)
	{
		if (Modules.criticals.isEnabled() && Modules.criticals.type.getValue() == 1 && NClient.MC.player.onGround)
		{
			NClient.MC.player.motionY += Modules.criticals.miniJumpHeigth.getValue();
			new Thread(() -> {
				while (NClient.MC.player.fallDistance <= 0)
				{
					BUtils.sleep(1);
				}
				NClient.MC.player.attackTargetEntityWithCurrentItem(target);
			}).start();
			return true;
		}
		return false;
	}
}
