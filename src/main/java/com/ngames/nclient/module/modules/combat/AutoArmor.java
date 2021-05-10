package com.ngames.nclient.module.modules.combat;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.event.NClientEvent;
import com.ngames.nclient.event.NClientEvent.LivingUpdatedEvent;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingValue;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@ModuleType(category = Category.PLAYER,
	description = "Automatically equip best armor in your inventory", 
	name = "AutoArmor", 
	words = "AutoArmor Armor")
public class AutoArmor extends Module
{
	private SettingValue equipDelay = new SettingValue("EquipDelay", 100, 0, 3000);
	private SettingBoolean equipWhenInvOpen = new SettingBoolean("EquipWhenInvOpen", false);
	
	private int[] bestArmorSlots = new int[4];
	private boolean isRun;
	
	public AutoArmor()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		new Listener(LivingUpdatedEvent.class, this);
	}
	
	@Override
	public void onInvoke (NClientEvent event)
	{
		if (isRun)
			return;
		
		new Thread(() -> {
			isRun = true;
			if (NClient.MC.currentScreen instanceof GuiInventory && !equipWhenInvOpen.getValue())
			{
				isRun = false;
				return;
			}
			getBestArmor();
			for (int i = 0; i < 4; i++)
			{
				int armorValue = getArmorValue(NClient.MC.player.inventory.armorInventory.get(i).getItem());
				if (armorValue < bestArmorSlots[i])
				{
					if (armorValue == -1)
						NClient.MC.playerController.windowClick(0, bestArmorSlots[i], 0, ClickType.QUICK_MOVE, NClient.MC.player);
					else {
						NClient.MC.playerController.windowClick(0, bestArmorSlots[i], 0, ClickType.PICKUP, NClient.MC.player);
						NClient.MC.playerController.windowClick(0, 5 + i, 0, ClickType.PICKUP, NClient.MC.player);
					}
					BUtils.sleep((int)this.equipDelay.getValue());
				}
			}
			isRun = false;
		}).start();
	}
	
	private void getBestArmor()
	{
		int[] bestArmorValues = new int[4];
		
		for(int slot = 0; slot < 36; slot++)
        {
            ItemStack stack = NClient.MC.player.inventory.getStackInSlot(slot);

            if (stack.getCount() > 1)
                continue;

            if(stack == null || !(stack.getItem() instanceof ItemArmor))
                continue;

            ItemArmor armor = (ItemArmor)stack.getItem();
            int armorType = armor.armorType.ordinal() - 2;

            if (armorType == 2 && NClient.MC.player.inventory.armorItemInSlot(armorType).getItem().equals(Items.ELYTRA)) continue;

            int armorValue = armor.damageReduceAmount;

            if(armorValue > bestArmorValues[armorType])
            {
                bestArmorSlots[armorType] = slot;
                bestArmorValues[armorType] = armorValue;
            }
        }
	}
	
	private int getArmorValue (Item itemArmor)
	{
		return itemArmor == null ? -1 : ((ItemArmor)itemArmor).damageReduceAmount;
	}
}
