package com.fpghoti.biscuit.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class ClientCommand extends BaseCommand{
	
	public boolean called(String[] args, MessageReceivedEvent event) {	
		return true;
	}

	public abstract void execute(String[] args, MessageReceivedEvent event);
	
	public CommandType getType() {
		return CommandType.CLIENT;
	}

}
