package com.fpghoti.biscuit.audio;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.TextChannel;

public class AudioResultHandler implements AudioLoadResultHandler {

	private Biscuit biscuit;
	private String uid;
	private TextChannel channel;
	private String searchPhrase;
	private boolean search;
	private boolean first;
	private boolean playAfterQueue;

	public AudioResultHandler(String uid, TextChannel channel, boolean search, String searchPhrase, boolean first, boolean playAfterQueue) {
		this.biscuit = Biscuit.getBiscuit(channel.getGuild());
		this.uid = uid;
		this.channel = channel;
		this.search = search;
		this.searchPhrase = searchPhrase;
		this.first = first;
		this.playAfterQueue = playAfterQueue;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		if(first) {
			biscuit.getAudioScheduler().queue(track, uid, channel, 1);
		}else {
			biscuit.getAudioScheduler().queue(track, uid, channel);
		}
		if(playAfterQueue) {
			biscuit.getAudioScheduler().startPlaying();
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		AudioTrack track = playlist.getTracks().get(0);
		if(first) {
			biscuit.getAudioScheduler().queue(track, uid, channel, 1);
		}else {
			biscuit.getAudioScheduler().queue(track, uid, channel);
		}
		if(playAfterQueue) {
			biscuit.getAudioScheduler().startPlaying();
		}
	}

	@Override
	public void noMatches() {
		if(!search) {
			biscuit.log("Exact match not found. Searching instead...");
			Main.getPlayerManager().loadItemOrdered(biscuit.getGuild(),"ytsearch:" + searchPhrase, new AudioResultHandler(uid, channel, true, searchPhrase, first, false));
		}else {
			channel.sendMessage("Song match not found.").queue();
		}
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		exception.printStackTrace();
		channel.sendMessage("An error was encountered while attempting to load audio.").queue();
	}
	
	

}
