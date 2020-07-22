package com.fpghoti.biscuit.audio.queue;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class QueuedTrack {
	
	private Biscuit biscuit;
	private AudioTrack track;
	private String userId;
	private TextChannel channel;
	
	public QueuedTrack(Biscuit biscuit, AudioTrack track, String userId, TextChannel channel) {
		this.biscuit = biscuit;
		this.track = track;
		this.userId = userId;
		this.channel = channel;
	}
	
	public Biscuit getBiscuit() {
		return biscuit;
	}
	
	public AudioTrack getTrack() {
		return track;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public Member getMember() {
		return biscuit.getGuild().getMemberById(userId);
	}
	
	public TextChannel getCommandChannel() {
		return channel;
	}

}
