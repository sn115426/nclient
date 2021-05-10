package com.ngames.nclient.module.modules.render;

import static com.ngames.nclient.NClient.MC;

import com.ngames.nclient.NClient;
import com.ngames.nclient.gui.Hud;
import com.ngames.nclient.gui.font.Fonts;
import com.ngames.nclient.gui.font.NFontRenderer;
import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleType;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingValue;
import com.ngames.nclient.module.Category;

@ModuleType(
		category = Category.RENDER, 
		description = "Ingame HUD", 
		name = "HUD", 
		words = "HUD InGameGUI watermark")
public class HUD extends Module
{
	public final SettingChoose theme = new SettingChoose("Theme", (byte) 0, new String[] {
			"RG",
			"RB",
			"BlackWhite",
			"RGB"});
	private final SettingValue x = new SettingValue("X", 10, 0, 8096);
	private final SettingValue y = new SettingValue("Y", 10, 0, 8096);
	private final SettingChoose sortMode = new SettingChoose("SortMode", (byte)0, "LengthDes" , "LengthAsc", "Alphabetically");
	private final SettingChoose side = new SettingChoose("Side", (byte)0, "Left", "Right");
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
	private final SettingBoolean NClientStr = new SettingBoolean("DisplayCheatName", true);
	private final SettingBoolean displayBox = new SettingBoolean("DisplayBox", false);
	
	public HUD()
	{
		this.settings = Module.addSettings(this);
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		Hud.mainColor.setTheme(theme.getValue());
		Hud.x = (int) x.getValue();
		Hud.y = (int) y.getValue();
		Hud.updateTheme();
		Hud.enable();
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (MC.currentScreen != null)
		{
			x.max = MC.currentScreen.width;
			y.max = MC.currentScreen.height;
		}
		Hud.updateTheme();
		Hud.x = (int) this.x.getValue();
		Hud.y = (int) this.y.getValue();
		Hud.sortMode = this.sortMode.getValue();
		Hud.side = this.side.getValue();
		NClient.hudFont = new NFontRenderer(Fonts.getFontById(this.font.getValue()), 20);
		Hud.displayCheatName = this.NClientStr.getValue();
		Hud.displayBox = this.displayBox.getValue();
	}
}
