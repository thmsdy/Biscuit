package com.fpghoti.biscuit.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class PermUtil {

	public static boolean isAdmin(Member member){
		if(member.hasPermission(Permission.ADMINISTRATOR)){
			return true;
		}
		return false;
	}

	public static boolean isMod(Member member){
		if(isAdmin(member)){
			return true;
		}else{
			for(Role role : member.getRoles()){
				if(role.getName().equalsIgnoreCase("bouncerkey")){
					return true;
				}
			}
		}

		return false;
	}

	public static boolean hasRole(Member member, Role role){
		for(Role r : member.getRoles()){
			if(r.getName().equals(role.getName())){
				return true;
			}
		}

		return false;
	}

	public static boolean canMute(Member member){
		if(member.hasPermission(Permission.MESSAGE_MANAGE)){
			return true;
		}else{
			for(Role role : member.getRoles()){
				if(role.getName().equalsIgnoreCase("bouncerkey")){
					return true;
				}
			}
		}
		return false;
	}

}
