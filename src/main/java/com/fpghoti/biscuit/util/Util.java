package com.fpghoti.biscuit.util;

import java.util.Random;

import com.fpghoti.biscuit.guild.BiscuitGuild;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class Util {
	public static int randInt(int min, int max) {
		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static Boolean isLoggable(TextChannel c) {
		BiscuitGuild biscuit = BiscuitGuild.getBiscuitGuild(c.getGuild());
		Boolean a = true;
		for(String s: biscuit.getProperties().getDontLogChannels()) {
			if(c.getName().equalsIgnoreCase(s)) {
				a = false;
			}
		}
		return a;
	}
	
	public static boolean isDigit(String s){
		return s.matches("[0-9]+");
	}
	
	public static boolean contains(String[] list, String s) {
		for(String l : list) {
			if(s.equals(l)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsIgnoreCase(String[] list, String s) {
		for(String l : list) {
			if(s.equalsIgnoreCase(l)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDeciDigit(String s){
		int i = 0;
		String s2 = "";
		for(Character c : s.toCharArray()) {
			if(!(i == 0 && c == '-')) {
				s2 = s2 + c;
			}
			i++;
		}
		return s2.replace(".", "").matches("[0-9]+");
	}
	
	public static String getTime(long t) {
		long rawseconds = Math.round(t / 1000);
		long rawminutes = Math.round(rawseconds/60);
		long hour = Math.round(rawminutes/60);
		long minute = rawminutes - (hour * 60);
		long second = rawseconds - (rawminutes * 60);
		if(hour < 0) {
			hour = 0;
		}
		if(minute < 0) {
			minute = 0;
		}
		if(second <= 1) {
			second = 2;
		}
		return String.format("%02d:%02d:%02d", hour, minute, second - 1);
	}
	
	public static String getLast(String toSplit, String splitBy) {
		String[] s = toSplit.split(splitBy);
		String last = s[s.length - 1];
		return last;
	}
	
	public static String getArgsMessage(int startAt, String[] args) {
		String message = "";
		for(int i = startAt; i < args.length; i++) {
			message = message + args[i] + " ";
		}
		if (message.length() > 0) {
	        message = message.substring(0, message.length() - 1);
	    }
		return message;
	}

}
