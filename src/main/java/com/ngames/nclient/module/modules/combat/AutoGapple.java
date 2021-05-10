package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerFoodStatsChangeEvent;
import com.ngames.nclient.event.NClientEvent.PlayerHealthChangeEvent;
import com.ngames.nclient.event.NClientEvent.PotionEffectRemovedEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingValue;

import net.minecraft.init.Items;
import net.minecraft.potion.Potion;

@ModuleType(
		category = Category.PLAYER, 
		description = "Automatically eat gapples", 
		name = "AutoGapple", 
		words = "AutoGapple AutoEat")
public class AutoGapple extends Module
{
	private final SettingBoolean allowInPvP = new SettingBoolean("AllowInPvP", false);
	private final SettingValue health = new SettingValue("Health", 10, 0, 20);
	private final SettingBoolean regenEffect = new SettingBoolean("RegenEffect", true);
	private final SettingBoolean fireProtectEffect = new SettingBoolean("FireProtectEffect", false);
	private final SettingBoolean offHand = new SettingBoolean("OffHand", false);
	private final SettingBoolean onlyHotbar = new SettingBoolean("OnlyHotbar", false);
	private final SettingBoolean setBack = new SettingBoolean("SetBack", true);
	
	private boolean isRun = false;
	
	public AutoGapple()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		this.onUpdate();
		new Listener(PlayerFoodStatsChangeEvent.class, this);
		new Listener(PotionEffectRemovedEvent.class, this);
		new Listener(PlayerHealthChangeEvent.class, this);
		this.enabled = true;
	}
	
	private boolean calcUsing()
	{
		return (this.allowInPvP.getValue() || !NClient.inPvP) && (this.health.getValue() >= NClient.MC.player.getHealth() ||
				(!NClient.MC.player.isPotionActive(Potion.getPotionById(10)) && this.regenEffect.getValue()) ||
				(!NClient.MC.player.isPotionActive(Potion.getPotionById(12)) && this.fireProtectEffect.getValue()));
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (!isRun)
			new Thread(() -> {
				isRun = true;
				if (this.calcUsing())
				{
					int slotIn = Baritone.getSlotFor(Items.GOLDEN_APPLE, onlyHotbar.getValue());
					int currSlot = NClient.MC.player.inventory.currentItem;
					if (slotIn != -1)
					{
						if (slotIn > 8 && !this.onlyHotbar.getValue())
							Baritone.putInMainHand(slotIn);
						else if (slotIn < 9)
							Baritone.setMainHand(slotIn);
						else if (this.offHand.getValue())
							Baritone.putInOffHand(slotIn);
						else
							slotIn = -1;
					}
					if (slotIn != -1)
					{	
						Baritone.useItem();
						while (this.calcUsing() && this.enabled)
							BUtils.sleep(100);
						Baritone.usedItem();
					}
					if (this.setBack.getValue())
					{
						if (this.offHand.getValue())
							Baritone.putInOffHand(slotIn);
						else if (slotIn != -1)
						{
							if (slotIn < 9)
								Baritone.setMainHand(currSlot);
							else
								Baritone.putInMainHand(slotIn);
						}
					}
				}
				isRun = false;
			}).start();
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		this.onInvoke(new PlayerFoodStatsChangeEvent());
	}
}
