package com.ngames.nclient.module.modules.player;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerHealthChangeEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingValue;

import net.minecraft.init.Items;

@ModuleType(
		category = Category.PLAYER, 
		description = "Automatically eat soup", 
		name = "AutoSoup", 
		words = "AutoSoup Regen")
public class AutoSoup extends Module
{
	private final SettingValue health = new SettingValue("Health", 12, 1, 19);
	private final SettingValue stopHealth = new SettingValue("stopHealth", 18, 1, 20);
	private final SettingValue attemps = new SettingValue("Attemps", 5, 1, 1000);
	private final SettingValue useMinDelay = new SettingValue("UseMinDelay", 30, 0, 1000);
	private final SettingValue useMaxDelay = new SettingValue("UseMaxDelay", 50, 0, 1000);
	private final SettingBoolean fastUse = new SettingBoolean("FastUse", true);
	private final SettingBoolean onlyHotbar = new SettingBoolean("OnlyHotbar", false);
	private final SettingBoolean setBack = new SettingBoolean("SetBack", true);
	private final SettingValue setBackSlot = new SettingValue("SetBackSlot", 0, 0, 8);
	
	private boolean isRun;
	
	public AutoSoup()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(PlayerHealthChangeEvent.class, this);
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (!isRun)
			new Thread(() -> {
				isRun = true;
				if (NClient.MC.player.getHealth() <= this.health.getValue())
				{
					int prevSlot = (int) setBackSlot.getValue();
					int slot = Baritone.getSlotFor(Items.MUSHROOM_STEW, this.onlyHotbar.getValue());
					if (slot != -1)
					{
						int slot2 = Baritone.getSlotFor(Items.BOWL, true);
						if (slot2 != -1)
						{
							Baritone.setMainHand(slot2);
							Baritone.dropCurrentItem();
						}
						if (!this.onlyHotbar.getValue() && slot > 8)
							Baritone.putInAir(slot);
						slot = Baritone.getSlotFor(Items.MUSHROOM_STEW, true);
						if (slot != -1 && slot < 9)
						{
							Baritone.setMainHand(slot);
							eat();
							if (Baritone.isInMainHand(Items.BOWL) || Baritone.isInMainHand(Items.MUSHROOM_STEW))
								Baritone.dropCurrentItem();
							if (this.setBack.getValue())
								Baritone.setMainHand(prevSlot);
						}
					}
				}
				isRun = false;
			}).start();
	}
	
	private void eat()
	{
		Baritone.clickSync = true;
		int i = 0;
		while (NClient.MC.player.getHealth() < this.stopHealth.getValue() && i < attemps.getValue())
		{
			if (this.fastUse.getValue() && Baritone.isInMainHand(Items.MUSHROOM_STEW))
			{
				Baritone.rightClickMouse();
				BUtils.sleep(BUtils.randomInRange((int) useMinDelay.getValue(), (int) useMaxDelay.getValue()));
			}
			if (!this.fastUse.getValue() && Baritone.isInMainHand(Items.MUSHROOM_STEW))
			{
				Baritone.useItem();
				BUtils.sleep(BUtils.randomInRange((int) useMinDelay.getValue(), (int) useMaxDelay.getValue()));
			}
			if (!this.fastUse.getValue())
				Baritone.usedItem();
			i++;
		}
		Baritone.clickSync = false;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		this.onInvoke(new PlayerHealthChangeEvent());
	}
}
