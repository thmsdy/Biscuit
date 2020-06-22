package com.fpghoti.biscuit.listener;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.config.ConfigRetrieval;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.global.MessageQueue;
import com.fpghoti.biscuit.global.SpamRecords;
import com.fpghoti.biscuit.util.ChatFilter;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceiveListener extends ListenerAdapter{

	Logger log = Main.log;


	/*TODO Cleanup*/
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if (event.isFromType(ChannelType.TEXT)) {
			PermUtil.clearUndeservedRoles(event.getMember());
			if(Util.isLoggable(event.getTextChannel())) {
				if(PropertiesRetrieval.logChat()) {
					log.info( "NEW MSG - MSGID: " + event.getMessageId() + "- @" + event.getAuthor().getName() + " " + event.getAuthor().getAsMention() +  " - CHANNEL: #" + event.getChannel().getName() + " - " + event.getMessage().getContentDisplay());
				}
			}

			if(event.getAuthor().isBot() && event.getMessage().getContentRaw().contains("This message contains words not appropriate for this channel.") || (ChatFilter.filter(event))){
				MessageQueue.removemessages.put(event.getMessage().getId(), event.getTextChannel());
			}

			//staff channels do not need filtering, as the filter could actually be a hinderance
			if(!event.getChannel().getName().toLowerCase().contains("staff") && ChatFilter.filter(event, false)){
				event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " This message contains words not appropriate for this channel.").complete();
				MessageQueue.fastremovemessages.put(event.getMessage().getId(), event.getTextChannel());
			}

			if(!event.getAuthor().isBot() && !MessageQueue.chatssent.containsKey(event.getAuthor())){
				MessageQueue.chatssent.put(event.getAuthor(), 0);
			}

			if(!event.getAuthor().isBot() && !MessageQueue.spammsgs.containsKey(event.getAuthor())){
				MessageQueue.spammsgs.put(event.getAuthor(), 0);
			}

			if(!MessageQueue.chatssent10s.containsKey(event.getAuthor()) && SpamRecords.spammers.contains(event.getAuthor())){
				MessageQueue.chatssent10s.put(event.getAuthor(), 0);
			}

			if(!MessageQueue.chatssent2m.containsKey(event.getAuthor()) && SpamRecords.softmute.contains(event.getAuthor())){
				MessageQueue.chatssent2m.put(event.getAuthor(), 0);
			}

			if(!event.getAuthor().isBot()){
				MessageQueue.spammsgs.put(event.getAuthor(), MessageQueue.spammsgs.get(event.getAuthor()) + 1);
				MessageQueue.chatssent.put(event.getAuthor(),  MessageQueue.chatssent.get(event.getAuthor()) + 1);
				if(SpamRecords.softmute.contains(event.getAuthor())){
					MessageQueue.chatssent2m.put(event.getAuthor(),  MessageQueue.chatssent2m.get(event.getAuthor()) + 1);
				}
				if(SpamRecords.spammers.contains(event.getAuthor())){
					MessageQueue.chatssent10s.put(event.getAuthor(),  MessageQueue.chatssent10s.get(event.getAuthor()) + 1);
				}
			}
			if(SpamRecords.softmute.contains(event.getAuthor()) && MessageQueue.chatssent2m.get(event.getAuthor()) > 1){
				String text = event.getMessage().getContentDisplay();
				log.info("Removed Msg - REASON SOFTMUTED - by " + event.getAuthor().getName() + ": " + text);
				MessageQueue.fastremovemessages.put(event.getMessage().getId(), event.getTextChannel());
			}
			if(SpamRecords.spammers.contains(event.getAuthor())){
				if(MessageQueue.chatssent10s.get(event.getAuthor()) > 1){
					String text = event.getMessage().getContentDisplay();
					log.info("Removed Msg - REASON FLAGGED AS SPAM - by " + event.getAuthor().getName() + ": " + text);
					MessageQueue.fastremovemessages.put(event.getMessage().getId(), event.getTextChannel());
				}
			}

			////// Listen for spammers

			if(ConfigRetrieval.getFromConfig("AllowSpamPunish").equalsIgnoreCase("true")){
				if(!event.getAuthor().isBot() && !SpamRecords.softmute.contains(event.getAuthor()) && !MessageQueue.chatssent.isEmpty() && MessageQueue.chatssent.get(event.getAuthor()) > 7){
					if(!SpamRecords.spammers.contains(event.getAuthor())){
						if(event.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_MANAGE)){
							try{
								//ignores music channels so that music bots can operate normally
								if(!event.getChannel().getName().toLowerCase().contains("music")){
									if(SpamRecords.warnedspm.contains(event.getAuthor())){
										MessageQueue.removemessages.put(event.getMessage().getId(), event.getTextChannel());
										SpamRecords.spammers.add(event.getAuthor());
										event.getTextChannel().sendMessage("*Flagging " + event.getAuthor().getAsMention() + " as spam!*").queue();
										log.info("User " + event.getAuthor().getName() + " has been flagged as spam!");
									}else{
										MessageQueue.chatssent.remove(event.getAuthor());
										SpamRecords.warnedspm.add(event.getAuthor());
										event.getTextChannel().sendMessage("**STOP spamming, " + event.getAuthor().getAsMention() + "! You have been warned!**").queue();
										log.info("User " + event.getAuthor().getName() + " has been warned for spam!");
									}
								}
							}catch(PermissionException e){
								log.info("Bot does not have permission to change the nick name of " + event.getAuthor().getName() + "!");
							}
						}
					}

					if(event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)){
						MessageQueue.fastremovemessages.put(event.getMessage().getId(), event.getTextChannel());
					}
				}
			}

			/////

		}
	}

}
