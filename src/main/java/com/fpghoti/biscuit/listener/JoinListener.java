package com.fpghoti.biscuit.listener;

import java.util.HashMap;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.user.CaptchaUser;
import com.fpghoti.biscuit.user.PreUser;
import com.jcabi.aspects.Async;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		User user = event.getMember().getUser();
		biscuit.log(BColor.YELLOW_BOLD + "USER JOINED: " + user.getName() + " " + user.getAsMention());
		biscuit.eventLog("**User Joined:** ``" + user.getName() + "`` " + user.getAsMention());
		if(biscuit.canManageServer() && biscuit.getProperties().checkJoinInvite()) {
			logUserInvite(user, biscuit);
		}else {
			biscuit.clearInviteUses();
		}
		if(biscuit.getProperties().customDefaultRole()) {
			if(!event.getMember().getUser().isBot()) {
				biscuit.log("Issuing a role..");
				Role role = null;
				for(Role r : event.getGuild().getRoles()) {
					if(r.getName().equals(biscuit.getProperties().getDefaultRole())) {
						role = r;
					}
				}
				if(role == null) {
					biscuit.error("Cannot find role!");
					return;
				}

				if(biscuit.getProperties().captchaEnabled()) {
					biscuit.log(BColor.MAGENTA_BOLD + "Adding pre-join check for user " + user.getName() + " (" + user.getAsMention() + ")...");
					PreUser.getPreUser(CaptchaUser.getCaptchaUser(user), biscuit);
				}

				event.getGuild().addRoleToMember(event.getMember(), role).queue();

			}
		}
	}

	@Async
	private void logUserInvite(final User user, final Biscuit b){
		Guild g = b.getGuild();
		g.retrieveInvites().queue(invs -> {
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
				b.log("The ability for the bot to check the invites has only recently been enabled, so there is not enough data to determine the invite used. This should be fixed by the next time a user joins the server.");
				usedInv = "Unknown";
			}
			b.setInviteUses(newinv);
			b.log(BColor.YELLOW_BOLD + "INVITE INFO: " + BColor.WHITE + user.getName() + " used invite: " + BColor.GREEN_BOLD + usedInv);
			b.eventLog("**Invite Info:** ``" + user.getName() + "`` used invite: ``" + usedInv + "``");
		});
	}


}