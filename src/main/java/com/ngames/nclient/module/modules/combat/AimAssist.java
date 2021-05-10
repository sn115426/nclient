package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.baritone.SafeThread;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingValue;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

@ModuleType(
		category = Category.COMBAT,
		description = "Automatically aiming at the entity",
		name = "AimAssist", 
		words = "Aim AimAssist AimAssistent Aimbot KillAura")
public class AimAssist extends Module
{
	private SettingValue updateDelay = new SettingValue("UpdateDelay", 10, 0, 1000);
	private SettingBoolean auraTarget = new SettingBoolean("AuraTarget", true);
	private SettingValue range = new SettingValue("Range", 4, 0, 20);
	private final SettingBoolean rtOnlyNotMO = new SettingBoolean("RTOnlyNotMO", true);
	
	private Entity target;
	
	public AimAssist()
	{
		this.settings = addSettings(this);
	}
	@Override
	public void onEnable()
	{
		super.onEnable();
		new SafeThread(() -> {
			if (this.auraTarget.getValue())
				this.target = Modules.killAura18.target;
			else
				this.target = Baritone.getPriorityTarget(range.getValue(), true, false, false, null, (byte) 1);
			if (target != null)
			{
				boolean rtr = BUtils.rayTraceBlocks(NClient.MC.player.getPositionEyes(NClient.MC.getRenderPartialTicks()), 
					new Vec3d (target.posX + target.width * 0.5, target.posY + target.height * 0.5, target.posZ + target.width * 0.5));;
				if (rtr && Baritone.isAlive(target) && NClient.MC.player.getDistanceToEntity(target) <= this.range.getValue() && 
						(Modules.killAura18.isEnabled() || !this.auraTarget.getValue()) && ((this.rtOnlyNotMO.getValue() && NClient.MC.objectMouseOver.entityHit != null) ||
								!this.rtOnlyNotMO.getValue()))
					Baritone.setRotationToEntity(target);
			}
			BUtils.sleep((int) updateDelay.getValue());	
		}, this).start();
	}
}
