package com.fpghoti.biscuit.audio.request;

import com.fpghoti.biscuit.biscuit.Biscuit;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class PlayRequest {
	
	private TextChannel channel;
	private Biscuit biscuit;
	private String authorId;
	private String identifier;
	
	public PlayRequest(Message message, String identifier) {
		this(message.getTextChannel(), message.getAuthor().getId(), identifier);
	}
	
	public PlayRequest(TextChannel channel, String authorId, String identifier) {
		this.channel = channel;
		this.authorId = authorId;
		this.identifier = identifier;
		this.biscuit = Biscuit.getBiscuit(channel.getGuild());
	}
	
	public TextChannel getRequestChannel() {
		return channel;
	}
	
	public Biscuit getBiscuit() {
		return biscuit;
	}
	
	public String getAuthorId() {
		return authorId;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public abstract RequestType getType();

}
