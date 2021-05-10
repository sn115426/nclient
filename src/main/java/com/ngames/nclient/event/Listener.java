package com.ngames.nclient.event;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Module;

public class Listener
{
	public Module module;
	private Class<? extends NClientEvent> eventType;
	
	public Listener (Class<? extends NClientEvent> eventType, Module module)
	{
		this.module = module;
		this.eventType = eventType;
		NClient.listeners.add(this);
	}
	
	public void invoke(NClientEvent event)
	{
		module.onInvoke(event);
	}
	
	public Class<? extends NClientEvent> getEventType()
	{
		return eventType;
	}
}
