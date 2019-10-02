package com.fpghoti.biscuit.listener;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.global.Properties;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {

	Logger log = Main.log;

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		log.info("MEMBER JOINED: " + event.getMember().getUser().getName());
		if(Properties.customdefaultrole) {
			if(!event.getMember().getUser().isBot()) {
				log.info("Issuing a role..");
				Role role = null;
				for(Role r : event.getGuild().getRoles()) {
					if(r.getName().contains(Properties.roleName)) {
						role = r;
					}
				}
				if(role == null) {
					log.error("Cannot find role!");
					return;
				}
				
				event.getGuild().addRoleToMember(event.getMember(), role).queue();
				
			}
		}
	}


}