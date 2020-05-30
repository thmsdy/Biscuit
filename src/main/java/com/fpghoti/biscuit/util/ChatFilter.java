package com.fpghoti.biscuit.util;

import com.fpghoti.biscuit.config.PropertiesRetrieval;

public class ChatFilter {

	//CHAT FILTER

	public static Boolean isNaughty(String sentence){
		for(String s : sentence.split(" ")){
			if(isNaughtyWord(s)){
				return true;
			}
		}
		return false;
	}

	public static Boolean isNaughtyWord(String word){
		String wordp = "";
		String word2 = word.toLowerCase();
		if(word2.length() >= 2 && word2.charAt(word2.length() -1) == '!'){
			for(int i = 0; i < word2.length() -1; i++ ){
				wordp += word2.charAt(i);
			}
			word2 = wordp;
		}
		wordp = "";
		for(int i = 0; i < word2.length(); i++ ){
			if(word2.charAt(i) != '!'){
				wordp += word2.charAt(i);
			}else{
				wordp += 'i';
			}
		}
		word2 = wordp;
		word2 = word2.replaceAll("\\p{Punct}+", "").replaceAll("1", "i").replaceAll("5", "s").replaceAll("6", "g").replaceAll("3", "e");
		String[] list = PropertiesRetrieval.getNaughtyWords();
		for(String item : list){
			if(word.equalsIgnoreCase(item) || word.equalsIgnoreCase(item + "s")){
				return true;
			}
		}
		word2 = null;
		return false;
	}

}
