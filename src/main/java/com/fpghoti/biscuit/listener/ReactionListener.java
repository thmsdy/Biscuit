package com.fpghoti.biscuit.listener;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
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

	Logger log = Main.log;

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event){
		if(event.getGuild() == null) {
			return;
		}
		if(Util.contains(PropertiesRetrieval.getToggleChannels(),event.getTextChannel().getName())) {
			handleMessageRole(event, false);
		}
	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event){
		if(event.getGuild() == null) {
			return;
		}
		if(Util.contains(PropertiesRetrieval.getToggleChannels(),event.getTextChannel().getName())) {
			handleMessageRole(event, true);
		}
	}

	private void handleMessageRole(GenericMessageReactionEvent event, boolean remove) {
		event.getTextChannel().retrieveMessageById(event.getMessageId()).queue((message) -> {
			String msg = message.getContentDisplay();
			for(String rolename : PropertiesRetrieval.getToggleRoles()) {
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
		Guild guild = event.getGuild();
		if(role == null) {
			return;
		}
		Member m = event.getMember();
		if(remove) {
			if(PermUtil.hasRole(m, role)) {
				log.info("REACTION TOGGLE (#" + event.getTextChannel().getName() + ") - Removing role " + role.getName() + " from " + m.getUser().getName() + "(" + m.getId() + ")");
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
					log.info("REACTION TOGGLE (#" + event.getTextChannel().getName() + ") - Adding role " + role.getName() + " too " + m.getUser().getName() + "(" + m.getId() + ")");
					guild.addRoleToMember(m, role).queue();
				}
			}
		}
	}


}