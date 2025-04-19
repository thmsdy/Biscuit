package com.fpghoti.biscuit.audio.request.youtube;

import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.RequestType;

import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class YTRequest extends PlayRequest{
	
	private boolean searchMode;
	
	public YTRequest(Message message, String identifier) {
		this(message.getChannel().asTextChannel(), message.getAuthor().getId(), identifier);
	}

	public YTRequest(TextChannel channel, String authorId, String identifier) {
		super(channel, authorId, identifier);
		searchMode = false;
	}
	
	public boolean inSearchMode() {
		return searchMode;
	}
	
	public void enableSearchMode() {
		searchMode = true;
	}
	
	public void disableSearchMode() {
		searchMode = false;
	}

	@Override
	public RequestType getType() {
		return RequestType.YOUTUBE;
	}

}
