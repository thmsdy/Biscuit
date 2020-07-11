package com.fpghoti.biscuit.config;

import com.fpghoti.biscuit.util.Util;

public class PropertiesRetrieval {

	public static String getToken(){
		return ConfigRetrieval.getFromConfig("Bot-Token");
	}
	
	public static String getCommandSignifier(){
		return ConfigRetrieval.getFromConfig("Command-Signifier");
	}
	
	public static String getDoneEmote(){
		return ConfigRetrieval.getFromConfig("Done-Emote");
	}
	
	public static String getDontNotify(){
		return ConfigRetrieval.getFromConfig("Dont-Notify-Role");
	}
	
	public static boolean captchaEnabled(){
		String value = ConfigRetrieval.getFromConfig("Captcha");
		return value.equalsIgnoreCase("true");
	}
	
	public static boolean customDefaultRole(){
		String value = ConfigRetrieval.getFromConfig("UseCustomDefaultRole");
		return value.equalsIgnoreCase("true");
	}
	
	public static String getCaptchaReward(){
		return ConfigRetrieval.getFromConfig("Captcha-Reward-Role");
	}
	
	public static String getDefaultRole(){
		return ConfigRetrieval.getFromConfig("DefaultRoleName");
	}
	
	public static String getModRole(){
		return ConfigRetrieval.getFromConfig("ModRole");
	}
	
	public static String getAdminRole(){
		return ConfigRetrieval.getFromConfig("AdminRole");
	}
	
	public static String getCaptchaLogChannel(){
		return ConfigRetrieval.getFromConfig("Captcha-Log-Channel");
	}
	
	public static boolean logCaptcha(){
		String value = ConfigRetrieval.getFromConfig("LogCaptcha");
		return value.equalsIgnoreCase("true");
	}
	
	public static boolean checkJoinInvite(){
		String value = ConfigRetrieval.getFromConfig("Check-Join-Invite");
		return value.equalsIgnoreCase("true");
	}
	
	public static boolean noCaptchaKick(){
		String value = ConfigRetrieval.getFromConfig("No-Captcha-Kick");
		return value.equalsIgnoreCase("true");
	}
	
	public static Integer noCaptchaKickTime(){
		String value = ConfigRetrieval.getFromConfig("No-Captcha-Kick-Time");
		if(!Util.isDigit(value)) {
			return 0;
		}
		return Integer.parseInt(value);
	}
	
	public static boolean logChat(){
		String value = ConfigRetrieval.getFromConfig("ChatLog");
		return value.equalsIgnoreCase("true");
	}
	
	public static String[] getNaughtyWords(){
		String [] list = ConfigRetrieval.getFromConfig("NaughtyList").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static boolean restrictCmdChannels(){
		String value = ConfigRetrieval.getFromConfig("Restrict-Cmd-Channels");
		return value.equalsIgnoreCase("true");
	}
	
	public static String[] getCmdChannels(){
		String [] list = ConfigRetrieval.getFromConfig("CmdChannels").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] getToggleRoles(){
		String [] list = ConfigRetrieval.getFromConfig("ToggleRoles").replace(" , ", ",").replace(", ", ",").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] getBoostExclusiveRoles(){
		String [] list = ConfigRetrieval.getFromConfig("Boost-Exclusive-Roles").replace(" , ", ",").replace(", ", ",").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] getBoosterRoles(){
		String [] list = ConfigRetrieval.getFromConfig("Treat-Like-Booster").replace(" , ", ",").replace(", ", ",").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] blockedUnicodeEmotes(){
		String [] list = ConfigRetrieval.getFromConfig("Block-Unicode-Emotes").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}

	public static String[] blockedCustomEmotes(){
		String [] list = ConfigRetrieval.getFromConfig("Block-Custom-Emotes").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] getCustomCmds(){
		String [] list = ConfigRetrieval.getFromConfig("Custom-Command-Names").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] disabledCommands(){
		String [] list = ConfigRetrieval.getFromConfig("DisabledCommands").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] disabledUserCommands(){
		String [] list = ConfigRetrieval.getFromConfig("DisabledUserCommands").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
	
	public static String[] getToggleChannels(){
		String [] list = ConfigRetrieval.getFromConfig("Toggle-Role-React-Channels").replace(" ", "").split(",");
		if(list.length == 1 && list[0].equals("")) {
			String[] blank = {};
			return blank;
		}
		return list;
	}
}
