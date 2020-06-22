package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.commands.ClientCommand;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ToggleRoleCommand extends ClientCommand{

	public ToggleRoleCommand() {
		name = "ToggleRole";
		description = "Toggles specified role on/off";
		usage = PropertiesRetrieval.getCommandSignifier() + "togglerole <role>";
		minArgs = 1;
		maxArgs = 1;
		identifiers.add("togglerole");
		identifiers.add("tr");
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event) {
		Biscuit b = Main.getBiscuit();
		if(!event.getAuthor().isBot()) {
			b.log(event.getAuthor().getName() + " issued a command: -togglerole " + args[0]);

			String rolename = "";

			for(String s : PropertiesRetrieval.getToggleRoles()) {
				if(s.equalsIgnoreCase(args[0])) {
					rolename = s;
				}
			}

			if(rolename.equals("")) {
				event.getTextChannel().sendMessage("Sorry! This role either cannot be toggled or does not exist!").queue();
				return;
			}

			Role role = null;
			for(Role r : event.getGuild().getRoles()) {
				if(r.getName().toLowerCase().equalsIgnoreCase(rolename)) {
					role = r;
				}
			}
			if(role == null) {
				b.error("Cannot find role!");
				return;
			}

			Emote done = null;
			for(Emote e : event.getGuild().getEmotes()) {
				if(e.getName().contains(PropertiesRetrieval.getDoneEmote())) {
					done = e;
				}
			}
			if(done == null) {
				b.error("Cannot find emote!");
				return;
			}
			if(PermUtil.hasRole(event.getMember(), role)){
				event.getGuild().removeRoleFromMember(event.getMember(),role).queue();
			}else {	
				boolean canAdd = false;
				if(PermUtil.isBoosterExclusive(role)) {
					if(PermUtil.isBooster(event.getMember())) {
						canAdd = true;
					}
				}else {
					canAdd = true;
				}
				if(canAdd) {
					event.getGuild().addRoleToMember(event.getMember(), role).queue();
				}
			}
			event.getMessage().addReaction(done).queue();
		}

	}

}
