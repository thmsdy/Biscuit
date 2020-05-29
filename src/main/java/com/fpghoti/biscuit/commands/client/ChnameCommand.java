package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChnameCommand extends ClientCommand{

    public ChnameCommand() {
        name = "Channel Name";
        description = "Retrieves the channel name.";
        usage = PropertiesRetrieval.getCommandSignifier() + "chname";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("chname");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -chname");
		if(PermUtil.isMod(event.getMember()) || PermUtil.canMute(event.getMember())) {
			event.getTextChannel().sendMessage("DEBUG: " + event.getTextChannel().getName()).queue();
		}
	}

}
