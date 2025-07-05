package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.logging.BColor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NameListener extends ListenerAdapter {

	@Override
	public void onUserUpdateName(UserUpdateNameEvent event) {
		User user = event.getUser();
		Main.getMainBiscuit().log(BColor.CYAN_BOLD + "User " + user.getName() + " " + user.getAsMention() + " changed username from " +
				event.getOldName() + " to " + event.getNewName() + ".");
		for(Guild guild : user.getMutualGuilds()) {
			BiscuitGuild biscuit = BiscuitGuild.getBiscuitGuild(guild);
			biscuit.eventLog("**Username changed:** ``" + user.getName() + "`` " + user.getAsMention() + " - From ``" + 
			event.getOldName() + "`` to ``" + event.getNewName() + "``");
		}
	}

}