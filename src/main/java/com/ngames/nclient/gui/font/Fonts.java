package com.ngames.nclient.gui.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import com.ngames.nclient.NClient;

public class Fonts
{
	public static Font _10_12_4_cyr_lat;
	public static Font a_Albionic;
	public static Font a_BighausTitulBrk_ExtraBold;
	public static Font a_ConceptoTitulNrWv;
	public static Font a_Futurica_ExtraBold;
	public static Font a_Futurica_ExtraBoldItalic;
	public static Font a_MachinaOrtoClg;
	public static Font a_MachinaOrtoSht;
	public static Font a_MachinaOrtoSls_Bold;
	public static Font BLADRMF_;
	public static Font BRITANIC;
	public static Font BRLNSB;
	public static Font BRLNSDB;
	public static Font BRLNSR;
	public static Font BROADW;
	public static Font Hotmb___;
	public static Font MATURASC;
	public static Font Sneabo__;
	public static Font STENCIL;
	public static Font tt0628m_;
	public static Font tt0756m_;
	public static Font TT1024M_;
	public static Font tt1247m_;
	public static Font tt1248m_;
	
	public static void init()
	{
		try {
			_10_12_4_cyr_lat = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/10.12_4_cyr-lat.ttf"));
			a_Albionic = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_Albionic.ttf"));
			a_BighausTitulBrk_ExtraBold = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_BighausTitulBrk_ExtraBold.ttf"));
			a_ConceptoTitulNrWv = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_ConceptoTitulNrWv.ttf"));
			a_Futurica_ExtraBold = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_Futurica_ExtraBold.ttf"));
			a_Futurica_ExtraBoldItalic = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_Futurica_ExtraBoldItalic.ttf"));
			a_MachinaOrtoClg = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_MachinaOrtoClg.ttf"));
			a_MachinaOrtoSht = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_MachinaOrtoSht.ttf"));
			a_MachinaOrtoSls_Bold = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/a_MachinaOrtoSls_Bold.ttf"));
			BLADRMF_ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/BLADRMF_.TTF"));
			BRITANIC = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/BRITANIC.TTF"));
			BRLNSB = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/BRLNSB.TTF"));
			BRLNSDB = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/BRLNSDB.TTF"));
			BRLNSR = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/BRLNSR.TTF"));
			BROADW = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/BROADW.TTF"));
			Hotmb___ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/Hotmb___.ttf"));
			MATURASC = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/MATURASC.TTF"));
			Sneabo__ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/Sneabo__.ttf"));
			STENCIL = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/STENCIL.TTF"));
			tt0628m_ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/tt0628m_.ttf"));
			tt0756m_ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/tt0756m_.ttf"));
			TT1024M_ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/TT1024M_.TTF"));
			tt1247m_ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/tt1247m_.ttf"));
			tt1248m_ = Font.createFont(Font.TRUETYPE_FONT, NClient.class.getClassLoader().getResourceAsStream("assets/nclient/fonts/tt1248m_.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Font getFontById (int id)
	{
		try {
			return (Font) Fonts.class.getDeclaredFields()[id].get(null);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
