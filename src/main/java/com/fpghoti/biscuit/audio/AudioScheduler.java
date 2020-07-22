package com.fpghoti.biscuit.audio;

import java.util.ArrayList;

import com.fpghoti.biscuit.audio.queue.AudioQueue;
import com.fpghoti.biscuit.audio.queue.QueuedTrack;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.util.Util;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.api.entities.TextChannel;

public class AudioScheduler extends AudioEventAdapter {

	private Biscuit biscuit;
	private AudioQueue queue;
	private ArrayList<String> skips;
	private boolean loop;

	public AudioScheduler(Biscuit biscuit) {
		this.biscuit = biscuit;
		this.queue = new AudioQueue();
		this.skips = new ArrayList<String>();
		this.loop = false;
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		log("The current track was paused.");
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		log("The current track was unpaused.");
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		log("Starting track " + track.getInfo().title + ".");
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

		QueuedTrack qt = queue.getLastTrack();
		String title = track.getInfo().title;

		switch(endReason) {

		case LOAD_FAILED:
			warn("Something went wrong while trying to load the current track. The next track will be played instead if available.");
			if(qt != null && qt.getCommandChannel() != null)
				qt.getCommandChannel().sendMessage("There was an error loading **" + title + "**.").queue();
			break;
		case CLEANUP:
			log("A track stopped playing due to audio player cleanup.");
			if(qt != null && qt.getCommandChannel() != null)
				qt.getCommandChannel().sendMessage("Track **" + track.getInfo().title + "** stopped playing due to audio player cleanup.").queue();
			break;
		case FINISHED:
			log("Finished playing track " + title + ".");
			break;
		case REPLACED:
			log("Skipped track " + title + ".");
			break;
		case STOPPED:
			log("Stopped playing track " + title + ".");
			break;
		default:
			break;

		}

		if (endReason.mayStartNext) {
			if(loop) {
				queueFirst(track.makeClone(), qt.getUserId(), qt.getCommandChannel());
			}
			startPlaying();
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		//TODO fix vevo
		if(track.getInfo().title.toLowerCase().contains("vevo") || track.getInfo().author.toLowerCase().contains("vevo")) {
			QueuedTrack qt = queue.getPreviousTrack(track);
			qt.getCommandChannel().sendMessage("**" + track.getInfo().title + "** could not be loaded, because it is a Vevo video.").queue();
		}
		biscuit.error("An exception occurred while trying to play a certain track. The next track will be played instead if available.");
		startPlaying();
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		QueuedTrack qt = queue.getPreviousTrack(track);
		if(qt != null && qt.getCommandChannel() != null) {
			qt.getCommandChannel().sendMessage("The current track has become stuck and will be skipped.").queue();
		}
	}

	public void queue(AudioTrack track, String uid) {
		queue(track, uid, null);
	}

	public void queue(AudioTrack track, String uid, TextChannel channel) {
		if(queue.isEmpty() && biscuit.getAudioPlayer().getPlayingTrack() == null) {
			queue.addPreviousTrack(new QueuedTrack(biscuit, track, uid, channel));
			biscuit.getAudioPlayer().playTrack(track);
		}else {
			queue.add(new QueuedTrack(biscuit, track, uid, channel));
		}
	}
	
	public void queueFirst(AudioTrack track, String uid, TextChannel channel) {
		if(queue.isEmpty() && biscuit.getAudioPlayer().getPlayingTrack() == null) {
			queue.addPreviousTrack(new QueuedTrack(biscuit, track, uid, channel));
			biscuit.getAudioPlayer().playTrack(track);
		}else {
			queue.addAtPlace(new QueuedTrack(biscuit, track, uid, channel), 1);
		}
	}

	public void startPlaying() {
		if(queue.isEmpty()) {
			if(loop) {
				return;
			}
			biscuit.getGuild().getAudioManager().closeAudioConnection();
			skips.clear();
			return;
		}
		queue.playNext();
		skips.clear();
	}
	
	public void setLooping(boolean b) {
		loop = b;
	}
	
	public boolean isLooping() {
		return loop;
	}

	public boolean hasSkipped(String id) {
		return skips.contains(id);
	}

	public boolean voteSkip(String id) {
		if(!hasSkipped(id)) {
			skips.add(id);
			return true;
		}
		return false;
	}

	public int getVotes() {
		return skips.size();
	}

	public AudioQueue getQueue(){
		return queue;
	}

	public void skip() {
		biscuit.getAudioPlayer().stopTrack();
		startPlaying();
	}

	public String getNextMessage() {
		if(queue.isEmpty()) {
			return null;
		}
		AudioTrack track = queue.getNext().getTrack();
		return getMessage(track);
	}

	public String getMessage(AudioTrack track) {
		return getMessage(track, false);
	}

	public String getMessage(AudioTrack track, Boolean showRemaining) {
		String msg ="**Now Playing: **\n" + track.getInfo().uri + "\n```" +  track.getInfo().title + "\nBy: "
				+ track.getInfo().author + "\nLength: " + Util.getTime(track.getDuration());
		if(showRemaining) {
			msg = msg + "\nTime Remaining: " + Util.getTime(track.getDuration() - track.getPosition()) + "```";
		}else {
			msg = msg + "```";
		}
		return msg;
	}

	public void wipeQueue() {
		queue.clear();
	}

	public Biscuit getBiscuit() {
		return biscuit;
	}

	public void log(String message) {
		if(biscuit.getProperties().logMusicPlayer()) {
			biscuit.log(message);
		}
	}

	public void warn(String message) {
		if(biscuit.getProperties().logMusicPlayer()) {
			biscuit.warn(message);
		}
	}

}