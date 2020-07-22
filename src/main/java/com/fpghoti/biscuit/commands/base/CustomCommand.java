package com.fpghoti.biscuit.commands.base;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.BaseCommand;
import com.fpghoti.biscuit.commands.CommandType;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CustomCommand extends BaseCommand {
	
	public static String fixPlaceholders(MessageReceivedEvent event, String msg) {
		msg = msg.replace("<user>", event.getAuthor().getAsMention());
		return msg;
	}
	
	private String name;
	private Biscuit biscuit;
	
	public CustomCommand(String name, Biscuit biscuit) {
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
		String desc = biscuit.getConfig().getFromConfig("cc-" + name + "-description", biscuit);
		if(desc == null) {
			return "null";
		}
		return desc;
	}

	@Override
	public CommandType getType() {
		return null;
	}

	public String getMessage() {
		String msg = biscuit.getConfig().getFromConfig("cc-" + name + "-message", biscuit);
		if(msg == null) {
			return "null";
		}
		return msg;
	}
	
}
