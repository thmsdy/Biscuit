package com.fpghoti.biscuit.commands.client;

import java.util.ArrayList;
import java.util.List;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.BaseCommand;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.commands.CustomCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends ClientCommand {

	public HelpCommand() {
		name = "Help";
		description = "Pulls up help menu";
		usage = PropertiesRetrieval.getCommandSignifier() + "help [Page #]";
		minArgs = 0;
		maxArgs = 1;
		identifiers.add("help");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {

		Biscuit biscuit = Main.getBiscuit();

		int pg = 1;
		if (args.length > 0) {
			if(Util.isDigit(args[0])) {
				pg = Integer.parseInt(args[0]);
			}else {
				event.getTextChannel().sendMessage("Usage: ``" + usage + "``").queue();
			}
		}

		List<BaseCommand> commands = new ArrayList<BaseCommand>();
		String[] ccs = PropertiesRetrieval.getCustomCmds();
		for(String s : ccs) {
			if(!Util.contains(PropertiesRetrieval.disabledCommands(), s)) {
				CustomCommand cc = new CustomCommand(s);
				commands.add(cc);
			}
		}
		for(BaseCommand bc : biscuit.getCommandManager().getCommands()) {
			String bclabel = bc.getUsage().split(" ")[0];
			if(!Util.contains(PropertiesRetrieval.disabledCommands(), bclabel.replace(PropertiesRetrieval.getCommandSignifier(), ""))) {
				commands.add(bc);
			}
		}

		int pageCount = (int) Math.ceil((double) commands.size() / 8);
		if (pg > pageCount) {
			pg = pageCount;
		}

		event.getTextChannel().sendMessage("**Use " + PropertiesRetrieval.getCommandSignifier() + "help [Page #] to navigate the different pages.**").queue();
		event.getTextChannel().sendMessage("[" + Integer.toString(pg) + "/" + Integer.toString(pageCount) + "] **Bot Commands:**").queue();
		String msg = "";
		for (int i = 0; i < 8; i++) {
			int index = (pg - 1) * 8 + i;
			String line = "";
			if (index < commands.size()) {
				line = "**-** ``" + commands.get(index).getUsage() + "``";
			}
			if(!(index + 1 >= commands.size() || index == 7)) {
				line = line + "\n";
			}
			if (index < commands.size()) {
				msg = msg + line;
			}
		}
		event.getTextChannel().sendMessage(msg).queue();

	}

}
