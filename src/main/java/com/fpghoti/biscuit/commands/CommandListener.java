package com.fpghoti.biscuit.commands;

import java.util.Scanner;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter implements Runnable {

	public Scanner sc;
	public JDA jda;
	Logger log = Main.log;

	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if (event.isFromType(ChannelType.TEXT)) {
			if(PermUtil.isAdmin(event.getMember()) || !PropertiesRetrieval.restrictCmdChannels() || (PropertiesRetrieval.restrictCmdChannels() && isBotChannel(event.getTextChannel()))) {
				if(!event.getAuthor().isBot() && event.getMessage().getContentDisplay().startsWith(PropertiesRetrieval.getCommandSignifier()) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()){
					Main.getBiscuit().getCommandManager().parse(event.getMessage().getContentRaw().toLowerCase(), event);
				}
			}
		}
	}

	private boolean isBotChannel(TextChannel c) {
		for(String s : PropertiesRetrieval.getCmdChannels()) {
			if(s.equals(c.getName())) {
				return true;
			}
		}
		return false;
	}

	public void run() {

		while(sc.hasNextLine()) {
			String command = sc.nextLine();
			String label = command.split(" ")[0];
			String[] args = new String[command.split(" ").length - 1];
			for(int i = 1; i < command.split(" ").length; i++) {
				args[i - 1] = command.split(" ")[i];
			}
			runCommand(label, args);
		}
	}

	private void runCommand(String label, String[] args) {
		log.info(label);
		Main.getBiscuit().getCommandManager().dispatch(label, args);
	}

}
