package com.fpghoti.biscuit.commands.base;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class MusicClientCommand extends ClientCommand{

	public abstract void execute(String[] args, MessageReceivedEvent event);

}
