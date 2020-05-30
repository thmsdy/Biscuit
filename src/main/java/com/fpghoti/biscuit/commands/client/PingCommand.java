package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends ClientCommand{
	
    public PingCommand() {
        name = "Ping";
        description = "Pings the bot.";
        usage = PropertiesRetrieval.getCommandSignifier() + "ping";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("ping");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Main.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -ping");
		event.getTextChannel().sendMessage("Pong!").queue();	
	}

}
