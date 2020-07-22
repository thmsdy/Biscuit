package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChanIDCommand extends ClientCommand{

    public ChanIDCommand() {
        name = "Channel ID";
        description = "Retrieves the channel ID.";
        usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "chanid";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("chanid");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -chanid");
		String id = event.getTextChannel().getId();
		event.getTextChannel().sendMessage(id).queue();
	}

}
