package com.fpghoti.biscuit.audio.request.file;

import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.RequestType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class FileRequest extends PlayRequest{
	
	public FileRequest(Message message, String identifier) {
		this(message.getChannel().asTextChannel(), message.getAuthor().getId(), identifier);
	}

	public FileRequest(TextChannel channel, String authorId, String identifier) {
		super(channel, authorId, identifier);
	}

	@Override
	public RequestType getType() {
		return RequestType.FILE;
	}

}
