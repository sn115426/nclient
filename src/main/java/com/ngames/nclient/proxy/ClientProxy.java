package com.ngames.nclient.proxy;

import com.ngames.nclient.keybinds.KeyBinds;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        KeyBinds.register();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }
}