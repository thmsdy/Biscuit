package com.fpghoti.biscuit.commands.client;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.commands.base.ClientCommand;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ToggleRoleCommand extends ClientCommand{

	public ToggleRoleCommand() {
		name = "ToggleRole";
		description = "Toggles specified role on/off";
		usage = Main.getMainBiscuit().getProperties().getCommandSignifier() + "togglerole <role>";
		minArgs = 1;
		maxArgs = 1;
		identifiers.add("togglerole");
		identifiers.add("tr");
	}

	@Override
	public void execute(String[] args, GuildMessageReceivedEvent event) {
		Biscuit b = Biscuit.getBiscuit(event.getGuild());
		if(!event.getAuthor().isBot()) {
			b.log(event.getAuthor().getName() + " issued a command: -togglerole " + args[0]);
			boolean foundEmote = false;

			String rolename = "";

			for(String s : b.getProperties().getToggleRoles()) {
				if(s.equalsIgnoreCase(args[0])) {
					rolename = s;
				}
			}

			if(rolename.equals("")) {
				MessageText.send(event.getChannel(), "Sorry! This role either cannot be toggled or does not exist!");
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
				if(e.getName().contains(b.getProperties().getDoneEmote())) {
					done = e;
				}
			}
			if(done != null) {
				foundEmote = true;
			}else {	
				b.error("Cannot find emote!");
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
					if(foundEmote) {
						event.getMessage().addReaction(done).queue();
					}else {
						event.getMessage().addReaction("✔").queue();
					}
				}
			}
		}

	}

}
