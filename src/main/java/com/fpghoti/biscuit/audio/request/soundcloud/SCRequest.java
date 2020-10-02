package com.fpghoti.biscuit.audio.request.soundcloud;

import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.RequestType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SCRequest extends PlayRequest{
	
	private boolean searchMode;
	
	public SCRequest(Message message, String identifier) {
		this(message.getTextChannel(), message.getAuthor().getId(), identifier);
	}

	public SCRequest(TextChannel channel, String authorId, String identifier) {
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
		return RequestType.SOUNDCLOUD;
	}

}
