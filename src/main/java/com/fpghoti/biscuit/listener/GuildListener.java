package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.logging.BColor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter {

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		loadGuild(event);
	}
	
	@Override
	public void onGuildAvailable(GuildAvailableEvent event) {
		loadGuild(event);
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		unloadGuild(event);
	}
	
	@Override
	public void onGuildUnavailable(GuildUnavailableEvent event) {
		unloadGuild(event);
	}

	private void loadGuild(GenericGuildEvent event) {
		Guild g = event.getGuild();
		BiscuitGuild biscuit = BiscuitGuild.loadGuild(g);
		biscuit.log(BColor.CYAN_BOLD + "---- Joined new Guild! ----");
		biscuit.log(BColor.CYAN_BOLD + "Name: " + BColor.WHITE + g.getName());
		biscuit.log(BColor.CYAN_BOLD + "Id: "  + BColor.WHITE +  g.getId());
		biscuit.log(BColor.CYAN_BOLD + "---------------------------");
	}
	
	private void unloadGuild(GenericGuildEvent event) {
		Guild g = event.getGuild();
		BiscuitGuild biscuit = BiscuitGuild.getBiscuitGuild(g);
		biscuit.remove();
	}
	
}
