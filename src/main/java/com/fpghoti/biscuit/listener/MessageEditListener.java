package com.fpghoti.biscuit.listener;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEditListener extends ListenerAdapter {

	Logger log = Main.log;

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		if(Util.isLoggable(event.getTextChannel()) && (!event.getAuthor().getName().equalsIgnoreCase("jbot") && !event.getAuthor().isBot())) {
			log.info("MESSAGE EDITED - MSGID: " + event.getMessageId() + "- @" + event.getAuthor().getName() + " " + event.getAuthor().getAsMention() +  " - CHANNEL: #" + event.getChannel().getName() + " - NEW TEXT - " + event.getMessage().getContentDisplay());
		}
	}

}
