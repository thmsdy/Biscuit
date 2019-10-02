package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UIDCommand extends ClientCommand{

    public UIDCommand() {
        name = "User ID";
        description = "Retrieves a user's ID.";
        usage = "-uid @<mention-user>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("uid");
    }

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		b.log(event.getAuthor().getName() + " issued a command: -uid " + args[0]);
		for(Member m : event.getMessage().getMentionedMembers()){
			User u = m.getUser();
			String s = u.getAsMention();
			if(PermUtil.isMod(event.getMember()) || PermUtil.canMute(event.getMember()))
				event.getTextChannel().sendMessage("DEBUG: " + s+ " retrieved.").queue();
		}
	}

}