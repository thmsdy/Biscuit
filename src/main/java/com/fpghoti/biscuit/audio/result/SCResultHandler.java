package com.fpghoti.biscuit.audio.result;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.AudioScheduler;
import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.soundcloud.SCPriorityRequest;
import com.fpghoti.biscuit.audio.request.soundcloud.SCRequest;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class SCResultHandler extends ResultHandler {

	private BiscuitGuild biscuit;
	private SCRequest request;

	public SCResultHandler(SCRequest request) {
		this.request = request;
		this.biscuit = request.getBiscuitGuild();
	}

	public PlayRequest getRequest() {
		return request;
	}

	public void handleTrack(AudioTrack track) {
		AudioScheduler sched = biscuit.getAudioScheduler();

		switch(request.getType()) {

		case SOUNDCLOUD:
			sched.queue(request.getType(), track, request.getAuthorId(), request.getRequestChannel());
			break;

		case SOUNDCLOUD_PRIORITY:
			SCPriorityRequest prq = (SCPriorityRequest) request;
			sched.queue(request.getType(), track, request.getAuthorId(), request.getRequestChannel(), prq.getSlot());
			break;

		default:
			biscuit.error("Soundcloud result handler received an incompatible request.");
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
			Main.getPlayerManager().loadItemOrdered(biscuit.getGuild(),"scsearch:" + request.getIdentifier(), new SCResultHandler(request));
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
