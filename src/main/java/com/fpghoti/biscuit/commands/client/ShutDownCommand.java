package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShutDownCommand extends ClientCommand{

    public ShutDownCommand() {
        name = "Shut Down";
        description = "Shuts down the bot.";
        usage = "-shutdown";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("shutdown");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -shutdown");
		if(PermUtil.isAdmin(event.getMember())) {

			event.getTextChannel().sendMessage("Shutting down Mr. Bouncer...").queue();
			
		}
	}

}
