package com.ngames.nclient;

import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.command.CommandMan;
import com.ngames.nclient.event.Listener;
import com.ngames.nclient.gui.Gui;
import com.ngames.nclient.gui.Theme;
import com.ngames.nclient.gui.font.Fonts;
import com.ngames.nclient.gui.font.NFontRenderer;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.Modules;
import com.ngames.nclient.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(modid = NClient.MODID, name = NClient.NAME, version = NClient.VERSION)
public class NClient {

    public static final String MODID = "nclient";
    public static final String NAME = "N-Client";
    public static final String VERSION = "B1";

    public static final Minecraft MC = Minecraft.getMinecraft();
    public static final ResourceLocation background = new ResourceLocation(MODID + ":background.png");
    public static NFontRenderer guiFont;
    public static NFontRenderer hudFont;
    public static NFontRenderer chatFont;

    public static Logger logger;
    public static Gui gui;
    public static Theme theme = new Theme();

    public static File path = MC.mcDataDir;
    public static File NClientPath = new File(path, NAME);
    public static File settings = new File(NClientPath, "Settings.json");

    @SidedProxy(clientSide = "com.ngames.nclient.proxy.ClientProxy", serverSide = "com.ngames.nclient.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static String commandPrefix = ".";
    public static HashMap<String, Integer> hacks = new HashMap<>();
    public static List<Module> moduleList = new ArrayList<>();
    public static boolean inPvP = false;
    public static boolean isLeftPressed = false;
    public static List<Listener> listeners = new ArrayList<>();

    public static List<Long> c = new ArrayList<>();
    public static long t = 0;
    public static byte tps;
    public static byte ticks;

    public static CommandMan commandManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        commandManager = new CommandMan();
        moduleList = Modules.initL();
        hacks = Modules.init();
        proxy.preInit(event);
        if (!NClientPath.exists())
            NClientPath.mkdir();
        try {
            if (!settings.exists())
                settings.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        c.add(0L);
        new Thread(() -> {
            while (true) {
                t++;
                BUtils.sleep(1);
            }
        }).start();
        new Thread(() ->
        {
            while (true) {
                tps = ticks;
                ticks = 0;
                BUtils.sleep(1000);
            }
        }).start();
        Fonts.init();
        guiFont = new NFontRenderer(Fonts.tt0756m_, 18);
        hudFont = new NFontRenderer(Fonts.tt0756m_, 20);
        chatFont = new NFontRenderer(Fonts.tt0756m_, 16);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Display.setTitle(NAME + " " + VERSION);
        proxy.postInit(event);
    }

    public static Module getModule(String name) {
        Module module = null;
        for (Module mod : moduleList) {
            if (mod.displayName.equalsIgnoreCase(name)) {
                module = mod;
            }
        }

        return module;
    }

    public static boolean doesModuleExist(String name) {
        boolean ret = false;
        if (hacks.containsKey(name)) {
            ret = true;
        }
        return ret;
    }
}