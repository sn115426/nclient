package com.ngames.nclient.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.NClient;
import com.ngames.nclient.module.Modules;

import net.minecraft.client.gui.GuiNewChat;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat
{
	@Inject(method = "addToSentMessages", at = @At("TAIL"))
	private void addToSentMessages(String message, CallbackInfo info)
	{
		if (Modules.chatPrefix.isEnabled() && message.charAt(0) != '/' && message.charAt(0) != '#' && !message.startsWith(NClient.commandPrefix))
			message = message.substring(Modules.chatPrefix.prefix.getValue().length());
		if (Modules.chatPostfix.isEnabled() && message.charAt(0) != '/' && message.charAt(0) != '#' && !message.startsWith(NClient.commandPrefix))
			message = message.substring(0, message.length() - Modules.chatPostfix.postfix.getValue().length());
		List<String> sent = NClient.MC.ingameGUI.getChatGUI().getSentMessages();
		sent.set(sent.size() - 1, message);
	}
}
