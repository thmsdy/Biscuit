package com.fpghoti.biscuit.audio.request.youtube;

import com.fpghoti.biscuit.audio.request.RequestType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class YTPriorityRequest extends YTRequest{
	
	int insertSlot;
	
	public YTPriorityRequest(Message message, String identifier, int insertSlot) {
		this(message.getTextChannel(), message.getAuthor().getId(), identifier, insertSlot);
	}

	public YTPriorityRequest(TextChannel channel, String authorId, String identifier, int insertSlot) {
		super(channel, authorId, identifier);
		this.insertSlot = insertSlot;
	}
	
	public int getSlot() {
		return insertSlot;
	}
	
	public void setSlot(int slotNumber) {
		insertSlot = slotNumber;
	}

	@Override
	public RequestType getType() {
		return RequestType.YOUTUBE_PRIORITY;
	}

}
