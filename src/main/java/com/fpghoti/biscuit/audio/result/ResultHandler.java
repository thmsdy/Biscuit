package com.fpghoti.biscuit.audio.result;

import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public abstract class ResultHandler implements AudioLoadResultHandler {

	private BiscuitGuild biscuit;
	private PlayRequest request;

	public PlayRequest getRequest() {
		return request;
	}

	public abstract void handleTrack(AudioTrack track);

	@Override
	public void trackLoaded(AudioTrack track) {
		handleTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		AudioTrack track = playlist.getTracks().get(0);
		handleTrack(track);
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		exception.printStackTrace();
		MessageText.send(request.getRequestChannel(), "An error was encountered while attempting to load audio.");
	}
	
	public BiscuitGuild getBiscuitGuild() {
		return biscuit;
	}

}
