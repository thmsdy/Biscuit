package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.logging.BColor;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NicknameListener extends ListenerAdapter {

	@Override
	public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
		BiscuitGuild biscuit = BiscuitGuild.getBiscuitGuild(event.getGuild());
		User user = event.getMember().getUser();
		String oldNick = "None";
		String newNick = "None";
		
		if(event.getOldNickname() != null) {
			oldNick = event.getOldNickname();
		}
		
		if(event.getNewNickname() != null) {
			newNick = event.getNewNickname();
		}
		
		biscuit.log(BColor.CYAN + "User " + user.getName() + " " + user.getAsMention() + " changed nickname from " +
		oldNick + " to " + newNick + ".");
		
		if(event.getOldNickname() != null) {
			oldNick = "``" + event.getOldNickname() + "``";
		}
		
		if(event.getNewNickname() != null) {
			newNick = "``" + event.getNewNickname() + "``";
		}
		biscuit.eventLog("**Nickname changed:** ``" + user.getName() + "`` " + user.getAsMention() + " - From " + 
		oldNick + " to " + newNick);
	}

}