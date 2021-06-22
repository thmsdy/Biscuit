package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.user.PreUser;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LeaveListener extends ListenerAdapter {

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		User user = event.getMember().getUser();
		PreUser u = biscuit.getPreUser(user);
		int time = 1;
		if(u != null) {
			time = u.getTimeLeft();
			u.remove();
		}
		if(time > 0) {
			biscuit.log(BColor.YELLOW + "USER LEFT: " + user.getName() + " " + user.getAsMention());
			biscuit.eventLog("**User Left: ** ``" + user.getName() + "`` " + user.getAsMention());
		}
		
	}

}