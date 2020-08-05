package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PingCommand extends ClientCommand{
	
    public PingCommand() {
        name = "Ping";
        description = "Pings the bot.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "ping";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("ping");
    }

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -ping");
		MessageText.send(event.getChannel(), "Pong!");	
	}

}
