package com.fpghoti.biscuit.commands.discord;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UIDCommand extends ClientCommand{

	public UIDCommand() {
		name = "User ID";
		description = "Retrieves a user's ID.";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "uid @<mention-user>";
		minArgs = 1;
		maxArgs = 1;
		identifiers.add("uid");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		b.log(event.getAuthor().getName() + " issued a command: -uid " + args[0]);
		for(Member m : event.getMessage().getMentions().getMembers()){
			User u = m.getUser();
			if(PermUtil.isMod(event.getMember())) {
				MessageText.send(event.getChannel().asTextChannel(), u.getId());
			}
		}
	}

}
