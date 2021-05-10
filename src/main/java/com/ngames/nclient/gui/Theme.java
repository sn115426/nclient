package com.ngames.nclient.gui;

import java.util.ArrayList;
import java.util.List;

public class Theme
{
	public Theme()
	{
		List<Integer> rg = generate(255, 0, 1, true);
		rg.addAll(generate(255, 0, 0, false));
		this.RG = rg;
		List<Integer> rb = new ArrayList<>();
		rb.addAll(generate(255, 0, 2, true));
		rb.addAll(generate(0, 255, 0, false));
		this.RB = rb;
		List<Integer> bw = new ArrayList<>();
		for (int i = 0; i < 255; i++)
			bw.add(getRGB(i, i, i));
		this.BlackWhite = bw;
		List<Integer> rgb = generate(255, 0, 2, true);
		rgb.addAll(generate(0, 255, 0, false));
		rgb.addAll(generate(0, 255, 1, true));
		rgb.addAll(generate(0, 255, 2, false));
		rgb.addAll(generate(255, 0, 0, true));
		rgb.addAll(generate(255, 0, 1, false));
		this.RGB = rgb;
	}
	
	public static List<Integer> generate (int a, int b, int inc, boolean direction)
	{
		List<Integer> ret = new ArrayList<>();
		if (direction)
		{
			for (int i = 0; i < 255; i++)
				ret.add(sortRGB(a, b, i, inc));
		} else {
			for (int i = 255; i > 0; i--)
				ret.add(sortRGB(a, b, i, inc));
		}
		return ret;
	}
	
	private static int sortRGB (int a, int b, int c, int cPlace)
	{
		if (cPlace == 0)
			return getRGB(c, a, b);
		if (cPlace == 1)
			return getRGB(a, c, b);
		if (cPlace == 2)
			return getRGB(a, b, c);
		return 0;
	}
	
	public final List<Integer> RG;
	public final List<Integer> RB;
	public final List<Integer> BlackWhite;
	public final List<Integer> RGB;
	
	public List<Integer> getTheme (byte id)
	{
		switch (id)
		{
		case 0:
			return RG;
		case 1:
			return RB;
		case 2:
			return BlackWhite;
		case 3:
			return RGB;
		default:
			return RGB;
		}
	}
	
	public static int getRGB(int r, int g, int b)
	{
		return (r << 16) + (g << 8) + (b);
	}

	/*public static Integer getRGB(int r, int g, int b)
	{
		return getRGB((byte) r, (byte) g, (byte) b);
	}*/
}
