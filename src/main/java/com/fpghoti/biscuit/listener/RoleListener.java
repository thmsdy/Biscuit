package com.fpghoti.biscuit.listener;

import com.fpghoti.biscuit.util.PermUtil;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleListener extends ListenerAdapter{

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event){
		PermUtil.clearUndeservedRoles(event.getMember());
	}

}