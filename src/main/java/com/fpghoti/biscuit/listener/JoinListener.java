package com.fpghoti.biscuit.listener;

import java.util.HashMap;

import org.slf4j.Logger;

import com.fpghoti.biscuit.Biscuit;
import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.user.PreUser;
import com.jcabi.aspects.Async;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {

	Logger log = Main.log;

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Biscuit b = Main.getBiscuit();
		User user = event.getMember().getUser();
		log.info("USER JOINED: " + user.getName() + " " + user.getAsMention());
		b.captchaLog("**User Joined:** ``" + user.getName() + "`` " + user.getAsMention());
		if(b.canManageServer() && PropertiesRetrieval.checkJoinInvite()) {
			logUserInvite(user);
		}else {
			b.clearInviteUses();
		}
		if(PropertiesRetrieval.customDefaultRole()) {
			if(!event.getMember().getUser().isBot()) {
				log.info("Issuing a role..");
				Role role = null;
				for(Role r : event.getGuild().getRoles()) {
					if(r.getName().equals(PropertiesRetrieval.getDefaultRole())) {
						role = r;
					}
				}
				if(role == null) {
					log.error("Cannot find role!");
					return;
				}
				
				if(PropertiesRetrieval.captchaEnabled()) {
					new PreUser(event.getMember().getUser());
				}
				
				event.getGuild().addRoleToMember(event.getMember(), role).queue();
				
			}
		}
	}
	
	@Async
	private void logUserInvite(final User user){
		final Biscuit b = Main.getBiscuit();
		for(Guild g : b.getJDA().getGuilds()) {
			g.retrieveInvites().queue((invs) -> {
				String usedInv = "Other";
				HashMap<String, Integer> newinv = new HashMap<String, Integer>();
				boolean empty = b.getInviteUses().isEmpty();
				for(Invite i : invs) {
					String code = i.getCode();
					int uses = i.getUses();
					newinv.put(code, uses);
					if(!empty &&(!b.getInviteUses().containsKey(code) || b.getInviteUses().get(code) != uses)) {
						usedInv = code;
					}
				}
				if(empty) {
					log.info("The ability for the bot to check the invites has only recently been enabled, so there is not enough data to determine the invite used. This should be fixed by the next time a user joins the server.");
					usedInv = "Unknown";
				}
				b.setInviteUses(newinv);
				log.info("INVITE INFO: " + user.getName() + " used invite: " + usedInv);
				b.captchaLog("**Invite Info:** ``" + user.getName() + "`` used invite: ``" + usedInv + "``");
			});
		}
	}


}