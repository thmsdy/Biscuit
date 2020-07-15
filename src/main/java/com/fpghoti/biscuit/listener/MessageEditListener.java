package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEditListener extends ListenerAdapter {

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		if(Util.isLoggable(event.getTextChannel()) && (!event.getAuthor().getName().equalsIgnoreCase("jbot") && !event.getAuthor().isBot())) {
			biscuit.log("[" + BColor.CYAN_BOLD + "MSG EDIT" + BColor.RESET + "] " + BColor.CYAN + "ID: " + BColor.RESET +
					event.getMessageId() + BColor.CYAN + " User: " + BColor.RESET +  event.getAuthor().getAsMention() +
					BColor.GREEN + " Channel: " + BColor.RESET + event.getChannel().getName());
			biscuit.log(BColor.CYAN + event.getAuthor().getName() + ": " + BColor.WHITE_BOLD + event.getMessage().getContentDisplay());
		}
	}

}
