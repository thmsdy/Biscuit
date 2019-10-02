package com.fpghoti.biscuit.listener;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageDeleteListener extends ListenerAdapter {

	Logger log = Main.log;

	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		if(Util.isLoggable(event.getTextChannel())) {
			log.info("MESSAGE DELETED - MSGID: " + event.getMessageId());
		}
	}

}
