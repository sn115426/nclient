package com.ngames.nclient.module.modules.render;

import static com.ngames.nclient.NClient.MC;

import org.lwjgl.input.Keyboard;

import com.ngames.nclient.NClient;
import com.ngames.nclient.gui.Gui;
import com.ngames.nclient.gui.font.Fonts;
import com.ngames.nclient.gui.font.NFontRenderer;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.Category;
import com.ngames.nclient.module.Module;

import net.minecraft.client.gui.GuiScreen;

@ModuleType(
		category = Category.RENDER, 
		description = "Open the clickGUI", 
		name = "ClickGUI", 
		words = "ClickGUI Menu Hud HudEditor", 
		keyBind = Keyboard.KEY_RSHIFT)
public class ClickGUI extends Module
{
	private final SettingChoose font = new SettingChoose("Font", (byte)11, 
			Fonts._10_12_4_cyr_lat.getName(),
			Fonts.a_Albionic.getName(),
			Fonts.a_BighausTitulBrk_ExtraBold.getName(),
			Fonts.a_ConceptoTitulNrWv.getName(),
			Fonts.a_Futurica_ExtraBold.getName(),
			Fonts.a_Futurica_ExtraBoldItalic.getName(),
			Fonts.a_MachinaOrtoClg.getName(),
			Fonts.a_MachinaOrtoSht.getName(),
			Fonts.a_MachinaOrtoSls_Bold.getName(),
			Fonts.BLADRMF_.getName(),
			Fonts.BRITANIC.getName(),
			Fonts.BRLNSB.getName(),
			Fonts.BRLNSDB.getName(),
			Fonts.BRLNSR.getName(),
			Fonts.BROADW.getName(),
			Fonts.Hotmb___.getName(),
			Fonts.MATURASC.getName(),
			Fonts.Sneabo__.getName(),
			Fonts.STENCIL.getName(),
			Fonts.tt0628m_.getName(),
			Fonts.tt0756m_.getName(),
			Fonts.TT1024M_.getName(),
			Fonts.tt1247m_.getName(),
			Fonts.tt1248m_.getName());
	
	public ClickGUI()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		NClient.gui = new Gui(MC.currentScreen);
		MC.displayGuiScreen(NClient.gui);
	}
	
	@Override
	public void onDisable()
	{
		MC.displayGuiScreen((GuiScreen)null);
		if (MC.currentScreen == null)
		{
			MC.setIngameFocus();
		}
		Gui.justPressed = true;
		super.onDisable();
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		NClient.guiFont = new NFontRenderer(Fonts.getFontById(this.font.getValue()), 18);
	}
}
