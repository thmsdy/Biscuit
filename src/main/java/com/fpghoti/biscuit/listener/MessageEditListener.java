package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEditListener extends ListenerAdapter {

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		BiscuitGuild biscuit = BiscuitGuild.getBiscuitGuild(event.getGuild());
		if(Util.isLoggable(event.getChannel().asTextChannel()) && (!event.getAuthor().getName().equalsIgnoreCase("jbot") && !event.getAuthor().isBot())) {
			biscuit.log("[" + BColor.CYAN_BOLD + "MSG EDIT" + BColor.RESET + "] " + BColor.CYAN + "ID: " + BColor.RESET +
					event.getMessageId() + BColor.CYAN + " User: " + BColor.RESET +  event.getAuthor().getAsMention());
			biscuit.log("[" + BColor.CYAN_BOLD + "#" + event.getChannel().getName() + BColor.RESET + "] " + BColor.CYAN + event.getAuthor().getName() + ": " + BColor.WHITE_BOLD + event.getMessage().getContentDisplay());
		}
	}

}
