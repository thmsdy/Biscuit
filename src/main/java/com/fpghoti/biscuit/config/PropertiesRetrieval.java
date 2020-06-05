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
		return ConfigRetrieval.getFromConfig("NaughtyList").replace(" ", "").split(",");
	}
	
	public static boolean restrictCmdChannels(){
		String value = ConfigRetrieval.getFromConfig("Restrict-Cmd-Channels");
		return value.equalsIgnoreCase("true");
	}
	
	public static String[] getCmdChannels(){
		return ConfigRetrieval.getFromConfig("CmdChannels").replace(" ", "").split(",");
	}
	
	public static String[] getToggleRoles(){
		return ConfigRetrieval.getFromConfig("ToggleRoles").replace(" ", "").split(",");
	}
	
	public static String[] blockedUnicodeEmotes(){
		return ConfigRetrieval.getFromConfig("Block-Unicode-Emotes").replace(" ", "").split(",");
	}

	public static String[] blockedCustomEmotes(){
		return ConfigRetrieval.getFromConfig("Block-Custom-Emotes").replace(" ", "").split(",");
	}
	
	public static String[] getCustomCmds(){
		return ConfigRetrieval.getFromConfig("Custom-Command-Names").replace(" ", "").split(",");
	}
	
	public static String[] disabledCommands(){
		return ConfigRetrieval.getFromConfig("DisabledCommands").replace(" ", "").split(",");
	}
	
	public static String[] disabledUserCommands(){
		return ConfigRetrieval.getFromConfig("DisabledUserCommands").replace(" ", "").split(",");
	}
}
