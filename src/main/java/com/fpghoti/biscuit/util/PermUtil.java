package com.fpghoti.biscuit.util;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.jcabi.aspects.Async;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class PermUtil {

	public static boolean isAdmin(Member member){
		if(member.hasPermission(Permission.ADMINISTRATOR)){
			return true;
		}else{
			for(Role role : member.getRoles()){
				if(role.getName().equalsIgnoreCase(PropertiesRetrieval.getAdminRole())){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isMod(Member member){
		if(isAdmin(member)){
			return true;
		}else{
			for(Role role : member.getRoles()){
				if(role.getName().equalsIgnoreCase(PropertiesRetrieval.getModRole())){
					return true;
				}
			}
		}

		return false;
	}
	
	public static boolean hasRole(Member member, String role){
		for(Role r : member.getRoles()){
			if(r.getName().equalsIgnoreCase(role)){
				return true;
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
	
	public static boolean isBooster(Member member) {
		if(isAdmin(member)) {
			return true;
		}
		for(String r : PropertiesRetrieval.getBoosterRoles()) {
			if(hasRole(member,r)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBoosterExclusive(Role r) {
		return Util.containsIgnoreCase(PropertiesRetrieval.getBoostExclusiveRoles(),r.getName());
	}
	
	@Async
	public static void clearUndeservedRoles(Member m) {
		boolean booster = isBooster(m);
		for(Role r : m.getRoles()) {
			if(!booster && isBoosterExclusive(r)) {
				m.getGuild().removeRoleFromMember(m,r).queue();
				Main.log.info("BOOST ROLE REMOVED - Member: " + m.getUser().getName() + " (" + m.getId() + ") Role: " + r.getName());
			}
		}
	}

}
