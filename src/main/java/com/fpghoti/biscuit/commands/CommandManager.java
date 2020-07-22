package com.fpghoti.biscuit.commands;

import java.util.ArrayList;
import java.util.List;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.commands.base.ConsoleCommand;
import com.fpghoti.biscuit.commands.base.CustomCommand;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

	private static List<BaseCommand> commands = new ArrayList<BaseCommand>();

	public static void parse(String message, MessageReceivedEvent e){
		Biscuit b = Biscuit.getBiscuit(e.getGuild());
		ArrayList<String> split = new ArrayList<String>();
		String fixed = message.replaceFirst(b.getProperties().getCommandSignifier(), "");
		String[] splitMsg = fixed.split(" ");
		for(String s: splitMsg){
			split.add(s);
		}
		String label = split.get(0);
		String[] args = new String[split.size() - 1];
		split.subList(1, split.size()).toArray(args);

		dispatch(e, label, args);
	}


	public static boolean dispatch(String label, String[] args) {
		return dispatch(null,label,args);
	}

	public static boolean dispatch(MessageReceivedEvent e, String label, String[] args) {
		Biscuit b = Main.getMainBiscuit();
		boolean isMain = true;
		if(e != null) {
			b = Biscuit.getBiscuit(e.getGuild());
			isMain = false;
			if(Util.contains(b.getProperties().disabledCommands(), label)) {
				return false;
			}
			if(!PermUtil.isAdmin(e.getMember()) && Util.contains(b.getProperties().disabledUserCommands(), label)) {
				return false;
			}

		}

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
			
			if(match instanceof MusicClientCommand && !b.getProperties().allowMusicBot()) {
				return false;
			}
			
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
		}else {
			if(Util.contains(Main.getMainBiscuit().getProperties().getCustomCmds(), label)) {
				CustomCommand cc = new CustomCommand(label, Main.getMainBiscuit());
				if(args.length >= 1) {
					commandReply(e, "``Command:" + " " + cc.getName() + "``");
					commandReply(e, "``Description:" + " " + cc.getDescription() + "``");
					commandReply(e, "``Usage:" + " " +  cc.getUsage() + "``");
				}else {
					commandReply(e, CustomCommand.fixPlaceholders(e, cc.getMessage()));
				}
			}else if(!isMain && Util.contains(b.getProperties().getCustomCmds(), label)) {
				CustomCommand cc = new CustomCommand(label, b);
				if(args.length >= 1) {
					commandReply(e, "``Command:" + " " + cc.getName() + "``");
					commandReply(e, "``Description:" + " " + cc.getDescription() + "``");
					commandReply(e, "``Usage:" + " " +  cc.getUsage() + "``");
				}else {
					commandReply(e, CustomCommand.fixPlaceholders(e, cc.getMessage()));
				}
			}
		}
		return true;
	}

	public static void commandReply(MessageReceivedEvent e, String msg) {
		if(e != null) {
			e.getTextChannel().sendMessage(msg).queue();
		}else {
			Main.getLogger().info(msg);
		}

	}

	public static void addCommand(BaseCommand command) {
		if(command instanceof MusicClientCommand && !Main.getMainBiscuit().getProperties().musicBotEnabled()) {
			return;
		}
		commands.add(command);
	}

	public static void removeCommand(BaseCommand command) {
		commands.remove(command);
	}

	public static List<BaseCommand> getCommands() {
		return commands;
	}

}
