package com.fpghoti.biscuit.audio.request.soundcloud;

import com.fpghoti.biscuit.audio.request.RequestType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SCPriorityRequest extends SCRequest{
	
	int insertSlot;
	
	public SCPriorityRequest(Message message, String identifier, int insertSlot) {
		this(message.getTextChannel(), message.getAuthor().getId(), identifier, insertSlot);
	}

	public SCPriorityRequest(TextChannel channel, String authorId, String identifier, int insertSlot) {
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
		return RequestType.SOUNDCLOUD_PRIORITY;
	}

}
