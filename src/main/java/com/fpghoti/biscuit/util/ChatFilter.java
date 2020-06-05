package com.fpghoti.biscuit.util;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.vdurmont.emoji.EmojiParser;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChatFilter {

	public static String[] suffixes = {"ing","s","ed","er","es","y","ers","ier","iest","ies","ys"};

	public static boolean filter(MessageReceivedEvent event) {
		return filter(event, true);
	}
	
	public static boolean filter(MessageReceivedEvent event, boolean silent) {
		String msg = event.getMessage().getContentDisplay();

		//Message removal priority occurs in this order

		boolean found = false;

		//Naughty word check
		boolean filter = (filter(msg));
		if(filter) {
			if(!silent) {
				Main.log.info("Removed Msg - REASON NAUGHTY WORD(S) - by " + event.getAuthor().getName() + ": " + msg);
			}
			return true;
		}

		//Custom emote check
		for(Emote e : event.getMessage().getEmotes()) {
			String name = e.getName();

			for(String s : PropertiesRetrieval.blockedCustomEmotes()) {
				if(s.equals(name)) {
					found = true;
				}
			}
		}

		if(found) {
			if(!silent) {
				Main.log.info("Removed Msg - REASON BLOCKED CUSTOM EMOTE(S) - by " + event.getAuthor().getName() + ": " + msg);
			}
			return true;
		}

		//Unicode emote check
		for(String u : EmojiParser.extractEmojis(msg)) {
			u = EmojiParser.parseToAliases(u).replace(":","");
			for(String s : PropertiesRetrieval.blockedUnicodeEmotes()) {
				if(s.equalsIgnoreCase(u)) {
					found = true;
				}
			}
		}		

		if(found) {
			if(!silent) {
				Main.log.info("Removed Msg - REASON BLOCKED UNICODE EMOTE(S) - by " + event.getAuthor().getName() + ": " + msg);
			}
			return true;
		}

		return false;
	}

	public static boolean filter(String sentence){
		for(String s : sentence.split(" ")){
			if(filterWord(s)){
				return true;
			}
		}
		return false;
	}

	public static boolean filterWord(String word) {
		String[] match = findMatchPair(word);
		if(match != null) {
			return true;
		}
		return false;
	}

	public static String findMatch(String word) {
		String[] match = findMatchPair(word);
		if(match == null || match[0] == null) {
			return null;
		}
		return match[0];
	}

	public static String[] findMatchPair(String word) {
		String cleaned = "";
		word = word.toLowerCase();
		if(word.length() >= 2 && word.charAt(word.length() -1) == '!'){
			for(int i = 0; i < word.length() -1; i++ ){
				cleaned += word.charAt(i);
			}
			word = cleaned;
		}
		cleaned = "";
		for(int i = 0; i < word.length(); i++ ){
			if(word.charAt(i) != '!'){
				cleaned += word.charAt(i);
			}else{
				cleaned += 'i';
			}
		}
		cleaned = cleaned.replace(" ", "");
		cleaned = cleaned.replaceAll("\\p{Punct}+", "").replaceAll("1", "i").replaceAll("5", "s").replaceAll("6", "g").replaceAll("3", "e").replaceAll("0", "o").replaceAll("9", "g").replaceAll("8", "b");
		if(cleaned.equals("")) {
			return null;
		}
		String[] wordSuf = {null,null};
		for(String item : PropertiesRetrieval.getNaughtyWords()) {
			if(cleaned.equalsIgnoreCase(item)){
				wordSuf[0] = item;
				return wordSuf;
			}
			for(String suffix : suffixes) {
				if(cleaned.equalsIgnoreCase(item + suffix)){
					wordSuf[0] = item;
					wordSuf[1] = suffix;
					return wordSuf;
				}
				String last = item.substring(item.length() - 1);
				if(cleaned.equalsIgnoreCase(item + last + suffix)){
					wordSuf[0] = item;
					wordSuf[1] = last + suffix;
					return wordSuf;
				}
				if(cleaned.equalsIgnoreCase(item + last + last + suffix)){
					wordSuf[0] = item;
					wordSuf[1] = last + last + suffix;
					return wordSuf;
				}
			}

		}
		return null;
	}

}
