package com.fpghoti.biscuit.commands.base;

import com.fpghoti.biscuit.commands.BaseCommand;
import com.fpghoti.biscuit.commands.CommandType;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class ClientCommand extends BaseCommand{
	
	public boolean called(String[] args, MessageReceivedEvent event) {	
		return true;
	}

	public abstract void execute(String[] args, GuildMessageReceivedEvent event);
	
	public CommandType getType() {
		return CommandType.CLIENT;
	}

}
