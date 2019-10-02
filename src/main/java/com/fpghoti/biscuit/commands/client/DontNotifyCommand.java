package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.api.API;
import com.fpghoti.biscuit.commands.ClientCommand;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DontNotifyCommand extends ClientCommand{

    public DontNotifyCommand() {
        name = "Don't Notify";
        description = "Puts user in Don't Notify status.";
        usage = "-dontnotify";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("dontnotify");
    }
    
	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = API.getBiscuit();
		if(!event.getAuthor().isBot()) {
			b.log(event.getAuthor().getName() + " issued a command: -dontnotify");
			Role role = null;
			for(Role r : event.getGuild().getRoles()) {
				if(r.getName().toLowerCase().contains("don't notify")) {
					role = r;
				}
			}
			if(role == null) {
				b.error("Cannot find role!");
				return;
			}
			
			Emote done = null;
			for(Emote e : event.getGuild().getEmotes()) {
				if(e.getName().contains("ActionComplete")) {
					done = e;
				}
			}
			if(done == null) {
				b.error("Cannot find emote!");
				return;
			}
			
			event.getGuild().addRoleToMember(event.getMember(), role).queue();
			event.getMessage().addReaction(done).queue();
		}

	}

}
