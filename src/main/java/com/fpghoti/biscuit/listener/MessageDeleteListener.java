package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageDeleteListener extends ListenerAdapter {

	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		if(Util.isLoggable(event.getChannel().asTextChannel())) {
			biscuit.log("[" + BColor.MAGENTA + "#" + event.getChannel().getName() + BColor.RESET + "] " + BColor.MAGENTA_BOLD + "Message " + event.getMessageId() + " was deleted.");
		}
	}

}
