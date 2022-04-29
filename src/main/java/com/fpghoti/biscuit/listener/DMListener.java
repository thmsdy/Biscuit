package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.captcha.HandleCaptcha;
import com.fpghoti.biscuit.logging.BColor;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DMListener extends ListenerAdapter{

	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if(event.getChannelType() != ChannelType.PRIVATE) {
			return;
		}
		if (!event.getAuthor().isBot()) {

			//Log DM content to console
			if(Main.getMainBiscuit().getProperties().logChat()) {
				Main.getMainBiscuit().log("[" + BColor.CYAN_BOLD + "DM" + BColor.RESET + "] " + BColor.YELLOW + "ID: " + BColor.RESET +
						event.getMessageId() + BColor.YELLOW + " Sender: " + BColor.RESET +  event.getAuthor().getAsMention());
				Main.getMainBiscuit().log(BColor.YELLOW + event.getAuthor().getName() + ": " + BColor.WHITE_BOLD + event.getMessage().getContentDisplay());
			}

			HandleCaptcha.handleCaptcha(event.getAuthor(), event.getChannel(), event.getMessage().getContentDisplay());
		}
	}

}
