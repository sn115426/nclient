package com.ngames.nclient.module.modules.combat;

import static com.ngames.nclient.NClient.MC;

import java.util.UUID;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.LivingUpdatedEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingChoose;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "Change your strafe speed", 
		name = "Strafe", 
		words = "Strafe Speed Sprint")
public class Strafe extends Module
{
	public SettingChoose mode = new SettingChoose("Mode", (byte) 1, new String[] {
			"simple",
			"slient"
	});
	
	private int leftTicks = 0;
	private int rightTicks = 0;
	private int fowTicks = 0;
	private int backTicks = 0;
	public AttributeModifier strafe = (new AttributeModifier(UUID.randomUUID(), "Strafe  NClient", 0.30000001192092896D, 2)).setSaved(false);
	private short rotationPriority = 2;
	
	public Strafe()
	{
		this.settings = addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(LivingUpdatedEvent.class, this);
	}
	
	@Override
	public void onDisable()
	{
		Baritone.overrideRotation = false;
		MC.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(strafe);
		Baritone.setRotationPriority((short) 0);
		super.onDisable();
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
			boolean flag = this.mode.getValue() == 1;
			boolean flag2 = false;
			float yaw = MC.player.rotationYaw;
			boolean fow = MC.gameSettings.keyBindForward.isKeyDown();
			boolean back = MC.gameSettings.keyBindBack.isKeyDown();
			boolean left = MC.gameSettings.keyBindLeft.isKeyDown();
			boolean right = MC.gameSettings.keyBindRight.isKeyDown();
			rightTicks = right ? ++rightTicks : 0;
			leftTicks = left ? ++leftTicks : 0;
			fowTicks = fow ? ++fowTicks : 0;
			backTicks = back ? ++backTicks : 0;
			int latest = Math.min(Math.min(fowTicks == 0 ? Integer.MAX_VALUE : fowTicks, backTicks == 0 ? Integer.MAX_VALUE : backTicks), 
					Math.min(leftTicks == 0 ? Integer.MAX_VALUE : leftTicks, rightTicks == 0 ? Integer.MAX_VALUE : rightTicks));
			if (fow || left || back || right)
			{
					if (back && backTicks == latest)
						yaw += 180;
					if (right && rightTicks == latest)
						yaw += 90;
					if (left && leftTicks == latest)
						yaw -= 90;
					yaw = BUtils.toMinecraftDegrees(yaw);
					flag2 = true;
			}
			if (flag2 && !fow && MC.player.onGround && Baritone.getJumpTicks() == 0 && MC.gameSettings.keyBindJump.isKeyDown())
			{
				float f = yaw * 0.017453292F;
				MC.player.motionX -= (double)(MathHelper.sin(f) * 0.2F);
				MC.player.motionZ += (double)(MathHelper.cos(f) * 0.2F);
				this.rotationPriority = 11;
			} else
			{
				this.rotationPriority = 2;
			}
			boolean rotate = Baritone.calcRotationPriority(this.rotationPriority);
			if (flag && yaw != Baritone.yaw && rotate)
			{
				Baritone.yaw = yaw;
				Baritone.overrideRotation = this.enabled;
				Baritone.updateRotation();
			} else 
				Baritone.setRotationPriority((short) 0);
			if (rotate)
				Baritone.pitch = MC.player.rotationPitch;
			float input = Math.max(Math.abs(NClient.MC.player.moveStrafing), Math.abs(NClient.MC.player.moveForward));
			if (!NClient.MC.player.isSprinting())
				NClient.MC.player.setSprinting(true);
			if (flag)
			{
				NClient.MC.player.moveForward = input;
				NClient.MC.player.moveStrafing = 0;
				
			}
			if (isEnabled() && (rotate || MC.player.rotationYaw == yaw))
			{
				IAttributeInstance iatt = MC.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
				if (!iatt.hasModifier(strafe))
					iatt.applyModifier(strafe);
				iatt.removeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
			}
	}
}
