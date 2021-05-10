package com.ngames.nclient.module.modules.combat;

import static com.ngames.nclient.NClient.MC;

import java.util.ArrayList;
import java.util.List;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.BUtils.Direction;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.baritone.SafeThread;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.LivingUpdatedEvent;
import com.ngames.nclient.event.NClientEvent.OnPlayerUpdateEvent;
import com.ngames.nclient.event.NClientEvent.RunTickKeyboardEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingValue;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleType(
		category = Category.COMBAT, 
		description = "Automatically attack players",
		name = "KillAura18", 
		words = "KillAura18 KillAura TriggerBot AutoClicker")
public class KillAura18 extends Module
{
	private final SettingChoose DelayType = new SettingChoose("DelayType", (byte) 2, new String[] {
			"simple",
			"random",
			"advanced"
		});
	private final SettingChoose ClickType = new SettingChoose("ClickType", (byte) 3, new String[] {
			"ClickMouse",
			"AAC",
			"target",
			"AACTarget"
	});
	private final SettingBoolean AACTCheckEntityType = new SettingBoolean("AACTCheckEntityType", true);
	private final SettingValue CPSMin = new SettingValue("CPSMin", 17, 1, 100);
	private final SettingValue CPSMax = new SettingValue("CPSMax", 21, 1, 100);
	private final SettingValue Range = new SettingValue("Range", 6, 1, 10);
	private final SettingBoolean TickSync = new SettingBoolean("TickSync", true);
	private final SettingBoolean RotationSync = new SettingBoolean("RotationSync", true);
	private final SettingValue CheckDelay = new SettingValue("CheckDelay", 10, 0, 1000);
	private final SettingBoolean multipleTarget = new SettingBoolean("MultipleTarget", true);
	private final SettingChoose priority = new SettingChoose("Priority", (byte) 0, new String[] {
			"health",
			"distance",
			"multi"
		});
	private final SettingValue STargetAttacks = new SettingValue("STargetAttacks", 2, 1, 100);
	private final SettingBoolean rayTracing = new SettingBoolean("RayTracing", true);
	private final SettingBoolean rtOnlyNotMO = new SettingBoolean("RTOnlyNotMO", true);
	private final SettingBoolean clientRotations = new SettingBoolean("ClientRotations", true);
	private final SettingChoose autoBlock = new SettingChoose("AutoBlock", (byte) 0, new String[] {
			"off",
			"legit",
			"packet"
		});
	private final SettingBoolean attackMobs = new SettingBoolean("AttackMobs", false);
	private final SettingBoolean attackPlayers = new SettingBoolean("AttackPlayers", true);
	
	public Entity target;
	private Entity botTarget;
	public boolean targeted = false;
	private boolean waitClick;
	private List<Entity> multiTargets = new ArrayList<>();
	int k;
	int m = 1;
	boolean rtr;
	short rotationPriority = 10;
	
