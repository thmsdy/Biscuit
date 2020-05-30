package com.fpghoti.biscuit.commands;

import java.util.ArrayList;
import java.util.List;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.config.PropertiesRetrieval;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

	private List<BaseCommand> commands = new ArrayList<BaseCommand>();

	public void parse(String message, MessageReceivedEvent e){
		ArrayList<String> split = new ArrayList<String>();
		String fixed = message.replaceFirst(PropertiesRetrieval.getCommandSignifier(), "");
		String[] splitMsg = fixed.split(" ");
		for(String s: splitMsg){
			split.add(s);
		}
		String label = split.get(0);
		String[] args = new String[split.size() - 1];
		split.subList(1, split.size()).toArray(args);

		dispatch(e, label, args);
	}


	public boolean dispatch(String label, String[] args) {
		return dispatch(null,label,args);
	}

	public boolean dispatch(MessageReceivedEvent e, String label, String[] args) {
		String input = label + " ";
		for (String s : args) {
			input += s + " ";
		}

		BaseCommand match = null;
		String[] trimmedArgs = null;
		StringBuilder identifier = new StringBuilder();

		for(BaseCommand cmd : commands) {
			StringBuilder tmpIdentifier = new StringBuilder();
			String[] tmpArgs = cmd.validate(input, tmpIdentifier);
			if (tmpIdentifier.length() > identifier.length()) {
				identifier = tmpIdentifier;
				match = cmd;
				trimmedArgs = tmpArgs;
			}
		}

		if(match != null) {
			if (trimmedArgs == null || (trimmedArgs.length > 0 && trimmedArgs[0].equals("?"))) {
				commandReply(e, "``Command:" + " " + match.getName() + "``");
				commandReply(e, "``Description:" + " " + match.getDescription() + "``");
				commandReply(e, "``Usage:" + " " +  match.getUsage() + "``");
				List<String> notes = match.getNotes();
				for (String note : notes) {
					commandReply(e, note);
				}
			} else {
				if(match instanceof ClientCommand && e != null) {
					((ClientCommand)match).execute(trimmedArgs, e);
				}else if(match instanceof ConsoleCommand && e == null) {
					((ConsoleCommand)match).execute(trimmedArgs);
				}
			}
		}
		return true;
	}

	public void commandReply(MessageReceivedEvent e, String msg) {
		Biscuit b = Main.getBiscuit();
		if(e != null) {
			b.say(e.getTextChannel(), msg);
		}else {
			b.log(msg);
		}

	}

	public void addCommand(BaseCommand command) {
		commands.add(command);
	}

	public void removeCommand(BaseCommand command) {
		commands.remove(command);
	}

	public List<BaseCommand> getCommands() {
		return commands;
	}

}
