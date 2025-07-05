package com.fpghoti.biscuit.commands;

import java.util.ArrayList;
import java.util.List;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.commands.base.ConsoleCommand;
import com.fpghoti.biscuit.commands.base.CustomCommand;
import com.fpghoti.biscuit.commands.base.MusicClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

	private static List<BaseCommand> commands = new ArrayList<BaseCommand>();

	public static void parse(String message, MessageReceivedEvent event){
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		ArrayList<String> split = new ArrayList<String>();
		String fixed = message.replaceFirst(b.getProperties().getCommandSignifier(), "");
		String[] splitMsg = fixed.split(" ");
		for(String s: splitMsg){
			split.add(s);
		}
		String label = split.get(0);
		String[] args = new String[split.size() - 1];
		split.subList(1, split.size()).toArray(args);

		dispatch(event, label, args);
	}


	public static boolean dispatch(String label, String[] args) {
		return dispatch(null,label,args);
	}

	public static boolean dispatch(MessageReceivedEvent event, String label, String[] args) {
		BiscuitGuild b = Main.getMainBiscuit();
		boolean isMain = true;
		if(event != null) {
			b = BiscuitGuild.getBiscuitGuild(event.getGuild());
			isMain = false;
			if(Util.contains(b.getProperties().disabledCommands(), label)) {
				return false;
			}
			if(!PermUtil.isAdmin(event.getMember()) && Util.contains(b.getProperties().disabledUserCommands(), label)) {
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
				commandReply(event, "``Command:" + " " + match.getName() + "``");
				commandReply(event, "``Description:" + " " + match.getDescription() + "``");
				commandReply(event, "``Usage:" + " " +  match.getUsage() + "``");
				List<String> notes = match.getNotes();
				for (String note : notes) {
					commandReply(event, note);
				}
			} else {
				if(match instanceof ClientCommand && event != null) {
					((ClientCommand)match).execute(trimmedArgs, event);
				}else if(match instanceof ConsoleCommand && event == null) {
					((ConsoleCommand)match).execute(trimmedArgs);
				}
			}
		}else {
			if(Util.contains(Main.getMainBiscuit().getProperties().getCustomCmds(), label)) {
				CustomCommand cc = new CustomCommand(label, Main.getMainBiscuit());
				if(args.length >= 1) {
					commandReply(event, "``Command:" + " " + cc.getName() + "``");
					commandReply(event, "``Description:" + " " + cc.getDescription() + "``");
					commandReply(event, "``Usage:" + " " +  cc.getUsage() + "``");
				}else {
					commandReply(event, CustomCommand.fixPlaceholders(event, cc.getMessage()));
				}
			}else if(!isMain && Util.contains(b.getProperties().getCustomCmds(), label)) {
				CustomCommand cc = new CustomCommand(label, b);
				if(args.length >= 1) {
					commandReply(event, "``Command:" + " " + cc.getName() + "``");
					commandReply(event, "``Description:" + " " + cc.getDescription() + "``");
					commandReply(event, "``Usage:" + " " +  cc.getUsage() + "``");
				}else {
					commandReply(event, CustomCommand.fixPlaceholders(event, cc.getMessage()));
				}
			}
		}
		return true;
	}

	public static void commandReply(MessageReceivedEvent event, String msg) {
		if(event != null) {
			MessageText.send(event.getChannel().asTextChannel(), msg);
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
