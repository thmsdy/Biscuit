package com.fpghoti.biscuit.audio.result;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.AudioScheduler;
import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTPriorityRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTRequest;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.rest.MessageText;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class YTResultHandler implements AudioLoadResultHandler {

	private Biscuit biscuit;
	private YTRequest request;

	public YTResultHandler(YTRequest request) {
		this.request = request;
		this.biscuit = request.getBiscuit();
	}

	public PlayRequest getRequest() {
		return request;
	}

	public void handleTrack(AudioTrack track) {
		AudioScheduler sched = biscuit.getAudioScheduler();

		switch(request.getType()) {

		case YOUTUBE:
			sched.queue(track, request.getAuthorId(), request.getRequestChannel());
			break;

		case YOUTUBE_PRIORITY:
			YTPriorityRequest prq = (YTPriorityRequest) request;
			sched.queue(track, request.getAuthorId(), request.getRequestChannel(), prq.getSlot());
			break;

		case YOUTUBE_IMMEDIATE:
			sched.queue(track, request.getAuthorId(), request.getRequestChannel(), 1);
			if(!sched.getQueue().isEmpty()) {
				sched.startPlaying();
			}
			break;

		default:
			biscuit.error("YouTube result handler received an incompatible request.");
			break;

		}
	}

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
	public void noMatches() {
		if(!request.inSearchMode()) {
			biscuit.log("Exact match not found. Searching instead...");
			request.enableSearchMode();
			Main.getPlayerManager().loadItemOrdered(biscuit.getGuild(),"ytsearch:" + request.getIdentifier(), new YTResultHandler(request));
		}else {
			MessageText.send(request.getRequestChannel(), "Song match not found.");
		}
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		exception.printStackTrace();
		MessageText.send(request.getRequestChannel(), "An error was encountered while attempting to load audio.");
	}

}
