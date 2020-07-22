package com.fpghoti.biscuit.util;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.logging.BColor;
import com.vdurmont.emoji.EmojiParser;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ChatFilter {

	public static String[] suffixes = {"ing","s","ed","er","es","y","ers","ier","iest","ies","ys"};

	public static boolean filter(GuildMessageReceivedEvent event) {
		return filter(event, true);
	}
	
	public static boolean filter(GuildMessageReceivedEvent event, boolean silent) {
		Biscuit biscuit = Biscuit.getBiscuit(event.getGuild());
		String msg = event.getMessage().getContentDisplay();
		
		//Message removal priority occurs in this order

		boolean found = false;

		//Naughty word check
		boolean filter = (filter(biscuit, msg));
		if(filter) {
			if(!silent) {
				biscuit.log(BColor.MAGENTA_BOLD + "Removed message from " + event.getAuthor().getName() + " for use of disallowed word(s).");
			}
			return true;
		}

		//Custom emote check
		for(Emote e : event.getMessage().getEmotes()) {
			String name = e.getName();

			for(String s : biscuit.getProperties().blockedCustomEmotes()) {
				if(s.equals(name)) {
					found = true;
				}
			}
		}

		if(found) {
			if(!silent) {
				biscuit.log(BColor.MAGENTA_BOLD + "Removed message from " + event.getAuthor().getName() + " for use of disallowed custom emote(s).");
			}
			return true;
		}

		//Unicode emote check
		for(String u : EmojiParser.extractEmojis(msg)) {
			u = EmojiParser.parseToAliases(u).replace(":","");
			for(String s : biscuit.getProperties().blockedUnicodeEmotes()) {
				if(s.equalsIgnoreCase(u)) {
					found = true;
				}
			}
		}		

		if(found) {
			if(!silent) {
				biscuit.log(BColor.MAGENTA_BOLD + "Removed message from " + event.getAuthor().getName() + " for use of disallowed unicode emote(s).");
			}
			return true;
		}

		return false;
	}

	public static boolean filter(Biscuit biscuit, String sentence){
		for(String s : sentence.split(" ")){
			if(filterWord(biscuit, s)){
				return true;
			}
		}
		return false;
	}

	public static boolean filterWord(Biscuit biscuit, String word) {
		String[] match = findMatchPair(biscuit, word);
		if(match != null) {
			return true;
		}
		return false;
	}

	public static String findMatch(Biscuit biscuit, String word) {
		String[] match = findMatchPair(biscuit, word);
		if(match == null || match[0] == null) {
			return null;
		}
		return match[0];
	}

	public static String[] findMatchPair(Biscuit biscuit, String word) {
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
		for(String item : biscuit.getProperties().getNaughtyWords()) {
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
