package com.fpghoti.biscuit.commands.discord;

import java.util.ArrayList;
import java.util.List;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.BaseCommand;
import com.fpghoti.biscuit.commands.CommandManager;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.commands.base.CustomCommand;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand extends ClientCommand {

	public HelpCommand() {
		name = "Help";
		description = "Pulls up help menu";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "help [Page #]";
		minArgs = 0;
		maxArgs = 1;
		identifiers.add("help");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {

		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());

		int pg = 1;
		if (args.length > 0) {
			if(Util.isDigit(args[0])) {
				pg = Integer.parseInt(args[0]);
			}else {
				MessageText.send(event.getTextChannel(), "Usage: ``" + usage + "``");
			}
		}
		List<BaseCommand> commands = new ArrayList<BaseCommand>();

		String[] ccs = biscuit.getProperties().getCustomCmds();
		for(String s : ccs) {
			if(!Util.contains(biscuit.getProperties().disabledCommands(), s)) {
				CustomCommand cc = new CustomCommand(s, biscuit);
				commands.add(cc);
			}
		}

		for(BaseCommand bc : CommandManager.getCommands()) {
			String bclabel = bc.getUsage().split(" ")[0];
			if(!Util.contains(biscuit.getProperties().disabledCommands(), bclabel.replace(Main.getMainBiscuit().getProperties().getCommandSignifier(), ""))) {
				if(bc instanceof MusicClientCommand) {
					if(biscuit.getProperties().allowMusicBot()) {
						commands.add(bc);
					}
				}else {
					commands.add(bc);
				}
			}
		}
		int pageCount = (int) Math.ceil((double) commands.size() / 8);
		if (pg > pageCount) {
			pg = pageCount;
		}

		MessageText.send(event.getTextChannel(), "**Use " + Main.getMainBiscuit().getProperties().getCommandSignifier() + "help [Page #] to navigate the different pages.**");
		MessageText.send(event.getTextChannel(), "[" + Integer.toString(pg) + "/" + Integer.toString(pageCount) + "] **Bot Commands:**");
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
		MessageText.send(event.getTextChannel(), msg);
	}

}
