package com.ngames.nclient.module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.modules.combat.AdvClickerDelays;
import com.ngames.nclient.module.modules.combat.AimAssist;
import com.ngames.nclient.module.modules.combat.AutoArmor;
import com.ngames.nclient.module.modules.combat.AutoClicker;
import com.ngames.nclient.module.modules.combat.AutoEz;
import com.ngames.nclient.module.modules.combat.AutoGapple;
import com.ngames.nclient.module.modules.combat.AutoRelog;
import com.ngames.nclient.module.modules.combat.AutoTotem;
import com.ngames.nclient.module.modules.combat.CPSCount;
import com.ngames.nclient.module.modules.combat.Criticals;
import com.ngames.nclient.module.modules.combat.KillAura18;
import com.ngames.nclient.module.modules.combat.Reach;
import com.ngames.nclient.module.modules.chat.ChatPostfix;
import com.ngames.nclient.module.modules.chat.ChatPrefix;
import com.ngames.nclient.module.modules.chat.Spammer;
import com.ngames.nclient.module.modules.combat.Speed;
import com.ngames.nclient.module.modules.combat.Strafe;
import com.ngames.nclient.module.modules.combat.Velocity;
import com.ngames.nclient.module.modules.exploit.AutoDupe;
import com.ngames.nclient.module.modules.exploit.InvActionLogger;
import com.ngames.nclient.module.modules.exploit.ItemSpammer;
import com.ngames.nclient.module.modules.exploit.LongJump;
import com.ngames.nclient.module.modules.exploit.NoRotate;
import com.ngames.nclient.module.modules.exploit.PacketCanceller;
import com.ngames.nclient.module.modules.exploit.Timer;
import com.ngames.nclient.module.modules.movement.AutoJump;
import com.ngames.nclient.module.modules.movement.AutoSpin;
import com.ngames.nclient.module.modules.movement.AutoSprint;
import com.ngames.nclient.module.modules.movement.EntityControl;
import com.ngames.nclient.module.modules.movement.HighJump;
import com.ngames.nclient.module.modules.movement.LookingForward;
import com.ngames.nclient.module.modules.movement.Sneak;
import com.ngames.nclient.module.modules.player.AutoEat;
import com.ngames.nclient.module.modules.player.AutoSoup;
import com.ngames.nclient.module.modules.render.ClickGUI;
import com.ngames.nclient.module.modules.render.HUD;
import com.ngames.nclient.module.modules.render.NoRender;

public class Modules
{
	public static final AdvClickerDelays advClickerDelays = new AdvClickerDelays();
	public static final AimAssist aimAssist = new AimAssist();
	public static final AutoArmor autoArmor = new AutoArmor();
	public static final AutoClicker autoClicker = new AutoClicker();
	public static final AutoDupe autoDupe = new AutoDupe();
	public static final AutoEat autoEat = new AutoEat();
	public static final AutoEz autoEz = new AutoEz();
	public static final AutoGapple autoGapple = new AutoGapple();
	public static final AutoJump autoJump = new AutoJump();
	public static final AutoRelog autoRelog = new AutoRelog();
	public static final AutoSoup autoSoup = new AutoSoup();
	public static final AutoSpin autoSpin = new AutoSpin();
	public static final AutoSprint autoSprint = new AutoSprint();
	public static final AutoTotem autoTotem = new AutoTotem();
	public static final ChatPostfix chatPostfix = new ChatPostfix();
	public static final ChatPrefix chatPrefix = new ChatPrefix();
	public static final ClickGUI clickGUI = new ClickGUI();
	public static final CPSCount cpsCount = new CPSCount();
	public static final Criticals criticals = new Criticals();
	public static final EntityControl entityControl = new EntityControl();
	public static final HighJump highJump = new HighJump();
	public static final HUD hud = new HUD();
	public static final InvActionLogger invActionLogger = new InvActionLogger();
	public static final ItemSpammer itemSpammer = new ItemSpammer();
	public static final KillAura18 killAura18 = new KillAura18();
	public static final LongJump longJump = new LongJump();
	public static final LookingForward lookingForward = new LookingForward();
	public static final NoRender noRender = new NoRender();
	public static final PacketCanceller packetCanceller = new PacketCanceller();
	public static final NoRotate noRotate = new NoRotate();
	public static final Reach reach = new Reach();
	public static final Sneak sneak = new Sneak();
	public static final Spammer spammer = new Spammer();
	public static final Speed speed = new Speed();
	public static final Strafe strafe = new Strafe();
	public static final Timer timer = new Timer();
	public static final Velocity velocity = new Velocity();
	
	public static HashMap<String, Integer> init()
	{
		HashMap<String, Integer> modules = new HashMap<>();
		int i = 0;
		for (Module h : NClient.moduleList)
		{
		modules.put(h.getClass().getAnnotation(ModuleType.class).name(), i);
		i++;
		}
		return modules;
	}
	
	public static List<Module> initL()
	{
		List<Module> ret = new ArrayList<>();
		for (Field moduleField : Modules.class.getDeclaredFields())
		{
			if (moduleField.getType().getSuperclass() == Module.class)
			{
					try {
						ret.add((Module) moduleField.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
			}
		}
		return ret;
	}
}
