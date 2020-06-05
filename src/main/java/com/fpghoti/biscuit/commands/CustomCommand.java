package com.fpghoti.biscuit.commands;

import com.fpghoti.biscuit.config.ConfigRetrieval;
import com.fpghoti.biscuit.config.PropertiesRetrieval;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CustomCommand extends BaseCommand {
	
	public static String fixPlaceholders(MessageReceivedEvent event, String msg) {
		msg = msg.replace("<user>", event.getAuthor().getAsMention());
		return msg;
	}
	
	private String name;
	
	public CustomCommand(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getUsage() {
		return PropertiesRetrieval.getCommandSignifier() + name;
	}
	
	@Override
	public String getDescription() {
		String desc = ConfigRetrieval.getFromConfig("cc-" + name + "-description");
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
		String msg = ConfigRetrieval.getFromConfig("cc-" + name + "-message");
		if(msg == null) {
			return "null";
		}
		return msg;
	}
	
}
