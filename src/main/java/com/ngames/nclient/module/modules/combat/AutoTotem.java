package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.LivingUpdatedEvent;
import com.ngames.nclient.event.NClientEvent.PlayerHealthChangeEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingValue;

import net.minecraft.init.Items;

@ModuleType(
		category = Category.PLAYER, 
		description = "Automatically put totems in offhand", 
		name = "AutoTotem", 
		words = "AutoTotem")
public class AutoTotem extends Module
{
	private final SettingChoose replace = new SettingChoose("Replace", (byte)2, "all", "empty", "exceptCrystals");
	private final SettingValue health = new SettingValue("Health", 8, 1, 20);
	private final SettingBoolean setBack = new SettingBoolean("SetBack", true);
	private final SettingValue setBackHealth = new SettingValue("SetBackHealth", 15, 2, 20);
	private boolean isRun;
	private int setBackSlot = -1;
	
	public AutoTotem()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(LivingUpdatedEvent.class, this);
		new Listener(PlayerHealthChangeEvent.class, this);
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (isRun && event instanceof PlayerHealthChangeEvent)
			return;
		if (event instanceof LivingUpdatedEvent)
		{
			if (!Baritone.isInOffHand(Items.TOTEM_OF_UNDYING))
			{
				int slotFor = Baritone.getSlotFor(Items.TOTEM_OF_UNDYING, false);
				if (slotFor != -1)
				{
					if (NClient.MC.player.getHealth() <= health.getValue())
					{
						if ((this.replace.getValue() == 1 && Baritone.isEmptyInOffHand()) || this.replace.getValue() == 0 || 
								(this.replace.getValue() == 2 && !Baritone.isInOffHand(Items.END_CRYSTAL)))
							Baritone.putInOffHand(slotFor);
					}
					setBackSlot = slotFor;
				}
			}
		} else {
			new Thread (() -> {
				isRun = true;
				while (this.enabled && NClient.MC.world != null && this.setBack.getValue() && setBackSlot != -1)
				{
					if (NClient.MC.player.getHealth() >= this.setBackHealth.getValue())
					{
						Baritone.putInOffHand(setBackSlot);
						break;
					}
					BUtils.sleep(50);
				}
				isRun = false;
			}).start();
		}
	}
}
