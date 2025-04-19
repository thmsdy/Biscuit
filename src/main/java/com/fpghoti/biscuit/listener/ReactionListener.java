package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.util.PermUtil;
import com.fpghoti.biscuit.util.Util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter{

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event){
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		if(event.getGuild() == null) {
			return;
		}
		if(Util.contains(biscuit.getProperties().getToggleChannels(),event.getChannel().asTextChannel().getName())) {
			handleMessageRole(event, false);
		}
	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event){
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		if(event.getGuild() == null) {
			return;
		}
		if(Util.contains(biscuit.getProperties().getToggleChannels(),event.getChannel().asTextChannel().getName())) {
			handleMessageRole(event, true);
		}
	}

	private void handleMessageRole(GenericMessageReactionEvent event, boolean remove) {
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		event.getChannel().asTextChannel().retrieveMessageById(event.getMessageId()).queue((message) -> {
			String msg = message.getContentDisplay();
			for(String rolename : biscuit.getProperties().getToggleRoles()) {
				for(Role r : event.getGuild().getRoles()) {
					if(r.getName().toLowerCase().equalsIgnoreCase(rolename)) {
						if(msg.toLowerCase().contains("[toggle " + rolename.toLowerCase() + "]")) {
							toggleRole(r, event, remove);
						}
					}
				}
			}

		}, (failure) -> {
			failure.printStackTrace();
		});
	}

	private void toggleRole(Role role, GenericMessageReactionEvent event, boolean remove) {
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		Guild guild = event.getGuild();
		if(role == null) {
			return;
		}
		Member m = event.getMember();
		if(remove) {
			if(PermUtil.hasRole(m, role)) {
				biscuit.log(BColor.MAGENTA_BOLD + "REACTION TOGGLE (#" + event.getChannel().asTextChannel().getName() + ") - " + BColor.RESET + "Removing role " + role.getName() + " from " + m.getUser().getName() + "(" + m.getId() + ")");
				guild.removeRoleFromMember(m, role).queue();
			}
		}else {
			if(!PermUtil.hasRole(m, role)) {

				boolean canAdd = false;
				if(PermUtil.isBoosterExclusive(role)) {
					if(PermUtil.isBooster(event.getMember())) {
						canAdd = true;
					}
				}else {
					canAdd = true;
				}
				if(canAdd) {
					biscuit.log(BColor.MAGENTA_BOLD + "REACTION TOGGLE (#" + event.getChannel().asTextChannel().getName() + ") - " + BColor.RESET + " Adding role " + role.getName() + " to " + m.getUser().getName() + "(" + m.getId() + ")");
					guild.addRoleToMember(m, role).queue();
				}
			}
		}
	}


}