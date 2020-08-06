package com.fpghoti.biscuit.util;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.jcabi.aspects.Async;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class PermUtil {

	public static boolean isAdmin(Member member){
		Biscuit biscuit = Biscuit.getBiscuit(member.getGuild());
		if(member.hasPermission(Permission.ADMINISTRATOR)){
			return true;
		}else{
			for(Role role : member.getRoles()){
				if(role.getName().equalsIgnoreCase(biscuit.getProperties().getAdminRole())){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isMod(Member member){
		Biscuit biscuit = Biscuit.getBiscuit(member.getGuild());
		if(isAdmin(member)){
			return true;
		}else{
			for(Role role : member.getRoles()){
				if(role.getName().equalsIgnoreCase(biscuit.getProperties().getModRole())){
					return true;
				}
			}
		}

		return false;
	}
	
	public static boolean hasMusicControl(Member member){
		Biscuit biscuit = Biscuit.getBiscuit(member.getGuild());
		if(isAdmin(member) || isMod(member)){
			return true;
		}else{
			for(Role role : member.getRoles()){
				if(role.getName().equalsIgnoreCase(biscuit.getProperties().getMusicControllerRole())){
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
		Biscuit biscuit = Biscuit.getBiscuit(member.getGuild());
		if(isAdmin(member)) {
			return true;
		}
		for(String r : biscuit.getProperties().getBoosterRoles()) {
			if(hasRole(member,r)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBoosterExclusive(Role r) {
		Biscuit biscuit = Biscuit.getBiscuit(r.getGuild());
		return Util.containsIgnoreCase(biscuit.getProperties().getBoostExclusiveRoles(),r.getName());
	}
	
	public static boolean hasDefaultRole(Member m) {
		Biscuit biscuit = Biscuit.getBiscuit(m.getGuild());
		for(Role r : biscuit.getGuild().getRoles()) {
			if(r.getName().equalsIgnoreCase(biscuit.getProperties().getDefaultRole())){
				return hasRole(m,r);
			}
		}
		return false;
	}
	
	public static boolean hasRewardRole(Member m) {
		Biscuit biscuit = Biscuit.getBiscuit(m.getGuild());
		for(Role r : biscuit.getGuild().getRoles()) {
			if(r.getName().equalsIgnoreCase(biscuit.getProperties().getCaptchaReward())){
				return hasRole(m,r);
			}
		}
		return false;
	}
	
	@Async
	public static void clearUndeservedRoles(Member m) {
		boolean booster = isBooster(m);
		Biscuit b = Biscuit.getBiscuit(m.getGuild());
		for(Role r : m.getRoles()) {
			if(!booster && isBoosterExclusive(r)) {
				m.getGuild().removeRoleFromMember(m,r).queue();
				b.log("Booster role removed from " + m.getUser().getName() + " (" + m.getId() + ") Role: " + r.getName());
			}
		}
	}

}
