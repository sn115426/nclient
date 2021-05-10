package com.ngames.nclient.module;

public enum Category
{
	ALL,
	CHAT,
	COMBAT,
	EXPLOIT,
	MISC,
	MOVEMENT,
	PLAYER,
	RENDER,
	WORLD;
	
	public static Category getCategory(int id)
	{
		Category ret = ALL;
		if (id == 1) { ret = MISC;} 
		if (id == 2) { ret = CHAT;}
		if (id == 3) { ret = WORLD;}
		if (id == 4) { ret = PLAYER;}
		if (id == 5) { ret = RENDER;}
		if (id == 6) { ret = COMBAT;}
		if (id == 7) { ret = EXPLOIT;}
		if (id == 8) { ret = MOVEMENT;}
		return ret;
	}
}
