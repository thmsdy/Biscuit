package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
	public void execute(String[] args, MessageReceivedEvent event) {
		BiscuitGuild b = BiscuitGuild.getBiscuitGuild(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -ping");
		MessageText.send(event.getChannel().asTextChannel(), "Pong!");	
	}

}
