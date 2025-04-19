package com.fpghoti.biscuit.audio.request;

import com.fpghoti.biscuit.audio.request.soundcloud.SCPriorityRequest;
import com.fpghoti.biscuit.audio.request.soundcloud.SCRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTImmediateRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTPriorityRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTRequest;
import com.fpghoti.biscuit.biscuit.Biscuit;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public abstract class PlayRequest {
	
	public static PlayRequest createPlayRequest(Message message, String searchPhrase) {
		return createPlayRequest(message, searchPhrase, 0);
	}
	
	public static PlayRequest createPlayRequest(Message message, String searchPhrase, int insertSlot) {
		String contents = message.getContentDisplay().toLowerCase();
		RequestType type = RequestType.YOUTUBE;
		if(insertSlot > 0) {
			type = RequestType.YOUTUBE_PRIORITY;
		}
		if(contents.contains("soundcloud.com") && searchPhrase.split(" ").length == 1) {
			type = RequestType.SOUNDCLOUD;
			if(insertSlot > 0) {
				type = RequestType.SOUNDCLOUD_PRIORITY;
			}
		}
		return createPlayRequest(type, message, searchPhrase, insertSlot);
	}
	
	public static PlayRequest createPlayRequest(RequestType type, Message message, String searchPhrase) {
		return createPlayRequest(type, message, searchPhrase, 0);
	}
	
	public static PlayRequest createPlayRequest(RequestType type, Message message, String searchPhrase, int insertSlot) {
		switch(type) {
			case YOUTUBE:
				return new YTRequest(message, searchPhrase);
			case YOUTUBE_PRIORITY:
				return new YTPriorityRequest(message, searchPhrase, insertSlot);
			case YOUTUBE_IMMEDIATE:
				return new YTImmediateRequest(message.getChannel().asTextChannel(), message.getAuthor().getId(), searchPhrase);
			case SOUNDCLOUD:
				return new SCRequest(message, searchPhrase);
			case SOUNDCLOUD_PRIORITY:
				return new SCPriorityRequest(message, searchPhrase, insertSlot);
			default:
				return null;
		}	
	}
	
	private TextChannel channel;
	private Biscuit biscuit;
	private String authorId;
	private String identifier;
	
	public PlayRequest(Message message, String identifier) {
		this(message.getChannel().asTextChannel(), message.getAuthor().getId(), identifier);
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
