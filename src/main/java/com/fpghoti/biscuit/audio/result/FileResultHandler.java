package com.fpghoti.biscuit.audio.result;

import com.fpghoti.biscuit.audio.AudioScheduler;
import com.fpghoti.biscuit.audio.request.PlayRequest;
import com.fpghoti.biscuit.audio.request.file.FilePriorityRequest;
import com.fpghoti.biscuit.audio.request.file.FileRequest;
import com.fpghoti.biscuit.guild.BiscuitGuild;
import com.fpghoti.biscuit.rest.MessageText;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class FileResultHandler extends ResultHandler {

	private BiscuitGuild biscuit;
	private FileRequest request;

	public FileResultHandler(FileRequest request) {
		this.request = request;
		this.biscuit = request.getBiscuitGuild();
	}

	public PlayRequest getRequest() {
		return request;
	}

	public void handleTrack(AudioTrack track) {
		AudioScheduler sched = biscuit.getAudioScheduler();

		switch(request.getType()) {

		case FILE:
			sched.queue(request.getType(), track, request.getAuthorId(), request.getRequestChannel());
			break;

		case FILE_PRIORITY:
			FilePriorityRequest prq = (FilePriorityRequest) request;
			sched.queue(request.getType(), track, request.getAuthorId(), request.getRequestChannel(), prq.getSlot());
			break;

		default:
			biscuit.error("File result handler received an incompatible request.");
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
		MessageText.send(request.getRequestChannel(), "File not found.");
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		exception.printStackTrace();
		MessageText.send(request.getRequestChannel(), "An error was encountered while attempting to load audio.");
	}

}
