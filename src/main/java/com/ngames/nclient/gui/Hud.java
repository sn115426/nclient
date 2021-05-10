package com.ngames.nclient.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ngames.nclient.NClient;
import com.ngames.nclient.baritone.BUtils;
import com.ngames.nclient.baritone.SafeThread;
import com.ngames.nclient.gui.render.Renderer2D;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleUtils;
import com.ngames.nclient.module.Modules;

public class Hud
{
	public static int x;
	public static int y;
	public static byte sortMode;
	public static byte side;
	
	public static ColorBuffer mainColor = new ColorBuffer((byte)0);
	
	private static List<String> StringList = new ArrayList<>();
	private static Map<String, String> inHuds = new HashMap<>(); //hack name, inHud value
	public static boolean isHacksChanged = true;
	private static float theLongest;
	public static boolean displayCheatName = true;
	public static boolean displayBox;
	
	public static void drawHUD()
	{
		if (mainColor.theme != Modules.hud.theme.getValue())
			updateTheme();
		if (isHacksChanged)
			updateHackList();
		
		int yStep = Math.round((float)mainColor.currentTheme.size() / StringList.size()) / (mainColor.currentTheme.size() / 255);
		int xStep = Math.round((float)mainColor.currentTheme.size() / theLongest) / (mainColor.currentTheme.size() / 255);
		int dy = y;
		ColorBuffer vbuff = new ColorBuffer(mainColor);
		for (String s : StringList)
		{
			String inHud = inHuds.get(s);
			float width = getWidth(s);
			if (displayBox)
			{
				float inHudWidth = (inHud == null ? 0 : 3 + NClient.hudFont.getWidth(inHud));
				if (side == 0)
					Renderer2D.drawRect(x - 2, dy, (int) (x + width + 2 + inHudWidth), dy + 15,
							0, 0.30f);
				else
					Renderer2D.drawRect(x - 2 - width - inHudWidth + theLongest, dy, x + 2 + theLongest, dy + 15,
							0, 0.30f);
			}
			ColorBuffer hbuff = new ColorBuffer(vbuff);
			int dx = x;
			if (side == 0)
			{
				for (Character c : s.toCharArray())
				{
					NClient.hudFont.drawStringWithShadow(c.toString(), dx, dy, hbuff.next(xStep));
					dx += NClient.hudFont.getCharWidth(c);
				}
				if (inHuds.containsKey(s))
					NClient.hudFont.drawString(inHuds.get(s), x + 3 + getWidth(s), dy, -1);
			} else {
				dx = Math.round(x + (theLongest - getWidth(s)));
				for (Character c : s.toCharArray())
				{
					NClient.hudFont.drawStringWithShadow(c.toString(), dx, dy, hbuff.next(xStep));
					dx += NClient.hudFont.getCharWidth(c);
				}
				if (inHud != null)
					NClient.hudFont.drawString(inHud, Math.round(x + (theLongest - width)) - 3 - NClient.hudFont.getWidth(inHud), dy, -1);
			}
			dy+=15;
			vbuff.next(yStep);
		}
	}
	
	public static void updateTheme()
	{
		mainColor.setTheme(Modules.hud.theme.getValue());
	}
	
	public static void updateHackList()
	{
		StringList = new ArrayList<>();
		inHuds = new HashMap<>(); //hack name, inHud value
		for (Module m : NClient.moduleList)
		{
			if (m.isEnabled() && !m.hidden.getValue())
			{
				String name = ModuleUtils.getName(m);
				StringList.add(name);
				if (m.inHud != null)
					inHuds.put(name, m.inHud.toString());
				float length = getWidth(name) + NClient.hudFont.getWidth(m.inHud == null ? "" : m.inHud.toString());
				if (length > theLongest)
					theLongest = length;
			}
		}
		Collections.sort(StringList, new Comparator<String>() {
		    public int compare(String s1, String s2)
		    {
		    	
		    	switch (sortMode)
		    	{
		    		case 0:
		    			return Float.compare(getWidth(s1) + (inHuds.get(s1) == null ? 0 : NClient.hudFont.getWidth(inHuds.get(s1)) + 3), 
		    					getWidth(s2) + (inHuds.get(s2) == null ? 0 : NClient.hudFont.getWidth(inHuds.get(s2)) + 3));
		    		case 1:
		    			return Float.compare(getWidth(s2) + (inHuds.get(s2) == null ? 0 : NClient.hudFont.getWidth(inHuds.get(s2)) + 3), 
		    					getWidth(s1) + (inHuds.get(s1) == null ? 0 : NClient.hudFont.getWidth(inHuds.get(s1)) + 3));
		    		case 2:
		    			return Integer.compare(s1.charAt(0), s2.charAt(0));
		    		default:
		    			return Float.compare(getWidth(s1) + (inHuds.get(s1) == null ? 0 : NClient.hudFont.getWidth(inHuds.get(s1)) + 3), 
		    					getWidth(s2) + (inHuds.get(s2) == null ? 0 : NClient.hudFont.getWidth(inHuds.get(s2)) + 3));
		    	}
		    }
		});
		if (displayCheatName)
			StringList.add(0, "N-Client " + NClient.VERSION);
		isHacksChanged = false;
	}
	
	private static float getWidth (String hack)
	{
		float ret = 0;
		for (char c : hack.toCharArray())
		{
			ret += NClient.hudFont.getCharWidth(c);
		}
		return ret;
	}
	
	public static void enable()
	{
		new SafeThread(() -> {
			if (Modules.hud.isEnabled())
				mainColor.next();
			else
				mainColor.j = 0;
			BUtils.sleep(5);
		}, Modules.hud).start();
	}
	
	public static class ColorBuffer
	{
		private int j = 0;
		private boolean direction = true;
		private List<Integer> currentTheme;
		public byte theme;
		
		public ColorBuffer (byte theme)
		{
			currentTheme = NClient.theme.getTheme(theme);
			this.theme = theme;
		}
		
		public ColorBuffer (ColorBuffer other)
		{
			this.j = other.j;
			this.direction = other.direction;
			this.currentTheme = other.currentTheme;
			this.theme = other.theme;
		}
		
		public void setTheme (byte theme)
		{
			currentTheme = NClient.theme.getTheme(theme);
			this.theme = theme;
			this.j = 0;
			this.direction = true;
		}
		
		public int next()
		{
			int ret = currentTheme.get(j);
			if (direction)
				j++;
			else
				j--;
			if (j == currentTheme.size() - 1)
				direction = false;
			if (j == 0)
				direction = true;
			return ret;
		}
		
		public int next (int step)
		{
			int ret = currentTheme.get(j);
			if (direction)
				j+=step;
			else
				j-=step;
			if (j >= currentTheme.size() - 1)
			{
				direction = false;
				j = currentTheme.size() - 1;
			}
			if (j <= 0)
			{
				direction = true;
				j = 0;
			}
			return ret;
		}
	}
}
