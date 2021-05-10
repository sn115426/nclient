package com.ngames.nclient.event;

import com.ngames.nclient.NClient;

public class NClientEvent
{
	private boolean canceled = false;
	
	//RETURN: canceled or not
	public static boolean callEvent (NClientEvent event)
	{
		for (int i = 0; i < NClient.listeners.size(); i++)
		{ 
			Listener l = NClient.listeners.get(i);
			if (l.getEventType() == event.getClass())
				l.invoke(event);
			if (!l.module.isEnabled())
				NClient.listeners.remove(l);
			if (event.isCanceled())
				return true;
		}
		return false;
	}
	
	public void setCalceled()
	{
		canceled = true;
	}
	
	public boolean isCanceled()
	{
		return canceled;
	}
	
	public static class PlayerHealthChangeEvent extends NClientEvent {}
	public static class PlayerFoodStatsChangeEvent extends NClientEvent {}
	public static class PlayerJoinWorldEvent extends NClientEvent {}
	public static class PlayerSwingArmEvent extends NClientEvent {}
	public static class PlayerAttackedEntityEvent extends NClientEvent {}
	public static class RunTickKeyboardEvent extends NClientEvent {}
	public static class OnPlayerUpdateEvent extends NClientEvent {}
	public static class OnPlayerUpdatedEvent extends NClientEvent {}
	public static class PlayerJumpEvent extends NClientEvent {}
	public static class LivingUpdatedEvent extends NClientEvent {}
	public static class PotionEffectRemovedEvent extends NClientEvent {}
}
