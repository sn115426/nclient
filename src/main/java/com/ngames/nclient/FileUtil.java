package com.ngames.nclient;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import com.ngames.nclient.module.Module;
import com.ngames.nclient.module.ModuleUtils;
import com.ngames.nclient.module.settings.Setting;
import com.ngames.nclient.module.settings.SettingBoolean;
import com.ngames.nclient.module.settings.SettingChoose;
import com.ngames.nclient.module.settings.SettingString;
import com.ngames.nclient.module.settings.SettingValue;
import com.ngames.nclient.module.settings.Settings;

public class FileUtil
{
	public static void saveAll()
	{
		String output = "";
		for (Module h : NClient.moduleList)
		{
			output += ModuleUtils.getName(h) + "%D%";
			output += String.valueOf(h.isEnabled()) + "%D%";
			for (Setting s : h.settings)
			{
				output += s.name + "%SD%";
				if (s.type == Settings.BOOLEAN)
					output += String.valueOf(((SettingBoolean) s).getValue());
				else if (s.type == Settings.CHOOSE)
					output += String.valueOf(((SettingChoose) s).getValue());
				else if (s.type == Settings.STRING_TYPE)
					output += String.valueOf(((SettingString) s).getValue());
				else if (s.type == Settings.VALUE_TYPE)
					output += String.valueOf(((SettingValue) s).getValue());
				output += "%D%";
			}
			if (output.charAt(output.length() - 1) == '%')
				output = output.substring(0, output.length() - 3);
			output += "\n";
		}
		try {
			org.apache.commons.io.FileUtils.write(NClient.settings, output, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadAll()
	{
		List<String> input = null;
		try {
			input = org.apache.commons.io.FileUtils.readLines(NClient.settings, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (input == null || input.isEmpty())
			return;
		for (String s : input)
		{
			String[] params = s.split(Pattern.quote("%D%"));
			String name = params[0];
			boolean isEnabled = Boolean.valueOf(params[1]);
			Module module = ModuleUtils.getModule(name);
			for (int i = 2; i < params.length; i++)
			{
				String[] setting = params[i].split(Pattern.quote("%SD%"));
				String settingName = setting[0];
				String settingValue = setting[1];
				for (Setting stng : module.settings)
				{
					if (stng.name.equals(settingName))
					{
						if (stng.type == Settings.BOOLEAN)
							((SettingBoolean) stng).setValue(Boolean.valueOf(settingValue));
						else if (stng.type == Settings.CHOOSE)
							((SettingChoose) stng).setValue(Byte.valueOf(settingValue));
						else if (stng.type == Settings.STRING_TYPE)
							((SettingString) stng).setValue(String.valueOf(settingValue));
						else if (stng.type == Settings.VALUE_TYPE)
							((SettingValue) stng).setValue(Float.valueOf(settingValue));
					}
				}
			}
			if (!module.isEnabled() && isEnabled)
				module.onEnable();
		}
	}
}
