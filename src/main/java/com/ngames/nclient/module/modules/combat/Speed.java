package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.OnPlayerUpdateEvent;
import com.ngames.nclient.event.NClientEvent.PlayerJumpEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingChoose;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.Timer;
import net.minecraft.util.math.MathHelper;

import static com.ngames.nclient.NClient.MC;

import java.util.UUID;

@ModuleType(
		category = Category.MOVEMENT, 
		description = "several ways to moving quickly", 
		name = "Speed", 
		words = "Speed SpeedHack AutoJump")
public class Speed extends Module
{
	public final SettingChoose type = new SettingChoose("Type", (byte) 0, "Legit", "test0.1", "advanced");
	
	private final AttributeModifier entitySpeed = (new AttributeModifier(UUID.randomUUID(), "Speed NClient", 0.3300000001192092896D, 2)).setSaved(false);
	private int leftTicks = 0;
	private int rightTicks = 0;
	private int fowTicks = 0;
	private int backTicks = 0;
	
	public Speed()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(OnPlayerUpdateEvent.class, this);
		new Listener(PlayerJumpEvent.class, this);
	}
	
	@Override
	public void onDisable()
	{
		Baritone.overrideRotation = false;
		MC.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(entitySpeed);
		super.onDisable();
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (event instanceof PlayerJumpEvent && this.type.getValue() == 2)
		{
			event.setCalceled();
			return;
		}
		switch (this.type.getValue())
		{
			case 0:
					this.handleType0();
			case 1:
					this.handleType1();
			case 2:
					this.handleType2();
		}
	}
	
	private void handleType0()
	{
		if (Baritone.isMoving())
		{
			KeyBinding.setKeyBindState(MC.gameSettings.keyBindJump.getKeyCode(), true);
			if (MC.player.moveForward != 0) 
				MC.player.setSprinting(true);
		} else
			KeyBinding.setKeyBindState(MC.gameSettings.keyBindJump.getKeyCode(), false);
	}
	
	private void handleType1()
	{
		boolean fow = MC.gameSettings.keyBindForward.isKeyDown();
		boolean back = MC.gameSettings.keyBindBack.isKeyDown();
		boolean left = MC.gameSettings.keyBindLeft.isKeyDown();
		boolean right = MC.gameSettings.keyBindRight.isKeyDown();
		if (fow || left || back || right)
			if (MC.player.onGround)
			{
				float yaw = MC.player.rotationYaw;
				if (back)
					yaw += 180;
				if (right)
					yaw += 90;
				if (left)
					yaw -= 90;
				if (fow && (right || left))
					yaw = right ? yaw - 45 : yaw + 45;
				yaw = BUtils.toMinecraftDegrees(yaw);
				float f = yaw * 0.017453292F;
				MC.player.movementInput.jump = true;
				MC.player.motionY = 0.42f;
				MC.player.motionX -= (double)(MathHelper.sin(f) * 0.2F);
				MC.player.motionZ += (double)(MathHelper.cos(f) * 0.2F);
			}
	}
	
	private void handleType2()
	{
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
		Baritone.yaw = yaw;
		Baritone.overrideRotation = this.enabled;
		Baritone.updateRotation();
		float input = Math.max(Math.abs(NClient.MC.player.moveStrafing), Math.abs(NClient.MC.player.moveForward));
		if (!NClient.MC.player.isSprinting() && flag2)
			NClient.MC.player.setSprinting(true);
		NClient.MC.player.moveForward = input;
		NClient.MC.player.moveStrafing = 0;
		if (fow)
			NClient.MC.player.movementInput.moveForward = 1;
		if (back)
			NClient.MC.player.movementInput.moveForward = -1;
		if (left)
			NClient.MC.player.movementInput.moveStrafe = -1;
		if (right)
			NClient.MC.player.movementInput.moveForward = 1;
		NClient.MC.player.movementInput.jump = true;
		float f = yaw * 0.017453292F;
		if (flag2 && MC.player.onGround)
		{
			MC.player.motionX -= (double)(MathHelper.sin(f) * 0.23F);	
			MC.player.motionY = 0.42;
			Baritone.setTimer(new Timer(20f));
			MC.player.motionZ += (double)(MathHelper.cos(f) * 0.23F);
		} else
		{
			Baritone.setTimer(new Timer(20.3f));
		}
		if (isEnabled())
		{
			IAttributeInstance iatt = MC.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
			if (!iatt.hasModifier(entitySpeed))
				iatt.applyModifier(entitySpeed);
			iatt.removeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
		}
	}
}
