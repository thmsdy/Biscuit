package com.fpghoti.biscuit.util;

import java.util.Random;

import com.fpghoti.biscuit.config.ConfigRetrieval;

import net.dv8tion.jda.api.entities.TextChannel;

public class Util {
	public static int randInt(int min, int max) {
		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static Boolean isLoggable(TextChannel c) {
		if(c == null) {
			return true;
		}
		Boolean a = true;
		for(String s: ConfigRetrieval.getFromConfig("Channels-To-Not-Chatlog").split(",")) {
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

}
