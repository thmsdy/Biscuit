package com.fpghoti.biscuit.global;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class MessageQueue {
	
	public static HashMap<User, Integer> chatssent = new HashMap<>();
	public static HashMap<User, Integer> spammsgs = new HashMap<>();
	public static HashMap<User, Integer> chatssent10s = new HashMap<>();
	public static HashMap<User, Integer> chatssent2m = new HashMap<>();
	public static ConcurrentHashMap<String, TextChannel> removemessages = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, TextChannel> fastremovemessages = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, TextChannel> slowremovemessages = new ConcurrentHashMap<>();
	
}
