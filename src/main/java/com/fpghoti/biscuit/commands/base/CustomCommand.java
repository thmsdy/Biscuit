package com.fpghoti.biscuit.commands.base;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.BaseCommand;
import com.fpghoti.biscuit.commands.CommandType;
import com.fpghoti.biscuit.guild.BiscuitGuild;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CustomCommand extends BaseCommand {

	public static String fixPlaceholders(MessageReceivedEvent event, String msg) {
		msg = msg.replace("<user>", event.getAuthor().getAsMention());
		return msg;
	}

	private String name;
	private BiscuitGuild biscuit;

	public CustomCommand(String name, BiscuitGuild biscuit) {
		this.name = name;
		this.biscuit = biscuit;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getUsage() {
		return biscuit.getProperties().getCommandSignifier() + name;
	}

	@Override
	public String getDescription() {
		String desc = biscuit.getConfig().getFromConfig("cc-" + name + "-description");
		if(desc == null) {
			desc = Main.getMainBiscuit().getConfig().getFromConfig("cc-" + name + "-description");
			if(desc == null) {
				return "null";
			}
		}
		return desc;
	}

	@Override
	public CommandType getType() {
		return null;
	}

	public String getMessage() {
		String msg = biscuit.getConfig().getFromConfig("cc-" + name + "-message");
		if(msg == null) {
			msg = Main.getMainBiscuit().getConfig().getFromConfig("cc-" + name + "-message");
			if(msg == null) {
				return "null";
			}
		}
		return msg;
	}

}
