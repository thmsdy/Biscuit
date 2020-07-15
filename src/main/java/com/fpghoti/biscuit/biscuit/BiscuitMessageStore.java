package com.fpghoti.biscuit.biscuit;

import java.util.ArrayList;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.User;

public class BiscuitMessageStore {

	Biscuit biscuit;
	private HashMap<User, Integer> messageCounts = new HashMap<>();
	private HashMap<User, Integer> messagesTenSeconds = new HashMap<>();
	private HashMap<User, Integer> messagesTwoMin = new HashMap<>();
	
	private ArrayList<User> spammers = new ArrayList<User>();
	private ArrayList<User> spamWarned = new ArrayList<User>();
	private ArrayList<User> softmuted = new ArrayList<User>();
	
	public BiscuitMessageStore(Biscuit b) {
		this.biscuit = b;
	}
	
	public String getSpammerList() {
		String msg = "Recent spammers:";
		for( User u: spammers){
			msg = msg + " " + u.getAsMention();
		}
		return msg;
	}

	public void forgetChats() {
		messageCounts.clear();
		messagesTenSeconds.clear();
	}
	
	public void allowSoftMutedMessage() {
		messagesTwoMin.clear();
	}
	
	public boolean isTwoMinCountEmpty() {
		return messagesTwoMin.isEmpty();
	}
	
	public boolean hasTwoMinCount(User user) {
		return messagesTwoMin.containsKey(user);
	}
	
	public void setTwoMinCount(User user, Integer i) {
		messagesTwoMin.put(user,i);
	}
	
	public int getTwoMinCount(User user) {
		return messagesTwoMin.get(user);
	}
	
	public void removeTwoMinCount(User user) {
		messagesTwoMin.remove(user);
	}
	
	public boolean isTenSecondCountEmpty() {
		return messagesTenSeconds.isEmpty();
	}
	
	public boolean hasTenSecondCount(User user) {
		return messagesTenSeconds.containsKey(user);
	}
	
	public void setTenSecondCount(User user, Integer i) {
		messagesTenSeconds.put(user,i);
	}
	
	public int getTenSecondsCount(User user) {
		return messagesTenSeconds.get(user);
	}
	
	public void removeTenSecondsCount(User user) {
		messagesTenSeconds.remove(user);
	}
	
	public boolean isMessageCountEmpty() {
		return messageCounts.isEmpty();
	}
	
	public boolean hasMessageCount(User user) {
		return messageCounts.containsKey(user);
	}
	
	public void setMessageCount(User user, Integer i) {
		messageCounts.put(user,i);
	}
	
	public int getMessageCount(User user) {
		return messageCounts.get(user);
	}
	
	public void removeMessageCount(User user) {
		messageCounts.remove(user);
	}
	
	public void addSpammer(User u) {
		spammers.add(u);
	}
	
	public void removeSpammer(User u) {
		spammers.remove(u);
	}
	
	public boolean isSpammer(User u) {
		return spammers.contains(u);
	}
	
	public boolean hasNoSpammers() {
		return spammers.isEmpty();
	}
	
	public void addSpamWarned(User u) {
		spamWarned.add(u);
	}
	
	public void removeSpamWarned(User u) {
		spamWarned.remove(u);
	}
	
	public boolean isSpamWarned(User u) {
		return spamWarned.contains(u);
	}
	
	public boolean hasNoSpamWarned() {
		return spamWarned.isEmpty();
	}
	
	public void addSoftmuted(User u) {
		softmuted.add(u);
	}
	
	public void removeSoftmuted(User u) {
		softmuted.remove(u);
	}
	
	public boolean isSoftmuted(User u) {
		return softmuted.contains(u);
	}
	
	public boolean hasNoSoftmuted() {
		return softmuted.isEmpty();
	}

}
