package com.ngames.nclient.baritone;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Module;

public class SafeThread
{
	public Runnable runnable;
	public Runnable ifWorldNull;
	public Module module;
	
	public SafeThread(Runnable runnable, Module module)
	{
		this.runnable = runnable;
		this.module = module;
	}
	
	public SafeThread(Runnable runnable, Runnable ifWorldNull, Module module)
	{
		this.runnable = runnable;
		this.ifWorldNull = ifWorldNull;
		this.module = module;
	}
	
	public void start()
	{
		new Thread(() -> {
			try {
				while (module.isEnabled())
				{
					if (NClient.MC.world == null)
					{
						if (ifWorldNull != null)
							ifWorldNull.run();
						else {
							BUtils.sleep(50);
						}
						continue;
					}
					runnable.run();
				}
			} catch (NullPointerException e) {
				if (module.isEnabled())
				{
					e.printStackTrace();
					BUtils.sleep(50);
					start();
				}
			}
		}).start();
	}
}
