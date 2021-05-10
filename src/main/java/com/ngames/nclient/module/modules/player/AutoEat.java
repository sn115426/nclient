package com.ngames.nclient.module.modules.player;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.Baritone;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.PlayerFoodStatsChangeEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingValue;

@ModuleType(
		category = Category.PLAYER, 
		description = "Automatically eat food when hungry", 
		name = "AutoEat", 
		words = "AutoEat Eat")
public class AutoEat extends Module
{
	private final SettingValue foodLvl = new SettingValue("FoodLvl", 10, 0, 20);
	private final SettingChoose preferFood = new SettingChoose("PreferFood", (byte) 0, new String[] {
			"best",
			"wise"
	});
	private final SettingBoolean setBack = new SettingBoolean("SetBack", true);
	private final SettingBoolean eatInPvP = new SettingBoolean("EatInPvP", false);
	
	private boolean isRun = false;
	
	public AutoEat()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(PlayerFoodStatsChangeEvent.class, this);
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (!isRun)
		new Thread(() -> {
			isRun = true;
			if (NClient.MC.player.getFoodStats().getFoodLevel() <= foodLvl.getValue())
			{
				while (Baritone.getFoodStats().needFood() && this.enabled)
				{
					byte isEaten = (byte) Baritone.getFoodStats().getFoodLevel();
					if ((!eatInPvP.getValue() && !NClient.inPvP) || eatInPvP.getValue())
					{
						int slotId = -1;
						int hand = NClient.MC.player.inventory.currentItem;
						if (preferFood.getValue() == 0)
							slotId = Baritone.getBestFood(true);
						if (preferFood.getValue() == 1)
							slotId = Baritone.getFoodWithHeal((byte) Baritone.getFoodStats().getFoodLevel(), true);
						if (NClient.MC.player.canEat(false) && slotId != -1)
						{
							Baritone.setMainHand(slotId);
							Baritone.useItem();
							while (isEaten >= Baritone.getFoodStats().getFoodLevel() && this.enabled)
							{
								if (NClient.MC.player.inventory.currentItem != slotId)
									Baritone.setMainHand(slotId);
								BUtils.sleep(100);
							}
							Baritone.usedItem();
							if (setBack.getValue())
							{
								Baritone.setMainHand(hand);
							}
						}
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
