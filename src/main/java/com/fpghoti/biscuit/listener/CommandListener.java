package com.fpghoti.biscuit.listener;

import java.util.Scanner;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.CommandManager;
import com.fpghoti.biscuit.logging.BiscuitLogger;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter implements Runnable {

	private Scanner sc;
	private BiscuitLogger log;

	public CommandListener(Scanner sc, BiscuitLogger log) {
		this.sc = sc;
		this.log = log;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if(!event.isFromGuild()) {
			return;
		}
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		if(PermUtil.isAdmin(event.getMember()) || !b.getProperties().restrictCmdChannels() || (b.getProperties().restrictCmdChannels() && isBotChannel(event.getChannel().asTextChannel()))) {
			if(!event.getAuthor().isBot() && event.getMessage().getContentDisplay().startsWith(b.getProperties().getCommandSignifier()) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()){
				CommandManager.parse(event.getMessage().getContentRaw().toLowerCase(), event);
			}
		}
	}

	private boolean isBotChannel(TextChannel c) {
		Biscuit b = Biscuit.getBiscuit(c.getGuild());
		for(String s : b.getProperties().getCmdChannels()) {
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
		CommandManager.dispatch(label, args);
	}

}
