package com.fpghoti.biscuit.audio.request.file;

import com.fpghoti.biscuit.audio.request.RequestType;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class FilePriorityRequest extends FileRequest{
	
	int insertSlot;
	
	public FilePriorityRequest(Message message, String identifier, int insertSlot) {
		this(message.getChannel().asTextChannel(), message.getAuthor().getId(), identifier, insertSlot);
	}

	public FilePriorityRequest(TextChannel channel, String authorId, String identifier, int insertSlot) {
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
		return RequestType.FILE_PRIORITY;
	}

}
