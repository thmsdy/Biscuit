package com.fpghoti.biscuit.audio.request.youtube;

import com.fpghoti.biscuit.audio.request.RequestType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class YTImmediateRequest extends YTRequest{
	
	public YTImmediateRequest(Message message, String identifier) {
		super(message, identifier);
	}

	public YTImmediateRequest(TextChannel channel, String authorId, String identifier) {
		super(channel, authorId, identifier);
	}

	@Override
	public RequestType getType() {
		return RequestType.YOUTUBE_IMMEDIATE;
	}

}