	public KillAura18()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(RunTickKeyboardEvent.class, this);
		new Listener(OnPlayerUpdateEvent.class, this);
		new Listener(LivingUpdatedEvent.class, this);
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
							if (!this.RotationSync.getValue())
								getTarget();
							if (((this.RotationSync.getValue() && rtr) || (mouseOver() && this.rtOnlyNotMO.getValue()) || rayTrace()) && 
									canBeAttacked(target) && !Baritone.clickSync)
							{
								if (this.TickSync.getValue())
								{
									if (this.autoBlock.getValue() == 1)
										Baritone.usedItem();
									if (!NClient.MC.player.isHandActive() && Baritone.getLeftClickCounter() <= 0)
										this.waitClick = true;
								} else {
									attack();
								}
								targeted = true;
							} else
								targeted = false;
						BUtils.sleep(delay, BUtils.randomInRange(0, 999999));
					} else
						break;
				}
				if (!targeted)
					BUtils.sleep((int) this.CheckDelay.getValue());
		}, this).start();
	}
	
	private boolean rayTrace()
	{
		boolean rayTraceResult = false;
		rayTraceResult = BUtils.rayTraceBlocks(NClient.MC.player.getPositionEyes(NClient.MC.getRenderPartialTicks()), 
				new Vec3d (target.posX + target.width * 0.5, target.posY + target.height * 0.5, target.posZ + target.width * 0.5));
		if (!this.rayTracing.getValue())
			rayTraceResult = true;
		if (rayTraceResult) {
			Baritone.setRotationToEntity(target, !this.clientRotations.getValue());
			if (!this.clientRotations.getValue())
			{
				Baritone.updateRotation();
				Baritone.overrideRotation = true;
			}
			return true;
		}
		else
			for (int i = 0; i < 10; i ++)
			{
				rayTraceResult = BUtils.rayTraceBlocks(NClient.MC.player.getPositionEyes(NClient.MC.getRenderPartialTicks()), 
						new Vec3d (target.posX + target.width * 0.5, target.posY + target.height * 0.1 * i, target.posZ + target.width * 0.5));
				if (rayTraceResult)
				{
					Baritone.setRotationToEntity(target, i / 10, !this.clientRotations.getValue());
					if (!this.clientRotations.getValue())
					{
						Baritone.updateRotation();
						Baritone.overrideRotation = true;
					}
					break;
				}
			}
		if (!rayTraceResult)
		{
			Baritone.setRotationPriority((short) 0);
			Baritone.refreshRotation();
		}
		return rayTraceResult;
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (this.enabled)
		{
			if (event instanceof RunTickKeyboardEvent && this.waitClick && canBeAttacked(target))
			{
				attack();
				this.waitClick = false;
			}
			if (event instanceof OnPlayerUpdateEvent && this.RotationSync.getValue())
			{
				Baritone.calcRotationPriority(this.rotationPriority);
				getTarget();
				if (target != null && canBeAttacked(target))
					rtr = (mouseOver() && this.rtOnlyNotMO.getValue()) || rayTrace();
			}
			if (event instanceof LivingUpdatedEvent && !this.clientRotations.getValue())
				this.fixMovement();
		}
	}
	
	private void attack()
	{
		if (this.autoBlock.getValue() == 2)
			NClient.MC.getConnection().sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
		if (this.ClickType.getValue() == 0)
			Baritone.leftClickMouse();
		else if (this.ClickType.getValue() == 1)
			Baritone.attackEntity();
		else if (this.ClickType.getValue() == 2)
			Baritone.attackEntity(target);
		else if (this.ClickType.getValue() == 3)
			Baritone.attackEntity(botTarget != null ? botTarget : target);
		if (this.autoBlock.getValue() == 1)
			Baritone.useItem();
		Baritone.setRotationPriority((short) 0);
	}
	
	private void getTarget()
	{
		if (m == this.STargetAttacks.getValue() || this.priority.getValue() != 2 || !canBeAttacked(target))
		{
			if (this.priority.getValue()  < 2)
			{
				target = Baritone.getPriorityTarget(this.Range.getValue(), this.attackPlayers.getValue(), 
						this.attackMobs.getValue(), !this.multipleTarget.getValue(), this.target, this.priority.getValue());
			} else {
				if (k >= this.multiTargets.size() || this.multiTargets.isEmpty())
				{
					this.multiTargets = Baritone.getMultiTargets(this.Range.getValue(), this.attackPlayers.getValue(), this.attackMobs.getValue());
					k = 0;
				}
				for (int i = 0; i < this.multiTargets.size(); i++)
				{
					if (this.enabled && !this.multiTargets.isEmpty())
					{
						if (canBeAttacked(this.multiTargets.get(i)))
						{
							target = this.multiTargets.get(i);
							break;
						}
					} else
						break;
				}
				if (!this.multiTargets.isEmpty() && this.multiTargets.get(k) != null && canBeAttacked(this.multiTargets.get(k)))
					target = this.multiTargets.get(k);
				k++;
			}
			if (NClient.MC.world != null && target != null)
			{
				if (this.ClickType.getValue() == 3)
					botTarget = Baritone.getBotTarget(target, this.AACTCheckEntityType.getValue());
			}
		}
		if (this.priority.getValue() == 2)
		{
			m++;
			if (m > this.STargetAttacks.getValue())
				m = 1;
		}
	}
	
	private boolean canBeAttacked (Entity e)
	{
		return e != null && Baritone.isAlive(e) && NClient.MC.player.getDistanceToEntity(e) <= this.Range.getValue() && 
				((e instanceof EntityOtherPlayerMP && this.attackPlayers.getValue()) || (e instanceof EntityLiving && this.attackMobs.getValue()));
	}
	
	private boolean mouseOver()
	{
		return (NClient.MC.objectMouseOver.entityHit == this.target || (this.ClickType.getValue() == 3 && NClient.MC.objectMouseOver.entityHit == this.botTarget)
				|| (this.ClickType.getValue() == 1 && NClient.MC.objectMouseOver.entityHit != null));
	}
	
	private void fixMovement()
	{
		boolean fow = MC.gameSettings.keyBindForward.isKeyDown();
		boolean jump = MC.gameSettings.keyBindJump.isKeyDown();
		if (fow && MC.player.isSprinting() && !(MC.player.rotationYaw > Baritone.yaw - 30 && MC.player.rotationYaw < Baritone.yaw + 30))
			MC.player.setSprinting(false);
		else
			MC.player.setSprinting(true);
		if (jump && MC.player.isSprinting() && Baritone.getJumpTicks() == 0)
		{
			float f = MC.player.rotationYaw * 0.017453292F;
			MC.player.motionX += (double)(MathHelper.sin(f) * 0.2F);
			MC.player.motionZ -= (double)(MathHelper.cos(f) * 0.2F);
		}
		Direction cdir = BUtils.getDirection(false, Baritone.yaw);
		Direction sdir = BUtils.getDirection(false);
		float cyaw = MC.player.rotationYaw;
		float syaw = Baritone.yaw;
		if (cdir != sdir)
		{
			boolean moveFow = (syaw > cyaw - 30 && syaw < cyaw + 30);
			boolean moveBack = syaw < cyaw - 120 || syaw > cyaw + 120;
			boolean moveLeft = syaw < cyaw - 30;
			boolean moveRight = syaw > cyaw + 30;
			if (moveFow)
			{
				MC.player.moveForward = 1;
				MC.player.movementInput.moveForward = 1;
			}
			else
			{
				MC.player.moveForward = 0;
				MC.player.movementInput.moveForward = 0;
			}
			if (moveBack)
			{
				MC.player.moveForward = -1;
				MC.player.movementInput.moveForward = -1;
			}
			if (moveLeft)
			{
				MC.player.moveStrafing = -1;
				MC.player.movementInput.moveStrafe = -1;
			}
			if (moveRight)
			{
				MC.player.moveStrafing = 1;
				MC.player.movementInput.moveStrafe = 1;
			}
			if (!moveRight && !moveLeft)
			{
				MC.player.moveStrafing = 0;
				MC.player.movementInput.moveStrafe = 0;
			}
		}
	}
	
	@Override
	public void onUpdate()
	{
		Baritone.overrideRotation = !this.clientRotations.getValue();
		super.onUpdate();
	}
	
	@Override
	public void onDisable()
	{
		if (!this.clientRotations.getValue())
			Baritone.overrideRotation = false;
		super.onDisable();
	}
}
