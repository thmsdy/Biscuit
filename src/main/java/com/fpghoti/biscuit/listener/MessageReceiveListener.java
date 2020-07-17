package com.fpghoti.biscuit.listener;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.biscuit.BiscuitMessageStore;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.util.ChatFilter;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceiveListener extends ListenerAdapter{

	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if (event.isFromType(ChannelType.TEXT)) {
			Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
			if(event.getAuthor().isBot()) {
				logBot(event, biscuit);
				return;
			}
			PermUtil.clearUndeservedRoles(event.getMember());

			logUser(event, biscuit);

			if(isNaughty(event)) {
				return;
			}

			if(handleSoftmuted(event, biscuit)) {
				return;
			}
			if(!handleSpammer(event, biscuit) && biscuit.getProperties().spamPunishAllow()){
				checkNewSpammer(event, biscuit);
			}
		}
	}

	private void logBot(MessageReceivedEvent event, Biscuit biscuit) {
		if(Util.isLoggable(event.getTextChannel())) {
			if(biscuit.getProperties().logChat()) {
				biscuit.log("[" + BColor.BLACK_BOLD + "BOT" + BColor.RESET + "] [" + BColor.RED + "#" + event.getChannel().getName() + BColor.RESET + "] " 
			+ BColor.RED_BOLD + event.getAuthor().getName() + ": " + BColor.RESET + event.getMessage().getContentDisplay());
			}
		}
	}
	
	private void logUser(MessageReceivedEvent event, Biscuit biscuit) {
		if(Util.isLoggable(event.getTextChannel())) {
			if(biscuit.getProperties().logChat()) {
				biscuit.log("[" + BColor.CYAN_BOLD + "MSG" + BColor.RESET + "] " + BColor.GREEN + "ID: " + BColor.RESET +
						event.getMessageId() + BColor.GREEN + " Sender: " + BColor.RESET +  event.getAuthor().getAsMention() +
						BColor.GREEN + " Channel: " + BColor.RESET + event.getChannel().getName());
				String msg = event.getMessage().getContentDisplay();
				
				if(event.getMessage().getAttachments().size() >= 1) {
					String tail = BColor.CYAN + "[ATTACHMENT(S)]: ";
					if(!msg.equals("")) {
						msg = msg + " ";
					}
					for(Attachment a : event.getMessage().getAttachments()) {
						tail = tail + " " +  a.getUrl();
					}
					msg = msg + tail;
				}
				
				biscuit.log(BColor.GREEN_BOLD + event.getAuthor().getName() + ": " + BColor.WHITE_BOLD + msg);
			}
		}
	}

	private boolean isNaughty(MessageReceivedEvent event) {
		// TODO make staff filter configurable
		if(!event.getChannel().getName().toLowerCase().contains("staff") && ChatFilter.filter(event, false)){
			event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " This message contains words not appropriate for this channel.").queue(new Consumer<Message>()
			{
				@Override
				public void accept(Message msg){
					msg.delete().submitAfter(3, TimeUnit.SECONDS);
				}
			});
			event.getMessage().delete().submit();
			return true;
		}
		return false;
	}

	private boolean handleSpammer(MessageReceivedEvent event, Biscuit biscuit) {
		//TODO make numbers configurable
		BiscuitMessageStore store = biscuit.getMessageStore();
		String mention = event.getAuthor().getAsMention();
		if(store.isSpammer(event.getAuthor())){
			//User is a spammer but has not sent a message during
			//the current interval. Count message and take no action
			if(!store.hasTenSecondCount(event.getAuthor())) {
				store.setTenSecondCount(event.getAuthor(), 1);
				return false;
			}
			//User is a spammer and has sent another message during
			//the current interval. Remove message.
			if(store.getTenSecondsCount(event.getAuthor()) >= 1) {
				biscuit.log(BColor.MAGENTA_BOLD + "Removed spam message from " + event.getAuthor().getName());
				event.getMessage().delete().reason("Spam removal activated for " + mention).submit();
				return true;
			}
		}	
		return false;
	}

	private boolean handleSoftmuted(MessageReceivedEvent event, Biscuit biscuit) {
		//TODO make numbers configurable
		BiscuitMessageStore store = biscuit.getMessageStore();
		String mention = event.getAuthor().getAsMention();
		if(store.isSoftmuted(event.getAuthor())){
			//User is softmuted but has not sent a message during
			//the current interval. Count message and take no action
			if(!store.hasTwoMinCount(event.getAuthor())) {
				store.setTwoMinCount(event.getAuthor(), 1);
				return false;
			}
			//User is softmuted and has sent another message during
			//the current interval. Remove message.
			if(store.getTwoMinCount(event.getAuthor()) >= 1) {
				biscuit.log(BColor.MAGENTA_BOLD + event.getAuthor().getName() + " is softmuted. Removing message...");
				event.getMessage().delete().reason("User is softmuted: " + mention).submit();
				return true;
			}
		}	
		return false;
	}

	private void checkNewSpammer(MessageReceivedEvent event, Biscuit biscuit) {
		BiscuitMessageStore store = biscuit.getMessageStore();
		String mention = event.getAuthor().getAsMention();

		if(!store.hasMessageCount(event.getAuthor())) {
			store.setMessageCount(event.getAuthor(), 1);
			return;
		}else {
			store.setMessageCount(event.getAuthor(), store.getMessageCount(event.getAuthor()) + 1);
		}

		if(store.getMessageCount(event.getAuthor()) > 7){
			//User has already been warned. Remove warning and apply restrictions.
			if(!store.isSpammer(event.getAuthor()) && store.isSpamWarned(event.getAuthor())){
				store.addSpammer(event.getAuthor());
				store.removeSpamWarned(event.getAuthor());
				event.getMessage().delete().submit();
				event.getTextChannel().sendMessage("*Flagging " + mention + " as spam!*").queue(new Consumer<Message>()
				{
					@Override
					public void accept(Message msg){
						msg.delete().reason("Automatic bot message removal").submitAfter(3, TimeUnit.SECONDS);
					}
				});
				biscuit.log(BColor.MAGENTA_BOLD + "User " + event.getAuthor().getName() + " has been flagged as spam!");
				event.getMessage().delete().reason("Spam removal activated for " + mention).submit();
				//User is spamming and has not been warned. Apply warning.
			}else if(!store.isSpammer(event.getAuthor()) && !store.isSpamWarned(event.getAuthor())){
				store.removeMessageCount(event.getAuthor());
				store.addSpamWarned(event.getAuthor());
				event.getTextChannel().sendMessage("**STOP spamming, " + mention + "! You have been warned!**").queue(new Consumer<Message>()
				{
					@Override
					public void accept(Message msg){
						msg.delete().reason("Automatic bot message removal").submitAfter(3, TimeUnit.SECONDS);
					}
				});
				biscuit.log(BColor.MAGENTA_BOLD + "User " + event.getAuthor().getName() + " has been warned for spam!");
			}
		}
	}


}
