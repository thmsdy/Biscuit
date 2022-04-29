package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RecentSpammersCommand extends ClientCommand{

	public RecentSpammersCommand() {
		name = "Recent Spammers";
		description = "Retrieves a list of recent spammers.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "recentspammers";
		minArgs = 0;
		maxArgs = 0;
		identifiers.add("recentspammers");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -recentspammers");
		String msg = b.getMessageStore().getSpammerList();
		MessageText.send(event.getTextChannel(), msg);

	}

}
