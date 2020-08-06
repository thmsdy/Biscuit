package com.fpghoti.biscuit.audio;

import java.util.ArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.audio.queue.AudioQueue;
import com.fpghoti.biscuit.audio.queue.QueuedTrack;
import com.fpghoti.biscuit.audio.request.youtube.YTImmediateRequest;
import com.fpghoti.biscuit.audio.request.youtube.YTRequest;
import com.fpghoti.biscuit.audio.result.YTResultHandler;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.rest.MessageText;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.api.entities.MessageEmbed;
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
			warn("Something went wrong while trying to load the current track. Trying alternate track.");
			if(!qt.triedAlternative()) {
				qt.useAttempt();
				TextChannel channel = qt.getCommandChannel();
				MessageText.send(channel, "The video selected cannot be played through the music player. An alternate track will be played if available.");
				title = title.toLowerCase().replace("vevo", "") + " lyrics";
				YTRequest request = new YTImmediateRequest(channel, qt.getUserId(), title);
				Main.getPlayerManager().loadItemOrdered(biscuit.getGuild(),"ytsearch:" + title, new YTResultHandler(request));
				return;
			}
			break;
		case CLEANUP:
			log("A track stopped playing due to audio player cleanup.");
			if(qt != null && qt.getCommandChannel() != null)
				MessageText.send(qt.getCommandChannel(), "Track **" + track.getInfo().title + "** stopped playing due to audio player cleanup.");
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
				queue(track.makeClone(), qt.getUserId(), qt.getCommandChannel(), 1);
			}
			startPlaying();
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		QueuedTrack qt = queue.getPreviousTrack(track);
		if(!qt.triedAlternative()) {
			return;
		}
		warn("No alternative track found. This means the video is most likely unavailable. The next track will be played instead if available.");
		startPlaying();
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		QueuedTrack qt = queue.getPreviousTrack(track);
		if(qt != null && qt.getCommandChannel() != null) {
			MessageText.send(qt.getCommandChannel(), "The current track has become stuck and will be skipped.");
		}
	}

	public void queue(AudioTrack track, String uid, TextChannel channel) {
		queue(track, uid, channel, null);
	}

	public void queue(AudioTrack track, String uid, TextChannel channel, Integer place) {
		if(queue.isEmpty() && biscuit.getAudioPlayer().getPlayingTrack() == null) {
			QueuedTrack qt = new QueuedTrack(biscuit, track, uid, channel);
			queue.sendQueueMessage(qt);
			queue.addPreviousTrack(qt);
			biscuit.getAudioPlayer().playTrack(track);
		}else {
			if(place != null) {
				queue.addAtPlace(new QueuedTrack(biscuit, track, uid, channel), place);
			}else {
				queue.add(new QueuedTrack(biscuit, track, uid, channel));
			}
		}
	}

	public void startPlaying() {
		if(queue.isEmpty()) {
			if(loop) {
				return;
			}			
			biscuit.getGuild().getAudioManager().closeAudioConnection();
			biscuit.getAudioPlayer().setVolume(100);
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
		skip(null);
	}

	public void skip(TextChannel channel) {
		biscuit.getAudioPlayer().stopTrack();
		if(channel != null && queue.getNext() != null ) {
			MessageEmbed next = queue.getNext().getEmbedMessage("Now Playing:");
			MessageText.send(channel, next);
		}
		startPlaying();
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