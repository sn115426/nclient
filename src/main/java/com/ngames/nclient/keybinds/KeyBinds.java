package com.ngames.nclient.keybinds;

import java.util.ArrayList;
import java.util.List;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.ModuleUtils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBinds
{
	private static final String catergory = "N-Client";
    public static List<KeyBinding> keyBinding = new ArrayList<>();

    public static void register()
    {
    	for (Module h : NClient.moduleList)
    	{
    		keyBinding.add(new KeyBinding(h.getClass().getAnnotation(ModuleType.class).name(), h.getClass().getAnnotation(ModuleType.class).keyBind(), catergory));
    	}
    	for (KeyBinding kb : keyBinding)
    	{
    		setRegister(kb);
    	}
    }

    private static void setRegister(KeyBinding binding)
    {
        ClientRegistry.registerKeyBinding(binding);
    }
    
    public static void getBind(int keyCode)
    {
    	for (KeyBinding kb : keyBinding)
    	{
    		if (kb.getKeyCode() == keyCode)
    		{
    			ModuleUtils.getModule(kb.getKeyDescription()).onToggle();
    		}
    	}
    }
    
    public static int getModuleKeyBind (String name)
    {
    	int ret = 0;
    	for (KeyBinding kb : keyBinding)
    	{
    		if (kb.getKeyDescription().equals(name))
    		{
    			ret = kb.getKeyCode();
    		}
    	}
    	return ret;
    }
}
