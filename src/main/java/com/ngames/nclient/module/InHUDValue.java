package com.ngames.nclient.module;

import com.ngames.nclient.gui.Hud;

public class InHUDValue
{
	private Type type;
	private Object value;
	
	public InHUDValue (Object value)
	{
		this.value = value;
		if (value instanceof Float[])
			this.type = Type.VECTOR_FLOAT;
		else if (value instanceof Integer[])
			this.type = Type.VECTOR_INTEGER;
		else if (value instanceof Float)
			this.type = Type.FLOAT;
		else if (value instanceof Integer)
			this.type = Type.INTEGER;
		else if (value instanceof String)
			this.type = Type.STRING;
		else 
			this.type = Type.NULL;
		
	}
	
	public String toString()
	{
		if (this.type == Type.VECTOR_FLOAT)
			return "[" + ((Float[]) value)[0].toString() + ", " + ((Float[]) value)[1].toString() + "]";
		if (this.type == Type.VECTOR_INTEGER)
			return "[" + ((Integer[]) value)[0].toString() + ", " + ((Integer[]) value)[1].toString() + "]";
		if (this.type == Type.FLOAT)
			return ((Float) value).toString();
		if (this.type == Type.INTEGER)
			return ((Integer) value).toString();
		if (this.type == Type.STRING)
			return ((String) value);
		return "";
	}
	
	public void set (Object value)
	{
		this.value = value;
		Hud.isHacksChanged = true;
	}
	
	public Object get()
	{
		return this.value;
	}
	
	public static enum Type
	{
		VECTOR_FLOAT,
		VECTOR_INTEGER,
		FLOAT,
		INTEGER,
		STRING,
		NULL
	}
}
