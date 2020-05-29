package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.global.SpamRecords;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RecentSpammersCommand extends ClientCommand{
	
    public RecentSpammersCommand() {
        name = "Recent Spammers";
        description = "Retrieves a list of recent spammers.";
        usage = PropertiesRetrieval.getCommandSignifier() + "recentspammers";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("recentspammers");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -recentspammers");
		String msg = "Recent spammers:";
	for( User u: SpamRecords.spammers){
		msg = msg + " " + u.getAsMention();
	}
		event.getTextChannel().sendMessage(msg).queue();
		
	}

}
